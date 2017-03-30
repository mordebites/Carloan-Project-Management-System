package presentation.boundary.markup;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Auto;
import business.entity.Modello;
import business.transfer.Parameter;

public abstract class SchermataImmissioneAuto extends Schermata {
	
	@FXML
	private Node root;
	
	@FXML
	protected Labeled id;
	@FXML
	protected TextField targa;
	@FXML
	protected TextField chilometraggio;
	@FXML
	protected ComboBox<String> modello;
	@FXML
	protected Button salva;
	
	@FXML
	protected String useCase;
	
	
	@FXML
	public void initialize(){
		@SuppressWarnings("unchecked")
		List<Modello> modelli = (List<Modello>) FrontController.processRequest("VisualizzaOgniModello", null);
		for(Modello m: modelli){
			modello.getItems().add(m.getId() +  " - " + m.getNome());
		}
	}
	
	@FXML
	public void onSalva(){
		Checker checker = CheckerFactory.buildInstance(Auto.class);
		Alert alert;
		
		try {
			Parameter par = new Parameter();
			String targaPar = targa.getText();
			String km = chilometraggio.getText();
			if(km.compareTo("") == 0 || targaPar.compareTo("") == 0){
				throw new ErroreInputException();
			} else {
				int kmPar = Integer.parseInt(km);
			
				List<Object> clienteValues = new ArrayList<>();
				clienteValues.add(targaPar);
				clienteValues.add(kmPar);
				
				checker.check(clienteValues);
				
				if(useCase.startsWith("Modifica")){
					par.setValue("id", id.getText());
				}
				par.setValue("Targa", targaPar);
				par.setValue("Chilometraggio", kmPar);
				
				if(modello.getValue() == null){
					throw new ErroreInputException();
				} else {
					Modello tempModello = new Modello();
					tempModello.setId(modello.getValue().split(" - ")[0]);
					par.setValue("Modello", tempModello);
					
	
					Object result = FrontController.processRequest(useCase, par);
					
					if (result != null && result.getClass() == CampoDuplicatoException.class) {
						throw new CampoDuplicatoException();
					}
					
					alert = AlertHandler.getAlertMessage(new Auto(), AlertType.CONFIRMATION);
					alert.showAndWait();
					
					// chiudo la schermata attuale
					Stage stage = (Stage) salva.getScene().getWindow();
				    stage.close();
				}
			}
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	}
}
