package io.derek.hackathon.characerizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class JobProfileCharacterizer {
	
	final String[] strongWords = {"strong", "required", "excellent", "highly", "fluency", "proficiency" ,"expert", "years"};
	final String[] desiredWords = {"desired", "plus", "bonus"};
	
	public Map<String, Double> characterize(InputStream input) {
		Map<String, Double> resultMap = new HashMap<String, Double>();
		
		Reader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		
		// Get the dict from the hardcode file
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("res/techkw");
		
		double weighting;
		String line;
		try {
			Random random = new Random();
			random.setSeed(1000);
			while ((line = br.readLine()) != null) {
				Vector<String> words = tokenizeDoc(line);
				
				// Decide the weighting
				if (hasMatchedWord(words, strongWords)) {
					weighting = 0.75 + 0.2 * random.nextDouble();
				} else if (hasMatchedWord(words, desiredWords)) {
					weighting = 0.15 + 0.2 * random.nextDouble();
				} else {
					weighting = 0.4 + 0.2 * random.nextDouble();
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
		} catch (IOException e) {
			System.err.println("null result map");
			return resultMap;
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
	
	public static void main(String[] args) throws FileNotFoundException {
		String filename = "res/JobDispData/Square";
//		List<String> jobDisp = ArticleLoader.getArticleFromfile(filename);
		FileInputStream fis = new FileInputStream(filename);
		
		JobProfileCharacterizer jobProfileExtractor = new JobProfileCharacterizer();
		Map<String, Double> map = jobProfileExtractor.characterize(fis);
		for (String kw : map.keySet()) {
			System.out.println(kw + ", " + map.get(kw));
		}
	}
}
