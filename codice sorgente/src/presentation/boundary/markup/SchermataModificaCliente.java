package presentation.boundary.markup;

import javafx.fxml.FXML;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Cliente;

public class SchermataModificaCliente extends SchermataImmissioneCliente implements SchermataModifica {
	
	static Logger log = Logger.getLogger(SchermataModificaCliente.class.getName());
	
	@Override
	@FXML
	public void initialize() {
		super.useCase = "ModificaCliente";
	}
	
	@FXML
	public void fill(){
		try {
			Cliente clientePar = (Cliente) super.parameter.getValue("Cliente");
			id.setText(clientePar.getId());
			nome.setText(clientePar.getNome());
			cognome.setText(clientePar.getCognome());
			numTel.setText(clientePar.getNumTel());
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}
}