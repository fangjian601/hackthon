package io.derek.hackathon.characerizer;
import java.util.List;
import java.util.Map;


public interface Extractor {
	/**Key - key word, Value - weighting*/
	public Map<String, Double> characterize(String pureText);

	public Map<String, Double> characterize(List<String> jobDiscription);	
}
