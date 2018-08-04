package pl.userjeden.firebaseviewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StationSettingDTO {

	@JsonProperty("station_active")
	private int stationActive;

	@JsonProperty("request_interval")
	private int requestInterval;

	@JsonProperty("syncron_interval")
	private int syncronInterval;

}
