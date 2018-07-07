package pl.userjeden.firebaseviewer;

import java.io.IOException;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pl.userjeden.firebaseviewer.client.FirebaseClientImpl;
import pl.userjeden.firebaseviewer.mapper.StationSettingsMapper;
import pl.userjeden.firebaseviewer.model.StationReadings;
import pl.userjeden.firebaseviewer.model.StationSettings;
import pl.userjeden.firebaseviewer.parser.FirebaseJsonParserImpl;


public class Controller {


	private String projectAddress;
	private String projectSecret;

	private FirebaseClientImpl firebaseClient;
	private FirebaseJsonParserImpl jsonParser;
	private static StationSettingsMapper settMapper;
	private static StationSettingsMapper readMapper;

	private String currentStationId;
	private StationSettings currentStationSettings;

	private Map<String, StationSettings> allStationSettings;
	private Map<String, StationReadings> allStationReadings;


	@FXML
	TextField textFieldAddress;

	@FXML
	TextField textFieldSecret;

	@FXML
	ComboBox<String> menuChooseStation;


	public Controller(){
		System.out.println("NEW INSTANCE");
	}


	public void buttonSignIn(ActionEvent event) throws IOException{

		projectAddress = textFieldAddress.getText();
		projectSecret = textFieldSecret.getText();

		firebaseClient = new FirebaseClientImpl(projectAddress, projectSecret);
		String payload = firebaseClient.readStationsConfig();

		jsonParser = new FirebaseJsonParserImpl();
		allStationSettings = settMapper.map(jsonParser.parseSettings(payload));

		if(!allStationSettings.keySet().isEmpty()){
			App.showControlPage();

			for(String s : allStationSettings.keySet()){
				menuChooseStation.getItems().add(s);
			}

		}
	}


	public void menuStationChosen(ActionEvent event){
		currentStationId = menuChooseStation.getSelectionModel().getSelectedItem();
		System.out.println(currentStationId);
	}





	public void buttonTest(ActionEvent event) throws IOException{
		System.out.println(menuChooseStation == null);
		System.out.println(allStationSettings == null);
		System.out.println("mapper keys: " + allStationSettings.keySet().toString());
		System.out.println("mapper values: " + allStationSettings.values().toString());
	}

}


