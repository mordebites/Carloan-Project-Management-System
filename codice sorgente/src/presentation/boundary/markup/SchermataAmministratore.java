package presentation.boundary.markup;

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
import business.applicationservice.exception.NessunElementoSelezionatoException;
import business.entity.Agenzia;
import business.entity.Auto;
import business.entity.Entity;
import business.entity.Fascia;
import business.entity.Modello;
import business.transfer.Parameter;

@SuppressWarnings("unchecked")
public class SchermataAmministratore extends Schermata {

	@FXML
	private Node root;
	
	//button auto
	@FXML
	private Button aggiungiAuto;
	@FXML
	private Button modificaAuto;
	@FXML
	private Button eliminaAuto;
	@FXML
	private Button autoManutenzione;
	
	//button fasce
	@FXML
	private Button aggiungiFascia;
	@FXML
	private Button modificaFascia;
	@FXML
	private Button eliminaFascia;
	@FXML
	private Button visualizzaFascia;
	
	//button agenzia
	@FXML
	private Button aggiungiAgenzia;
	@FXML
	private Button modificaAgenzia;
	@FXML
	private Button eliminaAgenzia;
	
	//button agenzia
	@FXML
	private Button aggiungiModello;
	@FXML
	private Button modificaModello;
	@FXML
	private Button eliminaModello;
	
	// pulsanti extra
	@FXML
	private Button logOut;
	@FXML
	private Labeled usernameAmministratore;
	
	// tabella auto
	@FXML
	private TableView<Auto> auto;
	@FXML
	private TableColumn<Auto, String> idAuto;
	@FXML
	private TableColumn<Auto, String> targaAuto;
	@FXML
	private TableColumn<Auto, String> chilometraggioAuto;
	@FXML
	private TableColumn<Auto, String> modelloAuto;
	
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
	
	//agenzie
	@FXML
	private TableView<Agenzia> agenzie;
	@FXML
	private TableColumn<Agenzia, String> idAgenzia;
	@FXML
	private TableColumn<Agenzia, String> nomeAgenzia;
	@FXML
	private TableColumn<Agenzia, String> numeroAgenzia;
	@FXML
	private TableColumn<Agenzia, String> indirizzoAgenzia;
	
	@FXML
	public void onLogOut() {
		// imposto il logged user a null
		SessionManager.setLoggedUser(null);
		
		// chiudo la schermata attuale
		Stage stage = (Stage) logOut.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize() {
		usernameAmministratore.setText(SessionManager.getUsername());
		riempiTabellaAuto();
		riempiTabellaFasce();
		riempiTabellaModelli();
		riempiTabellaAgenzie();
	}
	
	@FXML
	public void onAggiungiAuto(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiAuto", null);
		riempiTabellaAuto();
	}
	
	@FXML
	public void onAggiungiFascia(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiFascia", null);
		riempiTabellaFasce();
	}
	
	@FXML
	public void onAggiungiAgenzia(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiAgenzia", null);
		riempiTabellaAgenzie();
	}
	
	@FXML
	public void onAggiungiModello(ActionEvent event){
		FrontController.processRequest("MostraSchermataAggiungiModello", null);
		riempiTabellaModelli();
	}
	
	@FXML
	public void onModificaAuto(ActionEvent event){
		Auto tempAuto = auto.getSelectionModel().getSelectedItem();
		onModifica(tempAuto, "Auto", "MostraSchermataModificaAuto");
		riempiTabellaAuto();
	}
	
	@FXML
	public void onModificaFascia(ActionEvent event){
		Fascia tempFascia = fasce.getSelectionModel().getSelectedItem();
		onModifica(tempFascia, "Fascia", "MostraSchermataModificaFascia");
		riempiTabellaFasce();
	}
	
	@FXML
	public void onModificaAgenzia(ActionEvent event){
		Agenzia tempAgenzia = agenzie.getSelectionModel().getSelectedItem();
		onModifica(tempAgenzia, "Agenzia", "MostraSchermataModificaAgenzia");
		riempiTabellaAgenzie();
		
	}
	
	@FXML
	public void onModificaModello(ActionEvent event){
		Modello tempModello = modelli.getSelectionModel().getSelectedItem();
		onModifica(tempModello, "Modello", "MostraSchermataModificaModello");
		riempiTabellaModelli();
	}
	
	@FXML
	public void onEliminaAuto(){
		Auto tempAuto = auto.getSelectionModel().getSelectedItem();
		String id = null;
		if(tempAuto != null){
			id = tempAuto.getId();
		}
		onElimina(tempAuto, id, "RimuoviAuto");
		riempiTabellaAuto();
	}
	
	@FXML
	public void onEliminaFascia(){
		Fascia tempFascia = fasce.getSelectionModel().getSelectedItem();
		String id = null;
		if(tempFascia != null){
			id = tempFascia.getId();
		}
		onElimina(tempFascia, id, "RimuoviFascia");
		riempiTabellaFasce();
	}
	
	@FXML
	public void onEliminaAgenzia(){
		Agenzia tempAgenzia = agenzie.getSelectionModel().getSelectedItem();
		String id = null;
		if(tempAgenzia != null){
			id = tempAgenzia.getId();
		}
		onElimina(tempAgenzia, id, "RimuoviAgenzia");
		riempiTabellaAgenzie();
	}
	
	@FXML
	public void onEliminaModello(){
		Modello tempModello = modelli.getSelectionModel().getSelectedItem();
		String id = null;
		if(tempModello != null){
			id = tempModello.getId();
		}
		onElimina(tempModello, id, "RimuoviModello");
		riempiTabellaModelli();
	}
	
	private void onModifica(Entity en, String parInput, String useCase){
		Parameter par = null;
		
		if(en != null){
			par = new Parameter();
			par.setValue(parInput, en);
			FrontController.processRequest(useCase , par);
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}

	private void onElimina(Entity en, String id, String useCase){
		Alert alert = null;
		Parameter par = null;
		
		if(en != null){
			try {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Conferma eliminazione");
				alert.setHeaderText("Conferma eliminazione");
				alert.setContentText("Confermi l'eliminazione dell'elemento selezionato?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					
				    par = new Parameter();
					par.setValue("id", id);
					FrontController.processRequest(useCase, par);
					
					alert = AlertHandler.getAlertMessage(en, AlertType.CONFIRMATION);
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
	public void onManutenzione(){
		FrontController.processRequest("MostraSchermataManutenzione", null);
	}
	
	
	@FXML
	public void onVisualizzaFascia(){
		Fascia tempFascia = fasce.getSelectionModel().getSelectedItem();
		Parameter par = null;
		if(tempFascia != null){
			par = new Parameter();
			/*par.setValue("id", tempFascia.getId());
			
			tempFascia = (Fascia) FrontController.processRequest("RicercaFascia", par);*/
			par.setValue("Fascia", tempFascia);
			FrontController.processRequest("MostraSchermataVisualizzaFascia", par);
			
		} else {
			Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
			alert.showAndWait();
		}
	}
	
	@FXML
	private void riempiTabellaAuto() {
		//clienti
		List<Auto> autoList = (List<Auto>) FrontController.processRequest("VisualizzaOgniAuto", null);
		ObservableList<Auto> autoData = FXCollections.observableArrayList(autoList);

		auto.setItems(autoData);
				
		idAuto.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		targaAuto.setCellValueFactory(cellData -> cellData.getValue().getTargaProperty());
		chilometraggioAuto.setCellValueFactory(cellData -> cellData.getValue().getChilometraggioProperty());
		modelloAuto.setCellValueFactory(cellData -> cellData.getValue().getModelloProperty());
				 
		auto.getColumns().setAll(idAuto, targaAuto, chilometraggioAuto, modelloAuto);
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
	private void riempiTabellaAgenzie(){
		List<Agenzia> agenzieList = (List<Agenzia>) FrontController.processRequest("VisualizzaOgniAgenzia", null);
		ObservableList<Agenzia> agenziaData = FXCollections.observableArrayList(agenzieList);
		
		agenzie.setItems(agenziaData);
		
		idAgenzia.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		nomeAgenzia.setCellValueFactory(cellData -> cellData.getValue().getNomeProperty());
		numeroAgenzia.setCellValueFactory(cellData -> cellData.getValue().getNumTelProperty());
		indirizzoAgenzia.setCellValueFactory(cellData -> cellData.getValue().getIndirizzoProperty());

		agenzie.getColumns().setAll(idAgenzia, nomeAgenzia, numeroAgenzia, indirizzoAgenzia);
	}
}
