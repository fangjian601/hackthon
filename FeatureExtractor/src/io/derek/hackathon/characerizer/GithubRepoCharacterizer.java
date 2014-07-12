package io.derek.hackathon.characerizer;

import io.derek.hackathon.feature.Preprocessor;
import io.derek.hackathon.feature.WordVectorizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.uima.resource.ResourceInitializationException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

public class GithubRepoCharacterizer {

	final String LANGUAGE_FIELD = "language";
	final String DESC_FIELD = "description";

	static final double SCORE_THRESHOLD = 0.3;
	static Set<String> FEATURE_SET;
	static Set<String> DOMAIN_SET;
	static final String DOMAIN_PATH = "res/domain.txt";

	static boolean retrainModel = false;

	private WordVectorizer vectorizer = new WordVectorizer();

	static {
		// TODO a compare set for words that need to be extended to
		FEATURE_SET = FluentIterable.from(KeyWordLoader.getKeyWordSetFromfile(Preprocessor.DICT_PATH))
				.transform(new Function<String, String>() {
					public String apply(String words) {
						return constructPhrase(words);
					}
				}).toSet();

		DOMAIN_SET = FluentIterable.from(KeyWordLoader.getKeyWordSetFromfile(DOMAIN_PATH))
				.transform(new Function<String, String>() {
					public String apply(String words) {
						return constructPhrase(words);
					}
				}).toSet();
	}

	public GithubRepoCharacterizer() throws ResourceInitializationException, IOException {
		vectorizer.train(retrainModel);
	}

	public Map<String, Double> characterize(Map<String, String> repoInfo) {
		Map<String, Double> featureMap = Maps.newHashMap();
		Map<String, Double> domainMap = Maps.newHashMap();
		// language field
		count(featureMap, repoInfo.get(LANGUAGE_FIELD), 1);

		// description field
		String desc = repoInfo.get(DESC_FIELD);
		extractFeature(featureMap, domainMap, desc);

		// readme field
		String readme = ReadmeLoader.getReadMEFromURL(repoInfo);
		if (readme != null)
			extractFeature(featureMap, domainMap, readme);

		normalize(domainMap);

		for (String key : domainMap.keySet()) {
			if (!featureMap.containsKey(key) && domainMap.get(key) > SCORE_THRESHOLD) {
				featureMap.put(key, domainMap.get(key));
			}
		}
		return featureMap;
	}

	private void normalize(Map<String, Double> domainMap) {
		double maxVal = Double.MIN_VALUE;
		for (double value:domainMap.values()) {
			if (value > maxVal) {
				maxVal = value;
			}
		}
		
		if (maxVal != 0) {
			for (String key : domainMap.keySet()) {
				domainMap.put(key, domainMap.get(key) / maxVal);
			}
		}
	}

	private void extractFeature(Map<String, Double> featureMap, Map<String, Double> domainMap, String text) {
		if (text != null && !"".equals(text)) {
			String preprocessed = Preprocessor.preprocess(text);
			for (String word : preprocessed.split(" ")) {
				if (FEATURE_SET.contains(word) && !word.equals("r")) {
					featureMap.put(decomposePhrase(word), 1.0);
					for (String feature : DOMAIN_SET) {
						double similarity = vectorizer.query(word, feature);
						count(domainMap, decomposePhrase(feature), computeScore(similarity));
					}
				}
			}
		}
	}

	private double computeScore(double similarity) {
		// TODO normalization might be needed
		return similarity;
	}

	private static String constructPhrase(String words) {
		return Joiner.on('_').join(Splitter.on(' ').split(words.toLowerCase()));
	}

	private static String decomposePhrase(String words) {
		// TODO may need to revert back to original case sensitive string
		return Joiner.on(' ').join(Splitter.on('_').split(words.toLowerCase()));
	}

	private void count(Map<String, Double> map, String key, double value) {
		if (!map.containsKey(key)) {
			map.put(key, value);
		} else {
			map.put(key, value + map.get(key));
		}
	}

	public static void main(String[] args) throws ResourceInitializationException, IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("owner", "binarylogic");
		map.put("name", "searchlogic");
		GithubRepoCharacterizer extractor = new GithubRepoCharacterizer();
		for (Entry entry : extractor.characterize(map).entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

}
