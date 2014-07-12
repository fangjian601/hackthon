package io.derek.hackathon.feature;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

public class WikipediaScraper {
	
	public static final String DIR_NAME = "res/train";
	
	/** Scrape text from Wikipedia. */
	public static List<String> scrapeText(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements paragraphs = doc.select(".mw-content-ltr p");
		List<String> sentences = FluentIterable.from(paragraphs).transform(new Function<Element, String>() {
			public String apply(Element ele) {
				return ele.text().trim();
			}
		}).filter(Predicates.not(Predicates.equalTo(""))).toList();
		
		return sentences;
	}
	
	/** Scrape and save. */
	public static void scrapeAndSave(String url, String fileName) throws IOException {
		PrintWriter writer = new PrintWriter(new File(fileName));
		for (String sent : scrapeText(url)) {
			writer.println(sent);
		}
		writer.flush();
		writer.close();
	}
	
	public static Map<String, String> loadUrls(String inFile) throws FileNotFoundException {
		Map<String, String> dict = Maps.newHashMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
	}
	
	public static void main(String[] args) throws IOException {
		
	}
}
