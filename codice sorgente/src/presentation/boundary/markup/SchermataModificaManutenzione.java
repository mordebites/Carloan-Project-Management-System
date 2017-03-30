package presentation.boundary.markup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Auto;
import business.entity.PeriodoManut;

public class SchermataModificaManutenzione extends SchermataImmissioneManutenzione implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataModificaManutenzione.class.getName());
	
	@Override
	@FXML
	public void initialize() {
		super.useCase = "ModificaAutoManutenzione";
		super.initialize();
	}
	
	@FXML
	public void fill(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			PeriodoManut perMan = (PeriodoManut) super.parameter.getValue("PeriodoManut");
			id.setText(perMan.getId());
			
			Auto autoPar = perMan.getAuto();
			autoId.setText(autoPar.getId());
			
			tipo.setValue(perMan.getTipo().toString());
			
			dataInizio.setValue(LocalDate.parse(perMan.getDataInizio().toString(), formatter));
			dataFine.setValue(LocalDate.parse(perMan.getDataFine().toString(), formatter));
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}
