package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Fascia;
import business.transfer.Parameter;

/**
 * Application service per la gestione delle fasce.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di fasce nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ApplicationServiceFascia implements ApplicationService, CRUD<Fascia>{
	@SuppressWarnings("unchecked")
	private DAOCRUD<Fascia> daoFascia = (DAOCRUD<Fascia>) DAOFactory.getDAOEntity("DAOFascia");
	
	static Logger log = Logger.getLogger(ApplicationServiceFascia.class.getName());
	
	/**
	 * Crea una nuova fascia prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti la fascia.
	 * 
	 * @param par l'oggetto che contiene informazioni sulla fascia da creare
	 * @throws Exception
	 */
	@Override
	public void create(Parameter par) throws Exception {
		Fascia fascia = fill(par);
		daoFascia.create(fascia);
	}

	/**
	 * Restituisce una fascia presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sulla fascia da cercare
	 * @return l'oggetto fascia trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovata la fascia richiesta.
	 */
	@Override
	public Fascia read(Parameter par) {
		return daoFascia.read(par);
	}

	/**
	 * Restituisce una lista di tutte le fasce presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista delle fasce presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti fasce.
	 */
	@Override
	public List<Fascia> readAll() {
		return daoFascia.readAll(null);
	}

	/**
	 * Aggiorna le informazioni su una fascia specificata come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sulla fascia da aggiornare
	 * @throws Exception
	 */
	@Override
	public void update(Parameter par) throws Exception {
		try {
			Fascia fascia = fill(par);
			String id = (String) par.getValue("id");
			
			fascia.setId(id);
			daoFascia.update(fascia);
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}

	/**
	 * Elimina dal sistema di persistenza dei dati le informazioni su una fascia
	 * specificata come parametro.
	 * 
	 * @param par l'oggetto contente le informazioni sulla fascia da eliminare
	 */
	@Override
	public void delete(Parameter par) {
		String id = null;
		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		daoFascia.delete(id);		
	}


	/*
	 * Costruisce un oggetto di tipo fascia, in base ai dati presenti
	 * nel transfer object parameter passato come parametro alla funzione.
	 */
	private Fascia fill(Parameter par) throws Exception{
		Fascia fascia = new Fascia();
		
		fascia.setTipo((String) par.getValue("Tipo"));
		fascia.setDescrizione((String) par.getValue("Descrizione"));
		fascia.setCostoKm((Float) par.getValue("CostoKm"));
		fascia.setCostoGiornoLim((Float) par.getValue("CostoGiornoLim"));
		fascia.setCostoSettimanaLim((Float) par.getValue("CostoSettimanaLim"));
		fascia.setCostoGiornoIllim((Float) par.getValue("CostoGiornoIllim"));
		fascia.setCostoSettimanaIllim((Float) par.getValue("CostoSettimanaIllim"));
		
		return fascia;
	}
	
	/*public static void main(String... args) throws Exception {
		ApplicationServiceFascia appServ = new ApplicationServiceFascia();
		Parameter par = new Parameter();
		
		
		//scrittura
		par.setValue("Tipo", new String("E"));
		par.setValue("Descrizione", new String("Stukaxian"));
		par.setValue("CostoKm", new Float(0.20));		
		par.setValue("CostoGiornoLim", new Float(75));
		par.setValue("CostoSettimanaLim", new Float(400));
		par.setValue("CostoGiornoIllim", new Float(80));
		par.setValue("CostoSettimanaIllim", new Float(420));
		appServ.create(par);
		
		
	    // lettura
		par.setValue("id", new String("1"));
		Fascia fascia = appServ.read(par);
		
		
		
		// readall
		for(Fascia e : appServ.readAll()) {
			System.out.println(e.getId());
		}
		
		
		
		// delete
		par.setValue("id", new String("1"));
		appServ.delete(par);
		
		
		
		// update
		par.setValue("id", new String("2"));		
		par.setValue("Tipo", new String("A"));
		par.setValue("Descrizione", new String("Berlina, Utilitaria"));
		par.setValue("CostoKm", new Float(666.99));		
		par.setValue("CostoGiorno", new Float(60));
		par.setValue("CostoSettimana", new Float(12));
		appServ.update(par);
		
	}*/
}
