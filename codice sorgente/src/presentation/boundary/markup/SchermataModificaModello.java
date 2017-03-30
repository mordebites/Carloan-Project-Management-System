package presentation.boundary.markup;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Fascia;
import business.entity.Modello;

public class SchermataModificaModello extends SchermataImmissioneModello implements SchermataModifica {

	static Logger log = Logger.getLogger(SchermataModificaModello.class.getName());
	
	@Override
	public void initialize() {
		super.useCase = "ModificaModello";
		super.initialize();
	}

	@FXML
	public void fill(){
		try {
			Modello modelloPar = (Modello) super.parameter.getValue("Modello");
			id.setText(modelloPar.getId());
			nome.setText(modelloPar.getNome());
			Fascia tempFascia = modelloPar.getFascia();
			fascia.setValue(tempFascia.getId());
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}
