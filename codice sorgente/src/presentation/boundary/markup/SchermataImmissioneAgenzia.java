package presentation.boundary.markup;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Agenzia;
import business.transfer.Parameter;

public abstract class SchermataImmissioneAgenzia extends Schermata {
	@FXML
	protected Labeled id;
	@FXML
	protected TextField nome;
	@FXML
	protected TextField numTel;
	@FXML
	protected TextField indirizzo;
	@FXML
	protected String useCase;
	@FXML
	protected Button salva;
	
	
	@FXML
	public void onSalva(){
		Checker checker = CheckerFactory.buildInstance(Agenzia.class);
		Alert alert;
		
		try {
			Parameter par = new Parameter();
			String nomePar = nome.getText();
			String numTelPar = numTel.getText();
			String indirizzoPar = indirizzo.getText();
			if(nomePar.compareTo("") == 0 || numTelPar.compareTo("") == 0
					|| indirizzoPar.compareTo("") == 0){
				throw new ErroreInputException();
			} else {
				List<Object> agenziaValues = new ArrayList<>();
				agenziaValues.add(nomePar);
				agenziaValues.add(numTelPar);
				agenziaValues.add(indirizzoPar);
								
				checker.check(agenziaValues);
				
				if(useCase.startsWith("Modifica")){
					par.setValue("id", id.getText());
				}
				par.setValue("Nome", nomePar);
				par.setValue("NumTel", numTelPar);
				par.setValue("Indirizzo", indirizzoPar);

	
				Object result = FrontController.processRequest(useCase, par);
				
				if (result != null && result.getClass() == CampoDuplicatoException.class) {
					throw new CampoDuplicatoException();
				}
					
				alert = AlertHandler.getAlertMessage(new Agenzia(), AlertType.CONFIRMATION);
				alert.showAndWait();
					
				// chiudo la schermata attuale
				Stage stage = (Stage) salva.getScene().getWindow();
				stage.close();
			}		
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	}
}
