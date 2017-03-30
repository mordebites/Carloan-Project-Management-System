package presentation.boundary.markup;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Fascia;

public class SchermataVisualizzaFascia extends Schermata implements SchermataModifica {

	static Logger log = Logger.getLogger(SchermataVisualizzaFascia.class.getName());
	
	@FXML
	private Node root;
	
	@FXML
	private Labeled id;
	@FXML
	private Labeled tipo;
	@FXML
	private Labeled descrizione;
	@FXML
	private Labeled costoKm;
	@FXML
	private Labeled costoGL;
	@FXML
	private Labeled costoSL;
	@FXML
	private Labeled costoGI;
	@FXML
	private Labeled costoSI;
	@FXML
	private Button chiudi;
	
	@Override
	public void initialize() {}

	@Override
	public void fill() {
		try {
			Fascia fascia = (Fascia) super.parameter.getValue("Fascia");
			id.setText(fascia.getId());
			tipo.setText(fascia.getTipo());
			descrizione.setText(fascia.getDescrizione());
			costoKm.setText(Float.toString(fascia.getCostoKm()));
			costoGL.setText(Float.toString(fascia.getCostoGiornoLim()));
			costoSL.setText(Float.toString(fascia.getCostoSettimanaLim()));
			costoGI.setText(Float.toString(fascia.getCostoGiornoIllim()));
			costoSI.setText(Float.toString(fascia.getCostoSettimanaIllim()));
			
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}

}
