package presentation.boundary.markup;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Contratto;
import business.entity.Fascia;
import business.transfer.Parameter;

public abstract class SchermataImmissioneModello extends Schermata {
	
	@FXML
	protected Labeled id;
	@FXML
	protected TextField nome;
	@FXML
	protected ComboBox<String> fascia;
	@FXML
	protected Button salva;
	
	@FXML
	protected String useCase;

	@Override
	@FXML
	public void initialize() {
		@SuppressWarnings("unchecked")
		List<Fascia> fasce = (List<Fascia>) FrontController.processRequest("VisualizzaOgniFascia", null);
		for(Fascia f: fasce){
			fascia.getItems().add(f.getId() +  " - " + f.getTipo());
		}
	}
	
	@FXML
	public void onSalva() {
		Alert alert;
		Parameter par = new Parameter();
		
		try {
			String nomePar = nome.getText();
			String fasciaPar = fascia.getValue().split(" - ")[0];
			if(nomePar.compareTo("") == 0 || fasciaPar.compareTo("") == 0){
				throw new ErroreInputException();
			}
				
			if(useCase.startsWith("Modifica")){
				par.setValue("id", id.getText());
			}
			par.setValue("Nome", nomePar);
			Fascia tempFascia = new Fascia();
			tempFascia.setId(fasciaPar);
			par.setValue("Fascia", tempFascia);
			

			Object result = FrontController.processRequest(useCase, par);
			
			if (result != null && result.getClass() == CampoDuplicatoException.class) {
				throw new CampoDuplicatoException();
			}
			
			alert = AlertHandler.getAlertMessage(new Contratto(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			
			Stage stage = (Stage) salva.getScene().getWindow();
		    stage.close();
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
	}

}
