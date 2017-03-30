package business.applicationservice;

import integration.dao.DAOAutoDisp;
import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.exception.ContrattoChiusoException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Contratto;
import business.entity.PeriodoDisp;
import business.transfer.Parameter;

/**
 * Application service per l'assegnazione di un'auto ad un contratto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
@SuppressWarnings("unchecked")
public class ApplicationServiceAssegnaAuto implements ApplicationService {
	
	private DAOCRUD<Contratto> daoContratto = (DAOCRUD<Contratto>) DAOFactory.getDAOEntity("DAOContrattoAperto");
	private ApplicationServiceAutoDisp appServDisp = new ApplicationServiceAutoDisp();
	private DAOCRUD<PeriodoDisp> daoAutoDisp = (DAOCRUD<PeriodoDisp>) DAOFactory.getDAOEntity("DAOAutoDisp");
	
	static Logger log = Logger.getLogger(ApplicationServiceAssegnaAuto.class.getName());
	
	/**
	 * Ritorna tutti i periodi disponibili per le auto presenti nel sistema di persistenza dei dati
	 * in base alle informazioni passate nel parametro.
	 * 
	 * @param par transfer object che contiene le informazioni sui periodi di disponibilita' da selezionare 
	 * @return una lista di periodi di disponibilita' presenti nel sistema di persistenza dei dati.
	 */
	public List<PeriodoDisp> getPeriodi(Parameter par){
		return daoAutoDisp.readAll(par);
	}
	
	/**
	 * Assegna un'auto ad un contratto presente nel sistema di persistenza dei dati.
	 * 
	 * @param par transfer object che contiene informazioni sull'auto da assegnare e il contratto a cui l'auto verra' assegnata
	 * @throws Exception
	 */
	public void assegnaAuto(Parameter par) throws Exception {
		Contratto cont = null;
		try {
			cont = (Contratto) par.getValue("Contratto");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		if (!cont.isContrattoAperto()) {
			throw new ContrattoChiusoException();
		}

		par.setValue("tipoRichiesta", DAOAutoDisp.RICHIESTA_ID);
		PeriodoDisp nuovo = daoAutoDisp.read(par);
		appServDisp.riduciDisp(cont.getDataInizio(), cont.getDataLimite(), nuovo, cont.getLuogoRest());
		
		cont.setAuto(nuovo.getAuto());
		daoContratto.update(cont);
	}
	
	
	/*public static void main(String... args) {
		ApplicationServiceAssegnaAuto ass = new ApplicationServiceAssegnaAuto();
		Parameter par = new Parameter();
		
		par.setValue("idAuto", "1"); // auto
		par.setValue("id", "1"); // contratto 
		ass.assegnaAuto(par);
	}*/
}
