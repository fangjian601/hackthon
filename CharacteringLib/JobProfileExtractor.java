import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class JobProfileExtractor implements Extractor{
	
	@Override
	public Map<String, Double> characterize(String pureText) {
		Map<String, Double> resultMap = new HashMap<String, Double>();
		
		// Get the dict from the hardcode file
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("techkw");
		
		// Preprocessing input text
		Vector<String> words = tokenizeDoc(pureText);
		
		// Get the vector
		for (String word : words) {
			
			//TODO
			//tow gram search
			
			if (dict.contains(word)) {
				resultMap.put(word, (double) 1);
			}
		}
		
		return resultMap;
	}
	
	public static void main(String[] args) {
		String filename = "JobDispData/Square";
		String jobDisp = ArticleLoader.getArticleFromfile(filename);
		JobProfileExtractor jobProfileExtractor = new JobProfileExtractor();
		Map<String, Double> map = jobProfileExtractor.characterize(jobDisp);
		for (String kw : map.keySet()) {
			System.out.println(kw + ", " + map.get(kw));
		}
	}
	
	static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+|_|/");
		Vector<String> tokens = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll("\\W", "");
			if (words[i].length() > 0) {
				tokens.add(words[i]);
			}
		}
		return tokens;
	}
}
