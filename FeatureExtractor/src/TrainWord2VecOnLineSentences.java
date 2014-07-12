import io.derek.hackathon.feature.Preprocessor;

import java.io.File;

import org.deeplearning4j.text.tokenizerfactory.UimaTokenizerFactory;
import org.deeplearning4j.util.SerializationUtils;
import org.deeplearning4j.word2vec.Word2Vec;
import org.deeplearning4j.word2vec.inputsanitation.InputHomogenization;
import org.deeplearning4j.word2vec.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.word2vec.sentenceiterator.SentenceIterator;
import org.deeplearning4j.word2vec.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.word2vec.tokenizer.TokenizerFactory;

/**
 * Example on training word2vec with
 * 
 * a sentence on each line
 */
public class TrainWord2VecOnLineSentences {

	public static void main(String[] args) throws Exception {
		String path = "res/train";
		SentenceIterator lineIter = new FileSentenceIterator(new File(path));

		// get rid of @mentions
		lineIter.setPreProcessor(new SentencePreProcessor() {
			public String preProcess(String sentence) {
				String base = new InputHomogenization(sentence).transform();
				base = base.replaceAll("@.*", "");
				return Preprocessor.preprocess(base);
			}
		});

		TokenizerFactory tokenizerFactory = new UimaTokenizerFactory();
		Word2Vec vec = new Word2Vec(tokenizerFactory, lineIter, 5);
		vec.train();

		SerializationUtils.saveObject(vec, new File("res/model/model.mod"));

	}

}