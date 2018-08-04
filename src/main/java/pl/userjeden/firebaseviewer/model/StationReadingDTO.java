package pl.userjeden.firebaseviewer.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationReadingDTO {

	private Map<Long, Map<String, String>> reading;
}
