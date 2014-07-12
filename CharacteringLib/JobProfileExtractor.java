import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
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
			if (dict.contains(word)) {
				resultMap.put(word, (double) 1);
			}
		}
		
		return resultMap;
	}
	
	public static void main(String[] args) {
		String testString = "Medallia was founded on a simple idea: that companies can win by putting the customer before everything else. Our SaaS platform does this by capturing customer feedback, analyzing it in real-time, and then delivering it to everyone in a company — from the c-suite to the frontline — to help them improve. We’re now considered the leaders in a space we helped to create, we’re Sequoia backed, and we’re growing like crazy, doubling in size every 12 months. We’ve got a culture focused on smarts, kindness, continual learning, irreverence… and our people love it. A full 95% of our employees would recommend us to their friends. Come find out why.Medallia focuses on enabling businesses to understand their customers by gathering feedback data from a variety of sources, applying natural language processing and other analyses, and creating instantaneous, intuitive, and insightful visualizations. Our software engineers work on a wide array of problems, including distributed data storage, topic classification, sentiment analysis, OLAP technology, social media data collection, and more.";
		JobProfileExtractor jobProfileExtractor = new JobProfileExtractor();
		Map<String, Double> map = jobProfileExtractor.characterize(testString);
	}
	
	static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+|_");
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
