package pl.userjeden.firebaseviewer.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import pl.userjeden.firebaseviewer.model.StationReading;
import pl.userjeden.firebaseviewer.model.StationReadingDTO;

public class StationReadingsMapper {

	public StationReading map(StationReadingDTO value) {
		StationReading mapped = new StationReading();
		Map<Long, Map<String, String>> sourceReading = value.getReading();
		Map<String, Map<String, String>> targetReading = new HashMap<String, Map<String, String>>();
		for (Long key : sourceReading.keySet()) {
			LocalDateTime time = Instant.ofEpochMilli(key*1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
			String timeString = time.format((DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			targetReading.put(timeString, copyMap(sourceReading.get(key)));
		}
		mapped.setReadingData(targetReading);
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
