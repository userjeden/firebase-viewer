package pl.userjeden.firebaseviewer.mapper;

import java.util.HashMap;
import java.util.Map;
import pl.userjeden.firebaseviewer.model.StationReading;
import pl.userjeden.firebaseviewer.model.StationReadingDTO;

public class StationReadingsMapper {

	public StationReading map(StationReadingDTO value) {
		StationReading mapped = new StationReading();
		Map<Long, Map<String, String>> sourceReading = value.getReading();
		Map<Long, Map<String, String>> targetReading = new HashMap<Long, Map<String, String>>();
		for (Long key : sourceReading.keySet()) {
			targetReading.put(key, copyMap(sourceReading.get(key)));
		}
		mapped.setReading(targetReading);
		return mapped;
	}

	private Map<String, String> copyMap(Map<String, String> input) {
		Map<String, String> copied = new HashMap<String, String>();
		for (String key : input.keySet()) {
			copied.put(key, input.get(key));
		}
		return copied;
	}

}
