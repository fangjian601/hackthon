package io.derek.hackathon.feature;

import io.derek.hackathon.characerizer.KeyWordLoader;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Preprocessor {

	static final String DICT_PATH = "res/techkw";
	static Set<String> phrases;
	static Set<String> kwSet;
	static MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");

	// load phrases
	static {
		kwSet = KeyWordLoader.getKeyWordSetFromfile(DICT_PATH);

		phrases = FluentIterable.from(kwSet).filter(new Predicate<String>() {
			// filter the phrases
			public boolean apply(String word) {
				return word.split(" ").length > 1;
			}
		}).toSet();
	}

	/** retain phrase in rawText */
	public static String transformPhrases(String rawText) {
		String processed = rawText.toLowerCase();
		Joiner joiner = Joiner.on('_');
		for (String phrase : phrases) {
			processed = processed.replaceAll(phrase, joiner.join(phrase.split(" ")));
		}
		return processed;
	}

	/** Remove verb, adj, etc.. */
	public static String removeUncessaryWords(String rawText) {
		String taggedText = tagger.tagString(rawText);
		Iterable<String> filteredWords = Iterables.filter(
				Splitter.on(' ').trimResults().omitEmptyStrings().split(taggedText),
				Predicates.compose(
					Predicates.or(
							Predicates.equalTo("NN"),
							Predicates.equalTo(".")
					)
					, new Function<String, String>() {
						public String apply(String taggedWord) {
							return getPosTag(taggedWord);
						}
					}));
		return Joiner.on(' ').join(Iterables.transform(filteredWords, new Function<String, String>() {
			public String apply(String taggedWord) {
				return removeTag(taggedWord);
			}
		}));

	}

	private static String getPosTag(String taggedWord) {
		int index = taggedWord.lastIndexOf('_');
		Preconditions.checkArgument(index != -1, "%s not tagged", taggedWord);
		return taggedWord.substring(index + 1);
	}

	private static String removeTag(String taggedWord) {
		int index = taggedWord.lastIndexOf('_');
		Preconditions.checkArgument(index != -1, "%s not tagged", taggedWord);
		return taggedWord.substring(0, index);
	}

	public static String preprocess(String rawText) {
		return removeUncessaryWords(transformPhrases(rawText));
	}

	public static void main(String[] args) {
		System.out.println(preprocess("Java is a good programming language. Machine learning is kind of cool."));
	}

}
