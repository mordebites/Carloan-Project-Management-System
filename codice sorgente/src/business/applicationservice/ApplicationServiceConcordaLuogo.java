package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Agenzia;
import business.entity.Contratto;
import business.transfer.Parameter;

/**
 * Application service per concordare il luogo di restituzione dell'auto
 * presso una agenzia presente nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

@SuppressWarnings("unchecked")
public class ApplicationServiceConcordaLuogo implements ApplicationService {
	private DAOCRUD<Contratto> daoContrattoAperto = (DAOCRUD<Contratto>) DAOFactory.getDAOEntity("DAOContrattoAperto");
	private DAOCRUD<Agenzia> daoAgenzia = (DAOCRUD<Agenzia>) DAOFactory.getDAOEntity("DAOAgenzia");
	private ApplicationServiceAutoDisp asad = new ApplicationServiceAutoDisp();
	
	static Logger log = Logger.getLogger(ApplicationServiceConcordaLuogo.class.getName());
	
	/**
	 * Metodo per concordare un luogo di restituizione dell'auto presso una agenzia
	 * gia' esistente nel sistema di persistenza dei dati.
	 * 
	 * @param par transfer object parameter contente le informazioni sul contratto e l'agenzia
	 */
	public void concordaLuogo(Parameter par) throws Exception {
		Contratto contratto = null;
		try{
			contratto = (Contratto) par.getValue("Contratto");
			par.setValue("id", par.getValue("agenzia"));
		} catch(ChiaveNonDisponibileException e){
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		Agenzia nuovoLuogoRest = daoAgenzia.read(par);
		contratto.setLuogoRest(nuovoLuogoRest);
		
		daoContrattoAperto.update(contratto);
		
		if(contratto.getAuto() != null) {
			asad.cambiaLuogoRest(contratto.getAuto(), contratto.getLuogoRest(), nuovoLuogoRest, contratto.getDataLimite());
		}
		
	}
	
/*	public static void main(String... args){
		ApplicationServiceConcordaLuogo ascl = new ApplicationServiceConcordaLuogo();
		Parameter par = new Parameter();
		par.setValue("id", "1");
		par.setValue("idAgenzia", "1");
		ascl.concordaLuogo(par);
	}*/
}
