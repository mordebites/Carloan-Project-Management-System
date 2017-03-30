package presentation.boundary.markup;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Fascia;

public class SchermataModificaFascia extends SchermataImmissioneFascia implements SchermataModifica {

	static Logger log = Logger.getLogger(SchermataModificaFascia.class.getName());
	
	@Override
	public void initialize() {
		super.useCase = "ModificaFascia";
	}

	@FXML
	public void fill(){
		try {
			Fascia fasciaPar = (Fascia) super.parameter.getValue("Fascia");
			id.setText(fasciaPar.getId());
			tipo.setText(fasciaPar.getTipo());
			descrizione.setText(fasciaPar.getDescrizione());
			costoKm.setText(Float.toString(fasciaPar.getCostoKm()));
			cgl.setText(Float.toString(fasciaPar.getCostoGiornoLim()));
			csl.setText(Float.toString(fasciaPar.getCostoSettimanaLim()));
			cgi.setText(Float.toString(fasciaPar.getCostoGiornoIllim()));
			csi.setText(Float.toString(fasciaPar.getCostoSettimanaIllim()));
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}
