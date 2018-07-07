package pl.userjeden.firebaseviewer.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StationSettings {

	private boolean stationActive;
	private int requestInterval;
	private int syncronInterval;

}
