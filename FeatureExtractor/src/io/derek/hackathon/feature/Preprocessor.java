package io.derek.hackathon.feature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Preprocessor {

	static final String DICT_PATH = "";
	static Set<String> phrases;
	static MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");

	// load phrases
	static {
		try {
			Set<String> rawSet = KeyWOrd

			phrases = FluentIterable.from(rawSet).filter(new Predicate<String>() {
				// filter the phrases
				public boolean apply(String word) {
					return word.split(" ").length > 1;
				}
			}).toSet();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** retain phrase in rawText */
	public static String transformPhrases(String rawText) {
		String processed = rawText;
		Joiner joiner = Joiner.on('_');
		for (String phrase : phrases) {
			processed = rawText.replaceAll(phrase, joiner.join(phrase.split(" ")));
		}
		return processed;
	}

	/** Remove verb, adj, etc.. */
	public static String removeUncessaryWords(String rawText) {
		String taggedText = tagger.tagString(rawText);
		System.out.println(taggedText);
		Iterable<String> filteredWords = Iterables.filter(
				Splitter.on(' ').trimResults().omitEmptyStrings().split(taggedText),
				Predicates.compose(new Predicate<String>() {
					public boolean apply(String tag) {
						return "NN".equals(tag);
					}
				}, new Function<String, String>() {
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

	public static void main(String[] args) {
		System.out.println(removeUncessaryWords("Java is a good programming language."));
	}

	public String preprocess(String rawText) {
		return removeUncessaryWords(transformPhrases(rawText));
	}
}
