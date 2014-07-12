import java.util.List;
import java.util.Map;


public interface ProfileExtractor {
	public Map<String, List<FeatureValue>> characterize(String pureText);	
}
