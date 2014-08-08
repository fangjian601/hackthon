import org.deeplearning4j.util.SerializationUtils;
import org.deeplearning4j.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Loading word2vec and running similarity
 */
public class LoadWord2Vec {

    private static Logger log = LoggerFactory.getLogger(LoadWord2Vec.class);

    public static void main(String[] args) {
        Word2Vec vec = SerializationUtils.readObject(new File("res/model/model.mod"));
        log.info("Loaded word2vec with " + vec.getWordIndex().size() + " words and wordvectors of " + vec.getSyn0().rows);
        List<String> list = vec.similarWordsInVocabTo("develop", 0.3);
        Collections.sort(list);
        for (String word : list) {
        	double sim = vec.similarity("develop", word);
        	if (sim > 0.1) {
        		System.out.println(word + " " + sim);
        	}
        }
        System.out.println("Similarity of " + vec.similarity("it","has"));
    }
}