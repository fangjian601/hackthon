import java.util.Map;


public interface Extractor {
	/**Key - key word, Value - weighting*/
	public Map<String, Double> characterize(String pureText);	
}
