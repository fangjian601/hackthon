import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;


public class ArticleLoader {
	
	public static String getArticleFromfile(String filename) {
		StringBuilder article = new StringBuilder();
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filename));
			while ((sCurrentLine = br.readLine()) != null) {
				article.append(sCurrentLine);
				article.append(' ');
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
		
		return article.toString();
	}

	public static void main(String[] args) {
		Set<String> dict = KeyWordLoader.getKeyWordSetFromfile("techkw");
		System.out.println(dict.size());
		for (String itrString : dict) {
			System.out.println(itrString);
		}
	}

}
