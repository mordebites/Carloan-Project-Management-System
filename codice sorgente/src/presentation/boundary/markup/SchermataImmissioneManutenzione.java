package presentation.boundary.markup;

import integration.dao.DAOAutoDisp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.AlertHandler;
import utility.SessionManager;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Auto;
import business.entity.PeriodoDisp;
import business.entity.PeriodoManut;
import business.entity.TipoManut;
import business.transfer.Parameter;

public abstract class SchermataImmissioneManutenzione extends Schermata {

	@FXML
	protected Labeled id;
	@FXML
	protected TextField autoId;
	@FXML
	protected ComboBox<String> tipo;
	@FXML
	protected DatePicker dataInizio;
	@FXML
	protected DatePicker dataFine;
	
	@FXML
	protected Button salva;
	@FXML
	protected String useCase;
	
	@FXML
	public void initialize(){
		tipo.getItems().addAll("ORDINARIA", "STRAORDINARIA");
	}
	
	@FXML
	public void onSalva(){
		Alert alert;
		Parameter par;
		
		try {
			if (autoId.getText().compareTo("") == 0 || tipo.getValue().compareTo("") == 0
					|| dataInizio.getValue().toString().compareTo("") == 0
					|| dataFine.getValue().toString().compareTo("") == 0) {
				
				throw new ErroreInputException();
			}
			par = new Parameter();
			
			if(useCase.startsWith("Inserisci")) {
				Checker checker = CheckerFactory.buildInstance(PeriodoDisp.class);
				List<Object> values = new ArrayList<Object>();
				Auto auto = new Auto();
				auto.setId(autoId.getText());
				values.add(auto);
				values.add(Date.valueOf(dataInizio.getValue()));
				values.add(Date.valueOf(dataFine.getValue()));
				values.add(SessionManager.getAgenzia());
				values.add(DAOAutoDisp.RICHIESTA_MANUTENZIONE);
				
				checker.check(values);
				PeriodoDisp disp = (PeriodoDisp) values.get(5);
				par.setValue("PeriodoDisp", disp);
			}
			
			if(useCase.startsWith("Modifica")){
				par.setValue("id", id.getText());
				
				PeriodoManut per = (PeriodoManut) super.parameter.getValue("PeriodoManut");
				par.setValue("vecchiaDataInizio", per.getDataInizio());
				par.setValue("vecchiaDataFine", per.getDataFine());
			}
			Auto autoPar = new Auto();
			autoPar.setId(autoId.getText());
			par.setValue("Auto", autoPar);
			par.setValue("DataInizio", Date.valueOf(dataInizio.getValue()));
			par.setValue("DataFine", Date.valueOf(dataFine.getValue()));
			par.setValue("TipoManut", TipoManut.valueOf(tipo.getValue()));

			Object result = FrontController.processRequest(useCase, par);
			
			if (result != null && result.getClass() == CampoDuplicatoException.class) {
				throw new CampoDuplicatoException();
			}
			
			alert = AlertHandler.getAlertMessage(new PeriodoManut(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			// chiudo la schermata attuale
			Stage stage = (Stage) salva.getScene().getWindow();
		    stage.close();
		} catch (Exception e){
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	}
}
