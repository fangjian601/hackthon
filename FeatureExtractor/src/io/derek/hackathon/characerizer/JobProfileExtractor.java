package io.derek.hackathon.characerizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class JobProfileExtractor implements Extractor{
	
	final String[] strongWords = {"strong", "required", "excellent", "highly", "fluency", "proficiency" ,"expert", "years"};
	final String[] desiredWords = {"desired", "plus", "bonus"};
	
	public Map<String, Double> characterize(List<String> article) {
		Map<String, Double> resultMap = new HashMap<String, Double>();
		
		// Get the dict from the hardcode file
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("res/techkw");
		
		double weighting;
		for (String line : article) {
			Vector<String> words = tokenizeDoc(line);
			
			// Decide the weighting
			if (hasMatchedWord(words, strongWords)) {
				weighting = 0.75;
			} else if (hasMatchedWord(words, desiredWords)) {
				weighting = 0.25;
			} else {
				weighting = 0.5;
			}
			
			// Get the vector
			for (String word : words) {
				if (dict.contains(word.toLowerCase())) {
					if (resultMap.get(word.toLowerCase()) != null) {
						if (resultMap.get(word.toLowerCase()) < weighting) {
							resultMap.put(word.toLowerCase(), weighting);
						}
					} else {
						resultMap.put(word.toLowerCase(), weighting);
					}
				}
			}
			
			// Tow gram search
			for (int i = 0; i < words.size() - 1; i ++) {
				if (dict.contains((words.get(i) + " " + words.get(i + 1)).toLowerCase())) {
					resultMap.put((words.get(i) + " " + words.get(i + 1)).toLowerCase(), weighting);
				}
			}
		}
		
		return resultMap;
	}
	
	static Vector<String> tokenizeDoc(String cur_doc) {
		String[] words = cur_doc.split("\\s+|_|/");
		Vector<String> tokens = new Vector<String>();
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll("\\W", "");
			if (words[i].length() > 0) {
				tokens.add(words[i].toLowerCase());
			}
		}
		if (cur_doc.toLowerCase().contains("c++")) {
			tokens.add("c++");
		}
		return tokens;
	}
	
	static boolean hasMatchedWord(Vector<String> words, String[] src) {
		for (String string : src) {
			if (words.contains(string.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		String filename = "res/JobDispData2/mindgruve";
		List<String> jobDisp = ArticleLoader.getArticleFromfile(filename);
		JobProfileExtractor jobProfileExtractor = new JobProfileExtractor();
		Map<String, Double> map = jobProfileExtractor.characterize(jobDisp);
		for (String kw : map.keySet()) {
			System.out.println(kw + ", " + map.get(kw));
		}
	}

	public Map<String, Double> characterize(String pureText) {
		return null;
	}
}
