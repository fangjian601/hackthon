package io.derek.hackathon.characerizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class KeyWordLoader {
	
	public static Set<String> getKeyWordSetFromfile(String filename) {
		Set<String> dict = new HashSet<String>();
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filename));
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("---")) continue;
				String[] curLineSplitor = sCurrentLine.split(", ");
				for (int i = 0; i < curLineSplitor.length; i++) {
					dict.add(curLineSplitor[i].toLowerCase());
				}
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
		
		return dict;
	}

	public static void main(String[] args) {
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("techkw");
		System.out.println(dict.size());
		for (String itrString : dict) {
			System.out.println(itrString);
		}
	}

}
