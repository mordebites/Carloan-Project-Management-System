package presentation.boundary.markup;

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
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.checker.ContrattoChecker;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ErroreInputException;
import business.entity.Base;
import business.entity.Chilometraggio;
import business.entity.Cliente;
import business.entity.Contratto;
import business.entity.Fascia;
import business.entity.ModNoleggio;
import business.transfer.Parameter;

public class SchermataImmissioneContratto extends Schermata {
	
	@FXML
	protected Labeled id;
	@FXML
	protected DatePicker dataInizio;
	@FXML
	protected DatePicker dataLimite;
	@FXML
	protected TextField acconto;
	@FXML
	protected ComboBox<String> base;
	@FXML
	protected ComboBox<String> chilometraggio;
	@FXML
	protected ComboBox<String> fascia;
	@FXML
	protected ComboBox<String> cliente;

	@FXML
	protected Button salva;
	@FXML
	protected String useCase;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		base.getItems().addAll("GIORNALIERA", "SETTIMANALE");
		chilometraggio.getItems().addAll("LIMITATO", "ILLIMITATO");
		
		List<Fascia> fasce = (List<Fascia>) FrontController.processRequest("VisualizzaOgniFascia", null);
		for(Fascia f: fasce){
			fascia.getItems().add(f.getId() +  " - " + f.getTipo());
		}
		
		List<Cliente> clienti = (List<Cliente>) FrontController.processRequest("VisualizzaOgniCliente", null);
		for(Cliente c: clienti){
			cliente.getItems().add(c.getId() +  " - " + c.getCognome() + " " + c.getNome());
		}	
	}
	
	@FXML
	public void onSalva(){

		Checker checker = CheckerFactory.buildInstance(Contratto.class);
		Alert alert;
		Contratto cont;
		
		try {
			String dataInizioPar = dataInizio.getValue().toString();
			String dataLimitePar = dataLimite.getValue().toString();
			String accontoPar = acconto.getText();
			String basePar = base.getValue();
			String chilometraggioPar = chilometraggio.getValue();
			String fasciaPar = fascia.getValue();
			String clientePar = cliente.getValue();
			
			if(dataInizioPar.compareTo("") == 0 || dataLimitePar.compareTo("") == 0
					|| accontoPar.compareTo("") == 0 || basePar.compareTo("") == 0
					|| chilometraggioPar.compareTo("") == 0 || fasciaPar.compareTo("") == 0
					|| clientePar.compareTo("") == 0){
				
				throw new ErroreInputException();
			}
			
			Parameter par = new Parameter();
					
			List<Object> contrattoValues = new ArrayList<>();
	        contrattoValues.add(ContrattoChecker.CONTRATTO_APERTO);
	        contrattoValues.add(Date.valueOf(dataInizioPar));
	        contrattoValues.add(Date.valueOf(dataLimitePar));
	        contrattoValues.add(Float.parseFloat(accontoPar));
	        contrattoValues.add(Base.valueOf(basePar));

	        checker.check(contrattoValues);
			
	        par.setValue("Auto", null); 
	        
			if(useCase.startsWith("Modifica")){
				par.setValue("id", id.getText());
				cont = (Contratto) super.parameter.getValue("Contratto");
				
				par.setValue("Agenzia", cont.getAgenzia());
				par.setValue("LuogoRest", cont.getLuogoRest());
				par.setValue("Auto", cont.getAuto());
			}
			par.setValue("DataInizio", Date.valueOf(dataInizioPar));
			par.setValue("DataLimite", Date.valueOf(dataLimitePar));
			par.setValue("Acconto", Float.parseFloat(accontoPar));
			
			ModNoleggio mod = new ModNoleggio();
			mod.setBase(Base.valueOf(basePar));
			mod.setKm(Chilometraggio.valueOf(chilometraggio.getValue()));
			par.setValue("ModNoleggio", mod);

			Fascia fasciaTemp = new Fascia();
			fasciaTemp.setId(fascia.getValue().split(" - ")[0]);
			par.setValue("Fascia", fasciaTemp);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getValue().split(" - ")[0]);
			par.setValue("Cliente", clienteTemp);
			
			

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
