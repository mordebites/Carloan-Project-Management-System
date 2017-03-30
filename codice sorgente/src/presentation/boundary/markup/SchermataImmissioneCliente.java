package presentation.boundary.markup;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
import business.entity.Cliente;
import business.transfer.Parameter;

public abstract class SchermataImmissioneCliente extends Schermata {
	@FXML
	protected Node root;
	
	@FXML
	protected Labeled id;
	@FXML
	protected TextField nome;
	@FXML
	protected TextField cognome;
	@FXML
	protected TextField numTel;
	
	@FXML
	protected Button salva;
	@FXML
	protected String useCase;
	
	
	@FXML
	public void onSalva(){
		Checker checker = CheckerFactory.buildInstance(Cliente.class);
		Alert alert;
		
		//CONTROLLA CAMPI VUOTI
		try {
			if (nome.getText().compareTo("") == 0 || cognome.getText().compareTo("") == 0
					|| numTel.getText().compareTo("") == 0){
				
				throw new ErroreInputException();
			}
			
			Parameter par = new Parameter();
			String nomePar = nome.getText();
			String cognomePar = cognome.getText();
			String numPar = numTel.getText();
			
			List<Object> clienteValues = new ArrayList<>();
			clienteValues.add(nomePar);
			clienteValues.add(cognomePar);
			clienteValues.add(numPar);
			
			checker.check(clienteValues);
			
			if(useCase.startsWith("Modifica")){
				par.setValue("id", id.getText());
			}
			par.setValue("Nome", nomePar);
			par.setValue("Cognome", cognomePar);
			par.setValue("NumTel", numPar);

			Object result = FrontController.processRequest(useCase, par);
			
			if (result != null && result.getClass() == CampoDuplicatoException.class) {
				throw new CampoDuplicatoException();
			}
			
			alert = AlertHandler.getAlertMessage(new Cliente(), AlertType.CONFIRMATION);
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
