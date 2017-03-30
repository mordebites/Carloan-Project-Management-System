package presentation.boundary.markup;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Cliente;
import business.entity.Contratto;
import business.entity.Fascia;
import business.entity.ModNoleggio;

public class SchermataModificaContratto extends SchermataImmissioneContratto implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataModificaContratto.class.getName());
	
	@Override
	@FXML
	public void initialize() {
		super.useCase = "ModificaContratto";
		super.initialize();
	}
	
	@FXML
	public void fill(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			Contratto contrattoPar = (Contratto) super.parameter.getValue("Contratto");
			id.setText(contrattoPar.getId());
			
			Date dataInizioPar = contrattoPar.getDataInizio();
			dataInizio.setValue(LocalDate.parse(dataInizioPar.toString(), formatter));
			
			Date dataLimitePar = contrattoPar.getDataLimite();
			dataLimite.setValue(LocalDate.parse(dataLimitePar.toString(), formatter));
	
			acconto.setText(Float.toString(contrattoPar.getAcconto()));
			
			ModNoleggio mod = contrattoPar.getModNoleggio();
			base.setValue(mod.getBase().toString());
			chilometraggio.setValue(mod.getKm().toString());
			
			Fascia tempFascia = contrattoPar.getFascia();
			fascia.setValue(tempFascia.getId());

			Cliente tempCliente = contrattoPar.getCliente();
			cliente.setValue(tempCliente.getId());
			
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}