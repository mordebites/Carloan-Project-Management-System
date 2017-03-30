package presentation.boundary.markup;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import presentation.controller.FrontController;
import utility.AlertHandler;
import business.applicationservice.exception.NessunElementoSelezionatoException;
import business.entity.PeriodoManut;
import business.transfer.Parameter;

public class SchermataManutenzione extends Schermata {
	
	@FXML
	private TableView<PeriodoManut> manut;
	@FXML
	private TableColumn<PeriodoManut, String> id;
	@FXML
	private TableColumn<PeriodoManut, String> auto;
	@FXML
	private TableColumn<PeriodoManut, String> tipoManut;
	@FXML
	private TableColumn<PeriodoManut, String> dataInizio;
	@FXML
	private TableColumn<PeriodoManut, String> dataFine;
	
	@FXML
	private Button aggiungi;
	@FXML
	private Button modifica;
	@FXML
	private Button elimina;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		
		List<PeriodoManut> manutList = (List<PeriodoManut>) FrontController.processRequest("VisualizzaOgniAutoManutenzione", null);
		ObservableList<PeriodoManut> manutData = FXCollections.observableArrayList(manutList);
			
		manut.setItems(manutData);
			
		id.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
		auto.setCellValueFactory(cellData -> cellData.getValue().getAutoProperty());
		tipoManut.setCellValueFactory(cellData -> cellData.getValue().getTipoProperty());
		dataInizio.setCellValueFactory(cellData -> cellData.getValue().getDataInizioProperty());
		dataFine.setCellValueFactory(cellData -> cellData.getValue().getDataFineProperty());

		manut.getColumns().setAll(id, auto, tipoManut, dataInizio, dataFine);
		
	}

	@FXML
	public void onAggiungi(){
		FrontController.processRequest("MostraSchermataAggiungiManutenzione", null);
		initialize();
	}
	
	@FXML
	public void onModifica(){
		PeriodoManut perMan = manut.getSelectionModel().getSelectedItem();
		Parameter par = null;
			
		if(perMan != null){
			par = new Parameter();
			par.setValue("PeriodoManut", perMan);
			FrontController.processRequest("MostraSchermataModificaManutenzione" , par);
				
				initialize();
		} else {
				Alert alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
				alert.showAndWait();
		}
	}
	
	@FXML
	public void onElimina(){
		PeriodoManut perMan = manut.getSelectionModel().getSelectedItem();
		Alert alert = null;
		Parameter par = null;
		
		if(perMan != null){
			try {
				par = new Parameter();
				par.setValue("id", perMan.getId());
				FrontController.processRequest("RimuoviAutoManutenzione", par);
				
				alert = AlertHandler.getAlertMessage(perMan, AlertType.CONFIRMATION);
			    
				initialize();
			} catch (Exception e) {
				alert = AlertHandler.getAlertMessage(e, AlertType.ERROR);
			}
		} else {
			alert = AlertHandler.getAlertMessage(new NessunElementoSelezionatoException(), AlertType.ERROR);
		}
		alert.showAndWait();
	}
}
