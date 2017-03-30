package presentation.boundary.markup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import presentation.controller.FrontController;
import utility.AlertHandler;
import utility.SessionManager;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.checker.ContrattoChecker;
import business.applicationservice.exception.AutoNonAssegnabileException;
import business.applicationservice.exception.AutoNonValidaException;
import business.applicationservice.exception.ContrattoChiusoException;
import business.applicationservice.exception.NessunElementoSelezionatoException;
import business.entity.Cliente;
import business.entity.Contratto;
import business.entity.Fascia;
import business.entity.Modello;
import business.transfer.Parameter;

@SuppressWarnings("unchecked")
public class SchermataOperatore extends Schermata {

	@FXML
	private Node root;
	
	@FXML
	private Button aggiungiCliente;
	@FXML
	private Button modificaCliente;
	@FXML
	private Button visualizzaFascia;
	@FXML
	private Button visualizzaContratto;
	@FXML
	private Button aggiungiContratto;
	@FXML
	private Button modificaContratto;
	@FXML
	private Button eliminaContratto;
	@FXML
	private Button chiudiContratto;
	@FXML
	private Button concordaRestituzione;
	@FXML
	private Button assegnaAuto;
	@FXML
	private Button chiudi;
	@FXML
	private Button logOut;
	@FXML
	private Labeled usernameOperatore;
	
	//tabella clienti
	@FXML
	private TableView<Cliente> clienti;
	@FXML
	private TableColumn<Cliente, String> idCliente;
	@FXML
	private TableColumn<Cliente, String> nomeCliente;
	@FXML
	private TableColumn<Cliente, String> cognomeCliente;
	@FXML
	private TableColumn<Cliente, String> telefonoCliente;
	
	//tabella fasce
	@FXML
	private TableView<Fascia> fasce;
	@FXML
	private TableColumn<Fascia, String> idFascia;
	@FXML
	private TableColumn<Fascia, String> tipoFascia;
	@FXML
	private TableColumn<Fascia, String> descrizioneFascia;
	@FXML
	private TableColumn<Fascia, Number> costoKmFascia;
	
	//modelli
	@FXML
	private TableView<Modello> modelli;
	@FXML
	private TableColumn<Modello, String> idModello;
	@FXML
	private TableColumn<Modello, String> nomeModello;
	@FXML
	private TableColumn<Modello, String> fasciaModello;
	
	//contratti
	@FXML
	private TableView<Contratto> contratti;
	@FXML
	private TableColumn<Contratto, String> idContratto;
	@FXML
	private TableColumn<Contratto, String> dataInizioContratto; // in che formato le date?
	@FXML
	private TableColumn<Contratto, String> dataLimiteContratto;
	@FXML
	private TableColumn<Contratto, Number> accontoContratto;
	@FXML
	private TableColumn<Contratto, String> baseContratto;
	@FXML
	private TableColumn<Contratto, String> chilometraggioContratto;
	@FXML
	private TableColumn<Contratto, String> fasciaContratto;
	@FXML
	private TableColumn<Contratto, String> luogoRestContratto;
	@FXML
	private TableColumn<Contratto, String> agenziaContratto;
	@FXML
	private TableColumn<Contratto, String> clienteContratto;
	@FXML
	private TableColumn<Contratto, String> autoContratto;
	@FXML
	private TableColumn<Contratto, String> contrattoAperto;
	
	@FXML
	public void onChiudi(){
		// chiudo la schermata attuale
		Stage stage = (Stage) chiudi.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	public void onLogOut() {
		// imposto il logged user a null
		SessionManager.setLoggedUser(null);
		
		// chiudo la schermata attuale
		Stage stage = (Stage) logOut.getScene().getWindow();
		stage.close();
	}
	
	
	@FXML
	public void initialize(){
		usernameOperatore.setText(SessionManager.getUsername());
		riempiTabellaClienti();
		riempiTabellaFasce();
		riempiTabellaModelli();
		riempiTabellaContratti();	
	}
	
	@FXML
	public void onAggiungiCliente(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiCliente", null);
		// re initialize gli elementi all'interno della tabella aggiungendo il campo appena aggiunto
		riempiTabellaClienti();
	}
	
	@FXML
	public void onModificaCliente(ActionEvent event){
		// prelevo la riga del cliente selezionato
		Cliente tempCliente = clienti.getSelectionModel().getSelectedItem();
		Parameter par = null;
		
		if(tempCliente != null){
			par = new Parameter();
			par.setValue("Cliente", tempCliente);
			FrontController.processRequest("MostraSchermataModificaCliente", par);
			
			riempiTabellaClienti();
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	
	@FXML
	public void onAggiungiContratto(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiContratto", null);
		
		riempiTabellaContratti();
	}
	
	@FXML
	public void onModificaContratto(ActionEvent event){
		// prelevo la riga del cliente selezionato
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Parameter par = null;
		
		if(tempContratto != null){
			par = new Parameter();
			par.setValue("Contratto", tempContratto);
			FrontController.processRequest("MostraSchermataModificaContratto", par);
			
			riempiTabellaContratti();
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	
	@FXML
	public void onEliminaContratto(){
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Checker checker = CheckerFactory.buildInstance(Contratto.class);
		Alert alert = null;
		Parameter par = null;
		
		
		if(tempContratto != null){
			try {
				List<Object> values = new ArrayList<Object>();
				values.add(ContrattoChecker.CONTRATTO_ELIMINANDO);
				values.add(tempContratto.getDataInizio());
				checker.check(values);
				
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Conferma eliminazione");
				alert.setHeaderText("Conferma eliminazione");
				alert.setContentText("Confermi l'eliminazione dell'elemento selezionato?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					par = new Parameter();
					par.setValue("id", tempContratto.getId());
					FrontController.processRequest("RimuoviContratto", par);
					
					alert = AlertHandler.getAlertMessage(tempContratto, AlertType.CONFIRMATION);
					riempiTabellaContratti();
				} else {
					return;
				}

			} catch (Exception e) {
				alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			}
		} else {
			alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
		}
		alert.showAndWait();
	}
	
	@FXML
	public void onVisualizzaFascia(){
		Fascia tempFascia = fasce.getSelectionModel().getSelectedItem();
		Parameter par = null;
		if(tempFascia != null){
			par = new Parameter();
		
			par.setValue("Fascia", tempFascia);
			FrontController.processRequest("MostraSchermataVisualizzaFascia", par);
			
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	
	@FXML
	public void onVisualizzaContratto(){
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Parameter par = null;
		if(tempContratto != null){
			
			par = new Parameter();
			par.setValue("Contratto", tempContratto);
			FrontController.processRequest("MostraSchermataVisualizzaContratto", par);
			
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	
	@FXML
	public void onChiudiContratto(){
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Parameter par = null;
		
		if(tempContratto != null){
			par = new Parameter();
			
			try {
				if (! tempContratto.isContrattoAperto()) {
					throw new ContrattoChiusoException();
				}
				
				if(tempContratto.getAuto() == null){
					throw new AutoNonValidaException();
				}
				
				par.setValue("Contratto", tempContratto);
				FrontController.processRequest("MostraSchermataChiudiContratto", par);
			
				riempiTabellaContratti();
			} catch (Exception e) {
				Alert alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
				alert.showAndWait();
			}
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}	
	}
	
	@FXML
	public void onAssegnaAuto(){
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Parameter par = null;

		if(tempContratto != null){
			if(tempContratto.isContrattoAperto() && tempContratto.getAuto() == null){
				par = new Parameter();
				par.setValue("Contratto", tempContratto);
				FrontController.processRequest("MostraSchermataAssegnaAuto", par);
				
				riempiTabellaContratti();
			} else {
				Alert alert = AlertHandler.getAlertMessage(new AutoNonAssegnabileException(), AlertType.ERROR);
				alert.showAndWait();
			}		
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}	
	}
	
	
	@FXML
	public void onConcordaRestituzione(){
		Contratto tempContratto = contratti.getSelectionModel().getSelectedItem();
		Parameter par = null;
		
		if(tempContratto != null){
			try {
				if (! tempContratto.isContrattoAperto()) {
					throw new ContrattoChiusoException();
				}
				
				if(tempContratto.getAuto() == null){
					throw new AutoNonValidaException();
				}
				
				par = new Parameter();
				par.setValue("Contratto", tempContratto);
				FrontController.processRequest("MostraSchermataConcordaRestituzione", par);
			
				riempiTabellaContratti();
			} catch (Exception e) {
				Alert alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
				alert.showAndWait();
			}
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}	
	}
	@FXML
	private  void riempiTabellaClienti(){
		//clienti
		List<Cliente> clientiList = (List<Cliente>) FrontController.processRequest("VisualizzaOgniCliente", null);
		ObservableList<Cliente> clienteData = FXCollections.observableArrayList(clientiList);

		clienti.setItems(clienteData);
		
		idCliente.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		nomeCliente.setCellValueFactory(cellData -> cellData.getValue().getNomeProperty());
		cognomeCliente.setCellValueFactory(cellData -> cellData.getValue().getCognomeProperty());
		telefonoCliente.setCellValueFactory(cellData -> cellData.getValue().getNumTelProperty());
		 
		clienti.getColumns().setAll(idCliente, nomeCliente, cognomeCliente, telefonoCliente);
	}
	
	@FXML
	private void riempiTabellaFasce(){
		List<Fascia> fasceList = (List<Fascia>) FrontController.processRequest("VisualizzaOgniFascia", null);
		ObservableList<Fascia> fasciaData = FXCollections.observableArrayList(fasceList);

		fasce.setItems(fasciaData);
		
		idFascia.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		tipoFascia.setCellValueFactory(cellData -> cellData.getValue().getTipoProperty());
		descrizioneFascia.setCellValueFactory(cellData -> cellData.getValue().getDescrizioneProperty());
		costoKmFascia.setCellValueFactory(cellData -> cellData.getValue().getCostoKmProperty());
		 
		fasce.getColumns().setAll(idFascia, tipoFascia, descrizioneFascia, costoKmFascia);
	}
	
	
	@FXML
	private void riempiTabellaModelli(){
		//modelli
		List<Modello> modelliList = (List<Modello>) FrontController.processRequest("VisualizzaOgniModello", null);
		ObservableList<Modello> modelloData = FXCollections.observableArrayList(modelliList);

		modelli.setItems(modelloData);
		
		idModello.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		nomeModello.setCellValueFactory(cellData -> cellData.getValue().getNomeProperty());
		fasciaModello.setCellValueFactory(cellData -> cellData.getValue().getFasciaProperty());
		 
		modelli.getColumns().setAll(idModello, nomeModello, fasciaModello);
	}
	
	@FXML
	private void riempiTabellaContratti(){

		//Contratti
		List<Contratto> contrattiList = (List<Contratto>) FrontController.processRequest("VisualizzaOgniContratto", null);
		ObservableList<Contratto> contrattoData = FXCollections.observableArrayList(contrattiList);

		contratti.setItems(contrattoData);
		
		idContratto.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		dataInizioContratto.setCellValueFactory(cellData -> cellData.getValue().getDataInizioProperty());
		dataLimiteContratto.setCellValueFactory(cellData -> cellData.getValue().getDataLimiteProperty());
		accontoContratto.setCellValueFactory(cellData -> cellData.getValue().getAccontoProperty());
		baseContratto.setCellValueFactory(cellData -> cellData.getValue().getBaseProperty());
		chilometraggioContratto.setCellValueFactory(cellData -> cellData.getValue().getChilometraggioProperty());
		fasciaContratto.setCellValueFactory(cellData -> cellData.getValue().getFasciaProperty());
		luogoRestContratto.setCellValueFactory(cellData -> cellData.getValue().getLuogoRestProperty());
		agenziaContratto.setCellValueFactory(cellData -> cellData.getValue().getAgenziaProperty());
		clienteContratto.setCellValueFactory(cellData -> cellData.getValue().getClienteProperty());
		
		autoContratto.setCellValueFactory(cellData -> cellData.getValue().getAutoProperty());
		contrattoAperto.setCellValueFactory(cellData -> cellData.getValue().getApertoProperty());
		
		contratti.getColumns().setAll(idContratto, dataInizioContratto, dataLimiteContratto,
				accontoContratto, baseContratto, chilometraggioContratto, fasciaContratto,
				luogoRestContratto, agenziaContratto, clienteContratto, autoContratto, contrattoAperto);
	}
}
