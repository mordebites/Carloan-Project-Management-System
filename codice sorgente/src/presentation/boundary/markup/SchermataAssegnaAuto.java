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
import business.entity.Contratto;
import business.entity.PeriodoDisp;

public class SchermataAssegnaAuto extends Schermata implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataAssegnaAuto.class.getName());
	
	@FXML
	private Labeled id;
	@FXML
	private ComboBox<String> auto;
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
	
		List<PeriodoDisp> periodi = (List<PeriodoDisp>) FrontController.processRequest("VisualizzaPeriodi", super.parameter);
		for(PeriodoDisp p: periodi){
			auto.getItems().add(p.getId() + " - " + p.getAuto().getModello().getNome());
		}
		
	}

	@FXML
	public void onSalva(){
		Alert alert = null;
		String idPeriodoDisp = auto.getValue();
		if(idPeriodoDisp != null) {
			super.parameter.setValue("id", idPeriodoDisp.split(" - ")[0]);
		
			FrontController.processRequest("AssegnaAuto", super.parameter);
			alert = AlertHandler.getAlertMessage(new Contratto(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			Stage stage = (Stage) auto.getScene().getWindow();
		    stage.close();
			
		} else {
			alert = AlertHandler.getAlertMessage(new NullPointerException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	

}
