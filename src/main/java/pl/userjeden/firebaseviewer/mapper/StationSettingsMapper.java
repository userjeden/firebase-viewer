package pl.userjeden.firebaseviewer.mapper;

import java.util.HashMap;
import java.util.Map;

import pl.userjeden.firebaseviewer.model.StationReading;
import pl.userjeden.firebaseviewer.model.StationReadingDTO;
import pl.userjeden.firebaseviewer.model.StationSetting;
import pl.userjeden.firebaseviewer.model.StationSettingDTO;

public class StationSettingsMapper {



	public StationSetting map(StationSettingDTO value){
		StationSetting mapped = new StationSetting();
		mapped.setRequestInterval(value.getRequestInterval()/1000);
		mapped.setSyncronInterval(value.getSyncronInterval()/1000);
		mapped.setStationActive(value.getStationActive()>0? true : false);
		return mapped;
	}

	public StationSettingDTO map(StationSetting value){
		StationSettingDTO mapped = new StationSettingDTO();
		mapped.setRequestInterval(value.getRequestInterval()*1000);
		mapped.setSyncronInterval(value.getSyncronInterval()*1000);
		mapped.setStationActive(value.isStationActive()==true? 1 : 0);
		return mapped;
	}

	public Map<String, StationSetting> mapToEntity(Map<String, StationSettingDTO> value){
		Map<String, StationSetting> mapped = new HashMap<String, StationSetting>();
		for(String station : value.keySet()){
			mapped.put(station, map(value.get(station)));
		}

		return mapped;
	}

	public Map<String, StationSettingDTO> mapToDTO(Map<String, StationSetting> value){
		Map<String, StationSettingDTO> mapped = new HashMap<String, StationSettingDTO>();
		for(String station : value.keySet()){
			mapped.put(station, map(value.get(station)));
		}

		return mapped;
	}

}
