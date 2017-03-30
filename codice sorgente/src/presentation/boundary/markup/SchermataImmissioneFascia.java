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
import business.entity.Contratto;
import business.entity.Fascia;
import business.transfer.Parameter;

public abstract class SchermataImmissioneFascia extends Schermata {

	@FXML
	protected Labeled id;
	@FXML
	protected TextField tipo;
	@FXML
	protected TextField descrizione;
	@FXML
	protected TextField costoKm;
	@FXML
	protected TextField cgl;
	@FXML
	protected TextField csl;
	@FXML
	protected TextField cgi;
	@FXML
	protected TextField csi;
	@FXML
	protected Button salva;
	
	@FXML
	protected String useCase;
	

	@FXML
	public void onSalva(){
		Checker checker = CheckerFactory.buildInstance(Fascia.class);
		Alert alert;
		
		try {
			Parameter par = new Parameter();
			String tipoPar = tipo.getText();
			String descrizionePar = descrizione.getText();
			String costoKmPar = costoKm.getText();
			String cglPar = cgl.getText();
			String cslPar = csl.getText();
			String cgiPar = cgi.getText();
			String csiPar = csi.getText();
			
			if(tipoPar.compareTo("") == 0 || descrizionePar.compareTo("") == 0
					|| costoKmPar.compareTo("") == 0 || cglPar.compareTo("") == 0
					|| cslPar.compareTo("") == 0 || cgiPar.compareTo("") == 0
					|| csiPar.compareTo("") == 0) {
				
				throw new ErroreInputException();
			}
					
			List<Object> fasciaValues = new ArrayList<>();
			fasciaValues.add(tipoPar);
			fasciaValues.add(Float.parseFloat(costoKmPar));
			fasciaValues.add(Float.parseFloat(cglPar));
			fasciaValues.add(Float.parseFloat(cslPar));
			fasciaValues.add(Float.parseFloat(cgiPar));
			fasciaValues.add(Float.parseFloat(csiPar));

	        checker.check(fasciaValues);
			
			if(useCase.startsWith("Modifica")){
				par.setValue("id", id.getText());
			}
			
			par.setValue("Tipo", tipoPar);
			par.setValue("Descrizione", descrizionePar);
			par.setValue("CostoKm", Float.parseFloat(costoKmPar));
			par.setValue("CostoGiornoLim", Float.parseFloat(cglPar));
			par.setValue("CostoSettimanaLim", Float.parseFloat(cslPar));
			par.setValue("CostoGiornoIllim", Float.parseFloat(cgiPar));
			par.setValue("CostoSettimanaIllim", Float.parseFloat(csiPar));

			Object result = FrontController.processRequest(useCase, par);
			
			if (result != null && result.getClass() == CampoDuplicatoException.class) {
				throw new CampoDuplicatoException();
			}
			
			alert = AlertHandler.getAlertMessage(new Contratto(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			// chiudo la schermata attuale
			Stage stage = (Stage) salva.getScene().getWindow();
		    stage.close();
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	
	}
}
