package presentation.boundary.markup;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
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
import business.entity.Agenzia;
import business.entity.TipoUtente;
import business.entity.Utente;
import business.transfer.Parameter;

public class SchermataRegistrazione extends Schermata {

	@FXML
	private Node root;
	
	@FXML
	private Labeled usernameLbl;
	@FXML
	private Labeled passwordLbl;
	@FXML
	private Labeled tipoUtenteLbl;
	@FXML
	private Labeled agenziaLbl;
	
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private ComboBox<String> tipoUtente;
	@FXML
	private ComboBox<String> agenzia; 
	
	@FXML
	private Button invia;
	 
	
	@FXML
	public void initialize() {
		tipoUtente.getItems().addAll("AMMINISTRATORE", "OPERATORE");
		
		@SuppressWarnings("unchecked")
		List<Agenzia> agenzie = (List<Agenzia>) FrontController.processRequest("VisualizzaOgniAgenzia", null);
		for(Agenzia a: agenzie){
			agenzia.getItems().add(a.getId() +  " - " + a.getNome());
		}
	}
	
	@FXML
	public void onInvia(ActionEvent event){
		Checker checker = CheckerFactory.buildInstance(Utente.class);
		Alert alert;
		
		try {
			Parameter par = new Parameter();
			String user = username.getText();
			String pw = password.getText();
			
			List<Object> utenteValues = new ArrayList<>();
			utenteValues.add(user);
			utenteValues.add(pw);
			
			checker.check(utenteValues);
			
			TipoUtente tipo = TipoUtente.valueOf(tipoUtente.getValue());
			par.setValue("Username", user);
			par.setValue("Password", pw);
			par.setValue("TipoUtente", tipo);
			Agenzia tempAgenzia = new Agenzia();
			tempAgenzia.setId(agenzia.getValue().split(" - ")[0]);
			par.setValue("Agenzia", tempAgenzia);

			Object result = FrontController.processRequest("RegistraUtente", par);
			
			if (result != null && result.getClass() == CampoDuplicatoException.class) {
				throw new CampoDuplicatoException();
			}
			
			alert = AlertHandler.getAlertMessage(new Utente(), AlertType.CONFIRMATION);
			alert.showAndWait();
			
			// chiudo la schermata attuale
			Stage stage = (Stage) invia.getScene().getWindow();
			stage.close();			
		} catch (Exception e) {
			alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			alert.showAndWait();
		}
		
		
	}	
}
