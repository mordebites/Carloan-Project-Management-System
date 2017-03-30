package presentation.boundary.markup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.SessionManager;
import business.applicationservice.exception.ErroreInputException;
import business.entity.TipoUtente;
import business.transfer.Parameter;

public class SchermataPrincipale {
	@FXML
	private Node root;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Labeled usernameLbl;
	@FXML
	private Labeled passwordLbl;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	
	@FXML
	private Button login;
	@FXML
	private Button registrati;

	@FXML
    public void onRegistrati(ActionEvent event) {
		FrontController.processRequest("MostraSchermataRegistrazione", null);
    }
	
	@FXML
	public void onLogin(ActionEvent event){
		Parameter par = new Parameter();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Autenticazione fallita");
        alert.setHeaderText("L'autenticazione e' fallita");
        alert.setContentText("Prova a rieffettuare il login");
		
		try {
			par.setValue("Username", username.getText());
			par.setValue("Password", password.getText());
			
			if(username.getText().compareTo("") == 0 ||  password.getText().compareTo("") == 0) {
				throw new ErroreInputException();
			}
			
			boolean authenticated = (boolean) FrontController.processRequest("AutenticaUtente", par);
			
			if(authenticated){
				Stage stage = (Stage) login.getScene().getWindow();
				stage.close();
				
				if(SessionManager.getTipoUtente() == TipoUtente.OPERATORE){
					FrontController.processRequest("MostraSchermataOperatore", null);
				} else if (SessionManager.getTipoUtente() == TipoUtente.AMMINISTRATORE){
					FrontController.processRequest("MostraSchermataAmministratore", null);
				}
				stage.show();
			} else {
		        alert.showAndWait();
			}
		} catch (Exception e) {
			alert.showAndWait();
		}
	}
}
