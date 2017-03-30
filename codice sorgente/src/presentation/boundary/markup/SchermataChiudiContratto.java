package presentation.boundary.markup;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.checker.ContrattoChecker;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Contratto;
import business.transfer.Parameter;

public class SchermataChiudiContratto extends Schermata implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataChiudiContratto.class.getName());
	
	@FXML
	private Labeled id;
	@FXML
	private DatePicker dataRientro;
	@FXML
	private TextField kmPercorsi;
	@FXML
	private Contratto cont;
	
	@FXML
	public void initialize(){}
	
	@Override
	public void fill() {
		Contratto tempContratto = null;
		try {
			tempContratto = (Contratto) super.parameter.getValue("Contratto");
			this.cont = tempContratto;
			
			id.setText(tempContratto.getId());
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		} 
		id.setText(tempContratto.getId());
	}
	
	public void onSalva(){
		Checker checker = CheckerFactory.buildInstance(Contratto.class);
		Parameter par = new Parameter();
		Alert alert = null;
		
		try {
			if(dataRientro.getValue().toString().compareTo("") == 0 
					|| kmPercorsi.getText().compareTo("") == 0){
				throw new ErroreInputException();
			}
			
			List<Object> values = new ArrayList<Object>();
			values.add(ContrattoChecker.CONTRATTO_CHIUSO);
			values.add(Date.valueOf(dataRientro.getValue()));
			values.add(cont.getDataInizio());
			values.add(Integer.parseInt(kmPercorsi.getText()));
			values.add(cont.getDataLimite());
			
			checker.check(values);
			
			cont.setDataRientro(Date.valueOf(dataRientro.getValue()));
			cont.setKmPercorsi(Integer.parseInt(kmPercorsi.getText()));
			
			par.setValue("Contratto", cont);
			FrontController.processRequest("ChiudiContratto", par);
			
			alert = AlertHandler.getAlertMessage(new Contratto(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			Stage stage = (Stage) id.getScene().getWindow();
		    stage.close();
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	}

}
