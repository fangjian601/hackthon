import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupTest {
	
	public static void main(String[] args) throws IOException {
		String url = "http://en.wikipedia.org/wiki/Java_(programming_language)";
		Document doc = Jsoup.connect(url).get();
	    Elements paragraphs = doc.select(".mw-content-ltr p");
	    
	    for (Element ele : paragraphs ) {
	    	System.out.println(ele.text());
	    }
	}
}
