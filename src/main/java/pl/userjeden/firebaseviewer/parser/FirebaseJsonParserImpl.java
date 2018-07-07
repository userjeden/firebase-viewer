package pl.userjeden.firebaseviewer.parser;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.userjeden.firebaseviewer.model.StationSettingsDTO;


public class FirebaseJsonParserImpl {


	public Map<String, StationSettingsDTO> parseSettings(String payload){
		Map<String, StationSettingsDTO> preparedMap = new HashMap<String, StationSettingsDTO>();

		byte[] mapData = payload.getBytes();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			preparedMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,StationSettingsDTO>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("station names: " + preparedMap.keySet().toString());
		return preparedMap;
	}


}
