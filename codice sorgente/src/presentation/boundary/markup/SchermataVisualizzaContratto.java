package presentation.boundary.markup;

import java.sql.Date;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Labeled;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Auto;
import business.entity.Contratto;

public class SchermataVisualizzaContratto extends Schermata implements SchermataModifica {

    static Logger log = Logger.getLogger(SchermataVisualizzaContratto.class.getName());
	
	@FXML
	private Labeled id;
	@FXML
	private Labeled dataInizio;
	@FXML
	private Labeled dataLimite;
	@FXML
	private Labeled acconto;
	@FXML
	private Labeled base;
	@FXML
	private Labeled chilometraggio;
	@FXML
	private Labeled fascia;
	@FXML
	private Labeled luogoRest;
	@FXML
	private Labeled agenzia;
	@FXML
	private Labeled cliente;
	@FXML
	private Labeled aperto;
	@FXML
	private Labeled auto;
	@FXML
	private Labeled dataRientro;
	@FXML
	private Labeled kmPercorsi;
	@FXML
	private Labeled importoEffettivo;
	@FXML
	private Labeled saldo;
	
	//TODO DA FINIRE
	
	@Override
	public void initialize() {}
	
	@Override
	public void fill() {
		Contratto cont = null;
		try {
			cont = (Contratto) super.parameter.getValue("Contratto");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile");
		}
		
		id.setText(cont.getId());
		dataInizio.setText(cont.getDataInizio().toString());
		dataLimite.setText(cont.getDataLimite().toString());
		acconto.setText(Float.toString(cont.getAcconto()));
		base.setText(cont.getModNoleggio().getBase().toString());
		chilometraggio.setText(cont.getModNoleggio().getKm().toString());
		fascia.setText(cont.getFascia().getId());
		luogoRest.setText(cont.getLuogoRest().getId());
		agenzia.setText(cont.getAgenzia().getId());
		cliente.setText(cont.getCliente().getId());
		
		Auto autoTemp = cont.getAuto();
		if(autoTemp == null){
			auto.setText("ND");
		} else {
			auto.setText(autoTemp.getId());
		}
		
		String apertoString = "No";
		if(cont.isContrattoAperto()) {
			apertoString = "Si'";
		}
		
		aperto.setText(apertoString);
		
		Date dataRientroTemp = cont.getDataRientro();
		String dataRientroString = "ND";
		if (dataRientroTemp != null) {
			dataRientroString = dataRientroTemp.toString();
		}
		dataRientro.setText(dataRientroString);
		
		kmPercorsi.setText(Integer.toString(cont.getKmPercorsi()));
		importoEffettivo.setText(Float.toString(cont.getImportoEffettivo()));
		saldo.setText(Float.toString(cont.getSaldo()));
		
	}
	
	@FXML
	public void onChiudi(){
		this.onAnnulla();
	}
	

}
