package pl.userjeden.firebaseviewer.mapper;

import java.util.HashMap;
import java.util.Map;

import pl.userjeden.firebaseviewer.model.StationSettings;
import pl.userjeden.firebaseviewer.model.StationSettingsDTO;

public class StationSettingsMapper {

	public static StationSettings map(StationSettingsDTO value){
		StationSettings mapped = new StationSettings();
		mapped.setRequestInterval(value.getRequestInterval()/1000);
		mapped.setSyncronInterval(value.getSyncronInterval()/1000);
		mapped.setStationActive(value.getStationActive()>0? true : false);
		return mapped;
	}

	public static Map<String, StationSettings> map(Map<String, StationSettingsDTO> value){
		Map<String, StationSettings> mapped = new HashMap<String, StationSettings>();
		for(String station : value.keySet()){
			mapped.put(station, map(value.get(station)));
		}
		System.out.println("mapper keys: " + mapped.keySet().toString());
		System.out.println("mapper values: " + mapped.values().toString());
		return mapped;
	}


}
