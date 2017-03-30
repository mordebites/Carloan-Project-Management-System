package presentation.boundary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.log4j.Logger;

import utility.AlertHandler;
import utility.ControlloConnessione;
import business.applicationservice.exception.ConnessioneAlDataBaseException;
import business.transfer.Parameter;


public class SchermataPrincipale extends Application implements Boundary {
    private static final String MARKUP_FOLDER = "markup/";
    private static final String FXML_EXTENSION = ".fxml";

    private static final int MIN_HEIGHT = 450;
    private static final int MIN_WIDTH = 400;
    
    static Logger log = Logger.getLogger(SchermataPrincipale.class.getName());
	
	public void start(Stage primaryStage) throws Exception {
		
		// controllo che ci sia una connessione al database e che essa avvenga in maniera
		// corretta altrimenti ritorno un messaggio di errore attraverso un alert
		try {
			@SuppressWarnings("unused")
			ControlloConnessione testConnection = new ControlloConnessione();
		} catch (Exception e) {
			if (e.getClass() == ConnessioneAlDataBaseException.class) {
				Alert alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
				alert.showAndWait();
				
				log.error("La connessione al database non e' stato trovata!");
				throw new ConnessioneAlDataBaseException();
			}
		}
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		Class<?> mainClass = getClass();
		
		String schemePath = MARKUP_FOLDER + mainClass.getSimpleName() + FXML_EXTENSION;
		
		fxmlLoader.setLocation(mainClass.getResource(schemePath));
		Parent root = fxmlLoader.load();
		
		primaryStage.setTitle("CarLoan");
		primaryStage.initStyle(StageStyle.DECORATED);
		
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setMaxWidth(MIN_WIDTH);
		primaryStage.setMaxHeight(MIN_HEIGHT);
	
		double width = Screen.getPrimary().getBounds().getWidth();
		double height = Screen.getPrimary().getBounds().getHeight();
	
		Scene scene = new Scene(root, width, height);
	
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
    public Object showWindow(Parameter parameter) {
        launch();
        return null;
    }

    public static void main(String... args) {
        launch(args);
    }
}