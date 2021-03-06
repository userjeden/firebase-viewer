package pl.userjeden.firebaseviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import pl.userjeden.firebaseviewer.client.FirebaseClientImpl;
import pl.userjeden.firebaseviewer.mapper.StationReadingsMapper;
import pl.userjeden.firebaseviewer.mapper.StationSettingsMapper;
import pl.userjeden.firebaseviewer.model.Domain;
import pl.userjeden.firebaseviewer.model.StationReading;
import pl.userjeden.firebaseviewer.model.StationSetting;
import pl.userjeden.firebaseviewer.parser.FirebaseJsonParserImpl;

public class Controller {

	private String projectAddress;
	private String projectSecret;

	private FirebaseClientImpl firebaseClient;
	private FirebaseJsonParserImpl jsonParser;

	private StationSettingsMapper settMapper;
	private StationReadingsMapper readMapper;

	private String currentStationId;
	private StationSetting currentStationSettings;

	private Map<String, StationSetting> allStationSettings;
	private Map<String, StationReading> allStationReadings;



	@FXML
	LineChart<String, Number> chartDisplay;

	@FXML
	GridPane gridPane;

	List<CheckBox> checkboxes;

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
	Label labelMeasInterval;

	@FXML
	Label labelSyncInterval;

	@FXML
	ToggleButton buttonToggleOn;

	@FXML
	ToggleButton buttonToggleOff;

	@FXML
	Button buttonSaveAndRefresh;

	@FXML
	Button buttonLoadData;

	@FXML
	Button buttonCleanReadings;

	@FXML
	CheckBox checkboxCleanReadings;

	@FXML
	Button buttonShowDataPreview;

	@FXML
	Button buttonExportToFile;


	private void fillTable(){

	}


	public void onDataPreview(ActionEvent event){

		chartDisplay.getData().clear();
		chartDisplay.setAnimated(false);
		chartDisplay.setCreateSymbols(false);
		chartDisplay.setLegendSide(Side.TOP);

		Map<String, Map<String, String>> stationData = allStationReadings.get(currentStationId).getReadingData();
		Set<Domain> stationDomains = allStationReadings.get(currentStationId).getReadingDomains();
		List<String> timeKeys = allStationReadings.get(currentStationId).sortByTime();


		int factor = timeKeys.size()/80;
		List<String> filteredTimeKeys = new ArrayList<String>();
		int indicator = -1;
		for(String s : timeKeys){
			indicator ++;
			if(indicator % factor == 0){
				filteredTimeKeys.add(s);
			}
		}

		System.out.println(timeKeys.size());
		System.out.println(filteredTimeKeys.size());


		for(CheckBox check : checkboxes){
			String name = check.getText();
			Boolean active = check.isSelected();

			for(Domain domain : stationDomains){
				if(domain.getName().equals(name)){
					domain.setSelected(active);
				}
			}
		}




		for(Domain domain : stationDomains){

			if(domain.isSelected() == false){
				continue;
			}

			String domainName = domain.getName();
			XYChart.Series dataSeries = new XYChart.Series();
			dataSeries.setName(domainName);

			for(String key : filteredTimeKeys){
				Float value = Float.valueOf(stationData.get(key).get(domainName));
				dataSeries.getData().add(new XYChart.Data(key, value));
			}

			chartDisplay.getData().add(dataSeries);
		}


	}


	public Controller() {
		System.out.println("NEW INSTANCE");
		settMapper = new StationSettingsMapper();
		readMapper = new StationReadingsMapper();
		jsonParser = new FirebaseJsonParserImpl();
		checkboxes = new ArrayList<>();
		allStationReadings = new HashMap<String, StationReading>();
		SpinnerValueFactory<Integer> valueFactoryMeas = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 900);
		SpinnerValueFactory<Integer> valueFactorySync = new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 1800);
	}

	public void buttonSignIn(ActionEvent event) throws IOException {
		String address = textFieldAddress.getText();
		String secret = textFieldSecret.getText();
		openManagementBoard(address, secret);
	}

	private void openManagementBoard(String address, String secret) {
		String rawResponse = attemptStationsRead(address, secret);
		if (!rawResponse.contains("error") && rawResponse.length() > 0) {

			projectAddress = address;
			projectSecret = secret;

			App.showStationManagementBoard();
			updateStationsKnowledge(rawResponse);
			disableAllButtons();
		}
	}

	public void onSaveAndRefresh(ActionEvent event) throws IOException {
		String rawResponse = attemptStationsUpdate(projectAddress, projectSecret);
		updateStationsKnowledge(rawResponse);
	}

	private String attemptStationsRead(String address, String secret) {
		firebaseClient = new FirebaseClientImpl(address, secret);
		return firebaseClient.readStationsConfig();
	}

	private String attemptStationsUpdate(String address, String secret) {
		String payload = jsonParser.marshallSettings(settMapper.mapToDTO(allStationSettings));
		firebaseClient = new FirebaseClientImpl(address, secret);
		return firebaseClient.updateStationConfig(payload);
	}

	private void updateStationsKnowledge(String stationReading) {
		allStationSettings = settMapper.mapToEntity(jsonParser.unmarshallSettings(stationReading));
		String rememberChoice = menuChooseStation.getSelectionModel().getSelectedItem();

		menuChooseStation.getItems().clear();
		for (String s : allStationSettings.keySet()) {
			menuChooseStation.getItems().add(s);
		}

		menuChooseStation.getSelectionModel().select(rememberChoice);
		menuStationChosen(null);
	}

	public void menuStationChosen(ActionEvent event) {
		currentStationId = menuChooseStation.getSelectionModel().getSelectedItem();

		if (currentStationId != null) {
			currentStationSettings = allStationSettings.get(currentStationId);
			showStationSettings(null);
		}

		if(allStationReadings.get(currentStationId) == null){
			buttonShowDataPreview.setDisable(true);
		}else{
			buttonShowDataPreview.setDisable(false);
		}
	}

	public void showStationSettings(ActionEvent event) {
		sliderMeasInterval.setValue((double) currentStationSettings.getRequestInterval());
		sliderSyncInterval.setValue((double) currentStationSettings.getSyncronInterval());
		sliderMeasInterval.setDisable(false);
		sliderSyncInterval.setDisable(false);

		labelMeasInterval.setText(Integer.toString(currentStationSettings.getRequestInterval()));
		labelSyncInterval.setText(Integer.toString(currentStationSettings.getSyncronInterval()));

		buttonSaveAndRefresh.setDisable(false);
		buttonLoadData.setDisable(false);
		checkboxCleanReadings.setDisable(false);

		if (currentStationSettings.isStationActive()) {
			buttonToggleOn.setSelected(true);
			buttonToggleOn.setDisable(true);
			buttonToggleOff.setSelected(false);
			buttonToggleOff.setDisable(false);
		} else {
			buttonToggleOn.setSelected(false);
			buttonToggleOn.setDisable(false);
			buttonToggleOff.setSelected(true);
			buttonToggleOff.setDisable(true);
		}
	}

	public void onMeasSliderUpdate(Event event) {
		int value = (int) sliderMeasInterval.getValue();
		currentStationSettings.setRequestInterval(value);
		labelMeasInterval.setText(Integer.toString(value));
		System.out.println(currentStationId + " update: " + value);
	}

	public void onSyncSliderUpdate(Event event) {
		int value = (int) sliderSyncInterval.getValue();
		currentStationSettings.setSyncronInterval(value);
		labelSyncInterval.setText(Integer.toString(value));
		System.out.println(currentStationId + " update: " + value);
	}

	public void onToggleOn(ActionEvent event) {
		if (buttonToggleOff.isSelected()) {
			buttonToggleOn.setSelected(true);
			buttonToggleOn.setDisable(true);
			buttonToggleOff.setSelected(false);
			buttonToggleOff.setDisable(false);
			currentStationSettings.setStationActive(true);
			System.out.println(currentStationId + " ON");
		}
	}

	public void onToggleOff(ActionEvent event) {
		if (buttonToggleOn.isSelected()) {
			buttonToggleOn.setSelected(false);
			buttonToggleOn.setDisable(false);
			buttonToggleOff.setSelected(true);
			buttonToggleOff.setDisable(true);
			currentStationSettings.setStationActive(false);
			System.out.println(currentStationId + " OFF");
		}
	}

	public void onLoadData(ActionEvent event) {
		firebaseClient = new FirebaseClientImpl(projectAddress, projectSecret);
		String payload = firebaseClient.readStationReading(currentStationId);
		StationReading read = readMapper.map(jsonParser.unmarshallReadings(payload));
		allStationReadings.put(currentStationId, read);

		buttonShowDataPreview.setDisable(false);

		read.printMap();
		read.prepareDomains();
		read.printDomains();





		int row = 0;
		int col = 0;
		Set<String> domains = read.filterDomains();

		domains.add("QQQ");
		domains.add("aaaa");
		domains.add("xxx");
		domains.add("vvv");
		domains.add("QQaQ");
		domains.add("aaa");
		domains.add("xxsx");
		domains.add("vvdv");

		for(String s : domains){

			if(col%3 == 0){
				RowConstraints con = new RowConstraints(25);
				gridPane.getRowConstraints().add(con);
			}

			CheckBox check = new CheckBox(s);
			gridPane.add(check, col%3, row/3);
			checkboxes.add(check);

			row ++;
			col ++;
		}

	}


	private void disableAllButtons() {
		sliderMeasInterval.setDisable(true);
		sliderSyncInterval.setDisable(true);
		labelMeasInterval.setText("0");
		labelSyncInterval.setText("0");
		buttonToggleOn.setDisable(true);
		buttonToggleOff.setDisable(true);
		buttonSaveAndRefresh.setDisable(true);
		buttonLoadData.setDisable(true);
		buttonCleanReadings.setDisable(true);
		buttonShowDataPreview.setDisable(true);
		buttonExportToFile.setDisable(true);
		checkboxCleanReadings.setDisable(true);
	}

}
