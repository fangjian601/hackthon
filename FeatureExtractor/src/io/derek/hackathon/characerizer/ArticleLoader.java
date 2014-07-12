package io.derek.hackathon.characerizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ArticleLoader {
	
	public static List<String> getArticleFromfile(String filename) {
		List<String> article = new ArrayList<String>();
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filename));
			while ((sCurrentLine = br.readLine()) != null) {
				article.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return article;
	}

	public static void main(String[] args) {
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("res/techkw");
		System.out.println(dict.size());
		for (String itrString : dict) {
			System.out.println(itrString);
		}
	}

}
