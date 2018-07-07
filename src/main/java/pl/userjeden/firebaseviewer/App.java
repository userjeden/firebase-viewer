package pl.userjeden.firebaseviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private static Stage appWindow;
	private static Scene loginScene;
	private static Scene controlScene;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		FXMLLoader loaderOne = new FXMLLoader(getClass().getResource("login_page.fxml"));
		FXMLLoader loaderTwo = new FXMLLoader(getClass().getResource("control_page.fxml"));

		Controller controller = new Controller();
		loaderOne.setController(controller);
		loaderTwo.setController(controller);

		Parent loginPageParent = loaderOne.load();
		Parent controlPagePanel = loaderTwo.load();


		loginScene = new Scene(loginPageParent);
		controlScene = new Scene(controlPagePanel);

		appWindow = stage;
		appWindow.setScene(loginScene);
		appWindow.setTitle("Firebase Viewer");
		appWindow.setHeight(400);
		appWindow.setWidth(800);
		appWindow.show();
	}

	public static void showControlPage() {
		appWindow.setScene(controlScene);
	}

	public static void showLoginPage() {
		appWindow.setScene(loginScene);
	}

}
