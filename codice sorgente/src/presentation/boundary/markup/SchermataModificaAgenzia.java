package presentation.boundary.markup;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Agenzia;

public class SchermataModificaAgenzia extends SchermataImmissioneAgenzia implements SchermataModifica {

	static Logger log = Logger.getLogger(SchermataModificaAgenzia.class.getName());
	
	@Override
	public void initialize() {
		super.useCase = "ModificaAgenzia";
	}

	@FXML
	public void fill(){
		try {
			Agenzia agenziaPar = (Agenzia) super.parameter.getValue("Agenzia");
			id.setText(agenziaPar.getId());
			nome.setText(agenziaPar.getNome());
			numTel.setText(agenziaPar.getNumTel());
			indirizzo.setText(agenziaPar.getIndirizzo());
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}
