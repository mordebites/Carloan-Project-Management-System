package presentation.boundary.markup;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Agenzia;
import business.entity.Contratto;

public class SchermataConcordaRestituzione extends Schermata implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataConcordaRestituzione.class.getName());
	
	@FXML
	private Labeled id;
	@FXML
	private ComboBox<String> agenzia;
	@FXML
	private Button salva;
	
	@Override
	public void initialize() {}
	
	@SuppressWarnings("unchecked")
	@Override
	public void fill() {
		Contratto cont = null;
		try {
			cont = (Contratto) super.parameter.getValue("Contratto");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		id.setText(cont.getId());
	
		List<Agenzia> agenzie = (List<Agenzia>) FrontController.processRequest("VisualizzaOgniAgenzia", null);
		for(Agenzia a: agenzie){
			agenzia.getItems().add(a.getId() + " - " + a.getNome());
		}
		
	}

	@FXML
	public void onSalva(){
		Alert alert = null;
		String idAgenzia = agenzia.getValue();
		if(idAgenzia != null) {
			super.parameter.setValue("agenzia", idAgenzia.split(" - ")[0]);
		
			FrontController.processRequest("ConcordaLuogo", super.parameter);
			alert = AlertHandler.getAlertMessage(new Contratto(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			Stage stage = (Stage) agenzia.getScene().getWindow();
		    stage.close();
			
		} else {
			alert = AlertHandler.getAlertMessage(new NullPointerException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	

}
