package io.derek.hackathon.feature;

import java.io.File;
import java.io.IOException;

import org.apache.uima.resource.ResourceInitializationException;
import org.deeplearning4j.text.tokenizerfactory.UimaTokenizerFactory;
import org.deeplearning4j.util.SerializationUtils;
import org.deeplearning4j.word2vec.Word2Vec;
import org.deeplearning4j.word2vec.inputsanitation.InputHomogenization;
import org.deeplearning4j.word2vec.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.word2vec.sentenceiterator.SentenceIterator;
import org.deeplearning4j.word2vec.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.word2vec.tokenizer.TokenizerFactory;

import com.google.common.base.Preconditions;

public class WordVectorizer {
	public static final String TRAINSET_PATH = "res/train"; 
	public static final String MODEL_PATH = "res/model/model.mod";
	Word2Vec model;

	public void train(boolean forceReTrain) throws IOException, ResourceInitializationException {
		WikipediaScraper.scrape();
		SentenceIterator lineIter = new FileSentenceIterator(new File(TRAINSET_PATH));

		lineIter.setPreProcessor(new SentencePreProcessor() {
			public String preProcess(String sentence) {
				String base = new InputHomogenization(sentence).transform();
				base = base.replaceAll("@.*", "");
				return Preprocessor.preprocess(base);
			}
		});
		File modelFile = new File(MODEL_PATH);
		
		if (!forceReTrain && modelFile.exists()) {
			model = SerializationUtils.readObject(new File("res/model/model.mod"));
		} else {
			TokenizerFactory tokenizerFactory = new UimaTokenizerFactory();
			Word2Vec vec = new Word2Vec(tokenizerFactory, lineIter, 5);
			vec.train();
			model = vec;
			SerializationUtils.saveObject(vec, modelFile);
		}
		
	}
	
	public Double query(String word1, String word2) {
		Preconditions.checkNotNull(model, "model not trained!");
		return model.similarity(word1, word2);
	}

}
