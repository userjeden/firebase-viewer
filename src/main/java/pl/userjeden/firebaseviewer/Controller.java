package pl.userjeden.firebaseviewer;

import java.io.IOException;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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

	@FXML
	Slider sliderMeasInterval;

	@FXML
	Slider sliderSyncInterval;

	@FXML
	Spinner<Integer> spinnerMeasInterval;

	@FXML
	Spinner<Integer> spinnerSyncInterval;

	@FXML
	ToggleButton buttonToggleOn;

	@FXML
	ToggleButton buttonToggleOff;




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

			for(String s : allStationSettings.keySet()){
				menuChooseStation.getItems().add(s);
			}

			buttonToggleOn.setDisable(true);
			buttonToggleOff.setDisable(true);

			App.showControlPage();
		}


		SpinnerValueFactory<Integer> valueFactoryMeas = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 900);
		SpinnerValueFactory<Integer> valueFactorySync = new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 1800);
		spinnerMeasInterval.setValueFactory(valueFactoryMeas);
		spinnerSyncInterval.setValueFactory(valueFactorySync);
		spinnerMeasInterval.setDisable(true);
		spinnerSyncInterval.setDisable(true);
	}


	public void menuStationChosen(ActionEvent event){
		currentStationId = menuChooseStation.getSelectionModel().getSelectedItem();
		currentStationSettings = allStationSettings.get(currentStationId);
		System.out.println(currentStationId);
		System.out.println(currentStationSettings.getRequestInterval());

		showStationSettings(null);
	}


	public void showStationSettings(ActionEvent event){
		spinnerMeasInterval.getValueFactory().setValue(currentStationSettings.getRequestInterval());
		spinnerSyncInterval.getValueFactory().setValue(currentStationSettings.getSyncronInterval());
		spinnerMeasInterval.setDisable(false);
		spinnerSyncInterval.setDisable(false);

		sliderMeasInterval.setValue((double) currentStationSettings.getRequestInterval());
		sliderSyncInterval.setValue((double) currentStationSettings.getSyncronInterval());

		if(currentStationSettings.isStationActive()){
			buttonToggleOn.setSelected(true);
			buttonToggleOn.setDisable(true);
			buttonToggleOff.setSelected(false);
			buttonToggleOff.setDisable(false);
		}else{
			buttonToggleOn.setSelected(false);
			buttonToggleOn.setDisable(false);
			buttonToggleOff.setSelected(true);
			buttonToggleOff.setDisable(true);
		}
	}


	public void onMeasSliderUpdate(Event event){
		int value = (int) sliderMeasInterval.getValue();
		currentStationSettings.setRequestInterval(value);
		spinnerMeasInterval.getValueFactory().setValue(value);
		System.out.println("update: " + value);
	}

	public void onSyncSliderUpdate(Event event){
		int value = (int) sliderSyncInterval.getValue();
		currentStationSettings.setSyncronInterval(value);
		spinnerSyncInterval.getValueFactory().setValue(value);
		System.out.println("update: " + value);
	}



	public void onToggleOn(ActionEvent event){
		if(buttonToggleOff.isSelected()){
			buttonToggleOn.setSelected(true);
			buttonToggleOn.setDisable(true);
			buttonToggleOff.setSelected(false);
			buttonToggleOff.setDisable(false);
		}
	}


	public void onToggleOff(ActionEvent event){
		if(buttonToggleOn.isSelected()){
			buttonToggleOn.setSelected(false);
			buttonToggleOn.setDisable(false);
			buttonToggleOff.setSelected(true);
			buttonToggleOff.setDisable(true);
		}
	}





	public void buttonTest(ActionEvent event) throws IOException{
		System.out.println(menuChooseStation == null);
		System.out.println(allStationSettings == null);
		System.out.println("mapper keys: " + allStationSettings.keySet().toString());
		System.out.println("mapper values: " + allStationSettings.values().toString());

		System.out.println(spinnerMeasInterval.getValue());
	}

}


