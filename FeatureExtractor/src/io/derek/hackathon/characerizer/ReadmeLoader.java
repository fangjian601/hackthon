package io.derek.hackathon.characerizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class ReadmeLoader {
	
	public static String getText(String url) {
        URL website;
		try {
			website = new URL(url);
	        URLConnection connection = website.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                    connection.getInputStream()));
	        StringBuilder response = new StringBuilder();
	        String inputLine;
	        
	        while ((inputLine = in.readLine()) != null) {
	        	response.append(inputLine);
	        	response.append(' ');
	        }

	        in.close();
	        return response.toString();
	        
		} catch (MalformedURLException e ) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException eFileNotFound) {
			eFileNotFound.printStackTrace();
			return null;
		} catch (IOException eIO) {
			eIO.printStackTrace();
			return null;
		}
		
    }
	
	
	public static String getReadMEFromURL(Map<String, String> map) {
		
		// Get the project URL
		String projectURL = "https://github.com/" + map.get("owner") + "/" + map.get("name");
		
		String content = ReadmeLoader.getText(projectURL);
		if (content == null) return null;
	
		int index1 = content.indexOf("<span class=\"octicon octicon-book\"></span>  ");
		if (index1 == -1) return null;
		String str1 = content.substring(index1 + 48);
		
		int index2 = str1.indexOf("</span>");
		if (index2 == -1) return null;
		String str2 = str1.substring(0, index2);
		
		String filename = str2.trim();

		// Get the raw readme URL
		String readmeURL = "https://raw.githubusercontent.com/" + map.get("owner") + "/" + map.get("name") + "/master/" + filename;
//		System.out.println(filename);
		return ReadmeLoader.getText(readmeURL);
	}
	
	public static void main(String[] args) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("owner", "fqrouter");
		map.put("name", "qiang");
		
		String readme = ReadmeLoader.getReadMEFromURL(map);
		System.out.println(readme);
	}

}
