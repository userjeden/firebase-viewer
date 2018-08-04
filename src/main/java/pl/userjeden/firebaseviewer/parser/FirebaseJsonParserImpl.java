package pl.userjeden.firebaseviewer.parser;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.userjeden.firebaseviewer.model.StationReadingDTO;
import pl.userjeden.firebaseviewer.model.StationSettingDTO;


public class FirebaseJsonParserImpl {


	public Map<String, StationSettingDTO> unmarshallSettings(String payload){
		Map<String, StationSettingDTO> preparedMap = new HashMap<String, StationSettingDTO>();

		byte[] mapData = payload.getBytes();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			preparedMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,StationSettingDTO>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (preparedMap != null)? preparedMap : new HashMap<String, StationSettingDTO>() ;
	}


	public String marshallSettings(Map<String, StationSettingDTO> stationSettings){
		String payload = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			payload = objectMapper.writeValueAsString(stationSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return payload;
	}


	public StationReadingDTO unmarshallReadings(String payload){
		StationReadingDTO stationReading = new StationReadingDTO();
		Map<Long, Map<String, String>> preparedMap = new HashMap<Long, Map<String, String>>();

		byte[] mapData = payload.getBytes();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			preparedMap = objectMapper.readValue(mapData, new TypeReference<HashMap<Long, Map<String, String>>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(preparedMap == null){
			preparedMap = new HashMap<Long, Map<String, String>>();
		}

		stationReading.setReading(preparedMap);
		return stationReading;
	}


}
