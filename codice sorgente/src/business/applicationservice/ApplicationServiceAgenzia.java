package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Agenzia;
import business.transfer.Parameter;

/**
 * Application service per la gestione delle agenzie.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di agenzie nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ApplicationServiceAgenzia implements ApplicationService, CRUD<Agenzia> {
	
	@SuppressWarnings("unchecked")
	private DAOCRUD<Agenzia> daoAgenzia = (DAOCRUD<Agenzia>) DAOFactory.getDAOEntity("DAOAgenzia");

	static Logger log = Logger.getLogger(ApplicationServiceAgenzia.class.getName());

	/**
	 * Crea una nuova agenzia prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti l'agenzia.
	 * 
	 * @param par l'oggetto che contiene informazioni sull'agenzia da creare
	 * @throws Exception
	 */
	public void create(Parameter par) throws Exception {
		Agenzia agenzia = fill(par);
		daoAgenzia.create(agenzia);
	}

	/**
	 * Restituisce una agenzia presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sull'agenzia da cercare
	 * @return l'oggetto agenzia trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovata l'agenzia richiesta.
	 */
	public Agenzia read(Parameter par) {
		return daoAgenzia.read(par);
	}

	/**
	 * Restituisce una lista di tutte le agenzie presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista delle agenzie presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti agenzie.
	 */
	public List<Agenzia> readAll() {
		return daoAgenzia.readAll(null);
	}

	/**
	 * Aggiorna le informazioni su una agenzia specificata come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sull'agenzia da aggiornare
	 * @throws Exception
	 */
	public void update(Parameter par) throws Exception {
		try {
			Agenzia agenzia = fill(par);
			String id = (String) par.getValue("id");
			
			agenzia.setId(id);
			daoAgenzia.update(agenzia);
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}

	/**
	 * Elimina dal sistema di persistenza dei dati le informazioni su una agenzia
	 * specificata come parametro.
	 * 
	 * @param par l'oggetto contente le informazioni sull'agenzia da eliminare
	 */
	public void delete(Parameter par) {
		String id = null;
		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		daoAgenzia.delete(id);
	}

	/*
	 * Costruisce un oggetto di tipo agenzia, in base ai dati presenti
	 * nel transfer object parameter passato come parametro alla funzione.
	 */
	private Agenzia fill(Parameter parameter) throws Exception {
        String nome = (String) parameter.getValue("Nome");
        String numTel = (String) parameter.getValue("NumTel");
        String indirizzo = (String) parameter.getValue("Indirizzo");

        Agenzia agenzia = new Agenzia();

        agenzia.setNome(nome);
        agenzia.setNumTel(numTel);
        agenzia.setIndirizzo(indirizzo);
        
        return agenzia;
    }
	
	/*
	public static void main(String... args) throws Exception {
		
		ApplicationServiceAgenzia appServ = new ApplicationServiceAgenzia();
		
		Parameter par = new Parameter();
		
		par.setValue("Nome", "Pdp SPA");
		par.setValue("NumTel", "0502365656");
		par.setValue("Indirizzo", "Paperopoli");
		
		appServ.create(par);
	}*/
}
