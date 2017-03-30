package presentation.boundary.markup;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Auto;
import business.entity.Modello;

public class SchermataModificaAuto extends SchermataImmissioneAuto implements SchermataModifica {

	static Logger log = Logger.getLogger(SchermataModificaAuto.class.getName());
	
	@Override
	public void initialize() {
		super.useCase = "ModificaAuto";
		super.initialize();
	}

	@FXML
	public void fill(){
		try {
			Auto autoPar = (Auto) super.parameter.getValue("Auto");
			id.setText(autoPar.getId());
			targa.setText(autoPar.getTarga());
			chilometraggio.setText(Integer.toString(autoPar.getChilometraggio()));
			
			Modello tempModello = autoPar.getModello();
			modello.setValue(tempModello.getId());
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}
