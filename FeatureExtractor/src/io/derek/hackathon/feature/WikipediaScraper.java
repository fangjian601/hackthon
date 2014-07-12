package io.derek.hackathon.feature;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	public static final String WIKI_LIST_FILE = "res/trainList.txt";
	public static Map<String, String> DICT;
	
	static {
		try {
			DICT = loadUrls(WIKI_LIST_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
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
	public static void scrapeAndSave(String url, File file, boolean rescrape) throws IOException {
		if (rescrape || !file.exists()) {
			PrintWriter writer = new PrintWriter(file);
			for (String sent : scrapeText(url)) {
				writer.println(sent + " ");
			}
			writer.flush();
			writer.close();
		}
	}
	
	public static Map<String, String> loadUrls(String inFile) throws IOException {
		Map<String, String> dict = Maps.newHashMap();
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		String line;
		while ((line = reader.readLine()) != null) {
			String[] arr = line.split("\t");
			dict.put(arr[0].trim(), arr[1].trim());
		}
		
		reader.close();
		return dict;
	}
	
	public static void scrape(boolean rescrape) throws IOException {
		for (Entry<String, String> entry : DICT.entrySet()) {
			String key = entry.getKey();
			String url = entry.getValue();
			scrapeAndSave(url, new File(DIR_NAME, key), rescrape);
			System.out.println("Scraping " + key + " ..");
		}
		System.out.println("done!");
	}
	
	public static void main(String[] args) throws IOException {
		scrape(true);
	}
}
