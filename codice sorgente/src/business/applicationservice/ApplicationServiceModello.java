package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Fascia;
import business.entity.Modello;
import business.transfer.Parameter;

/**
 * Application service per la gestione dei modelli.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di modelli nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ApplicationServiceModello implements ApplicationService, CRUD<Modello>{
	@SuppressWarnings("unchecked")
	private DAOCRUD<Modello> daoModello = (DAOCRUD<Modello>) DAOFactory.getDAOEntity("DAOModello");
	
	static Logger log = Logger.getLogger(ApplicationServiceModello.class.getName());
	
	/**
	 * Crea un nuovo modello prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti il modello.
	 * 
	 * @param par l'oggetto che contiene informazioni sul modello da creare
	 * @throws Exception
	 */
	@Override
	public void create(Parameter par) throws Exception {
		Modello modello = fill(par);
		daoModello.create(modello);
	}

	/**
	 * Restituisce un modello presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sul modello da cercare
	 * @return l'oggetto modello trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovata il modello richiesta.
	 */
	@Override
	public Modello read(Parameter par) {
		return daoModello.read(par);
	}

	/**
	 * Restituisce una lista di tutti modelli presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista dei modelli presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti modelli.
	 */
	@Override
	public List<Modello> readAll() {
		return daoModello.readAll(null);
	}

	/**
	 * Aggiorna le informazioni su un modello specificato come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sul modello da aggiornare
	 * @throws Exception
	 */
	@Override
	public void update(Parameter par) throws Exception {
		try {
			Modello modello = fill(par);
			String id = (String) par.getValue("id");
			
			modello.setId(id);
			daoModello.update(modello);
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}

	/**
	 * Elimina dal sistema di persistenza dei dati le informazioni su un modello
	 * specificato come parametro.
	 * 
	 * @param par l'oggetto contente le informazioni sul modello da eliminare
	 */
	@Override
	public void delete(Parameter par) {
		String id = null;
		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		daoModello.delete(id);		
	}
	
	/*
	 * Costruisce un oggetto di tipo modello, in base ai dati presenti
	 * nel transfer object parameter passato come parametro alla funzione.
	 */
	private Modello fill(Parameter par) throws Exception{
		Modello modello = new Modello();
		
		modello.setNome((String) par.getValue("Nome"));
		modello.setFascia((Fascia) par.getValue("Fascia"));
		
		return modello;
	}

	/*	
	public static void main(String... args) throws Exception {
		ApplicationServiceModello appServ = new ApplicationServiceModello();
		Parameter par = new Parameter();
		
		//scrittura
		Fascia tempFascia = new Fascia();
		tempFascia.setId("8");
		
		par.setValue("Nome", new String("antani"));
		par.setValue("Fascia", tempFascia);		
		appServ.create(par);
		
		
				
	    // lettura
		par.setValue("id", new String("1"));
		Modello mod = appServ.read(par);
		
		
		
		// readall
		for(Modello e : appServ.readAll()) {
			System.out.println(e.getId());
		}
		
		
		
		// delete
		par.setValue("id", new String("1"));
		appServ.delete(par);
		
		
		
		// update
		Fascia tempFascia = new Fascia();
		tempFascia.setId("2");
		
		par.setValue("id", new String("2"));
		par.setValue("Nome", new String("superModelloneextra"));
		par.setValue("Fascia", tempFascia);		
		appServ.update(par);
		
	}*/
}
