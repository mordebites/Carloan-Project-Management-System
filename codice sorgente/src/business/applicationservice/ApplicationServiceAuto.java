package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import utility.SessionManager;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Auto;
import business.entity.Modello;
import business.entity.PeriodoDisp;
import business.transfer.Parameter;

/**
 * Application service per la gestione delle auto.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di auto nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ApplicationServiceAuto implements ApplicationService, CRUD<Auto> {
	@SuppressWarnings("unchecked")
	private DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
	@SuppressWarnings("unchecked")
	private DAOCRUD<PeriodoDisp> daoAutoDisp = (DAOCRUD<PeriodoDisp>) DAOFactory.getDAOEntity("DAOAutoDisp");
	
	static Logger log = Logger.getLogger(ApplicationServiceAuto.class.getName());

	/**
	 * Crea una nuova auto prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti l'auto.
	 * 
	 * @param par l'oggetto che contiene informazioni sull'auto da creare
	 * @throws Exception
	 */
	public void create(Parameter par) throws Exception {
		Auto auto = fill(par);
		auto.setId(daoAuto.create(auto));

		// l'auto appena inserita sara' impostata su disponibile
		PeriodoDisp disp = new PeriodoDisp();
		disp.setAuto(auto);
		disp.setDataInizio(new Date(Calendar.getInstance().getTime().getTime()));
		disp.setDataFine(null);
		disp.setAgenzia(SessionManager.getAgenzia());
		daoAutoDisp.create(disp);
	}

	/**
	 * Restituisce una auto presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sull'auto da cercare
	 * @return l'oggetto auto trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovata l'auto richiesta.
	 */
	public Auto read(Parameter par) { // aggiusta eccezione
		return daoAuto.read(par);
	}

	/**
	 * Restituisce una lista di tutte le auto presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista delle auto presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti auto.
	 */
	public List<Auto> readAll() {
		return daoAuto.readAll(null);
	}

	/**
	 * Aggiorna le informazioni su una auto specificata come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sull'auto da aggiornare
	 * @throws Exception
	 */
	public void update(Parameter par) throws Exception {
		try {
			Auto auto = fill(par);
			String id = (String) par.getValue("id");

			auto.setId(id);
			daoAuto.update(auto);
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
	}

	/**
	 * Elimina dal sistema di persistenza dei dati le informazioni su una auto
	 * specificata come parametro.
	 * 
	 * @param par l'oggetto contente le informazioni sull'auto da eliminare
	 */
	public void delete(Parameter par) {
		String id = null;
		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}

		daoAuto.delete(id);
	}

	/*
	 * Costruisce un oggetto di tipo auto, in base ai dati presenti
	 * nel transfer object parameter passato come parametro alla funzione.
	 */
	private Auto fill(Parameter parameter) throws Exception {
		String targa = (String) parameter.getValue("Targa");
		int chilometraggio = (int) parameter.getValue("Chilometraggio");

		/*List<Object> autoValues = new ArrayList<>();
		autoValues.add(targa);
		autoValues.add(chilometraggio);

		checker.check(autoValues);*/

		Auto auto = new Auto();

		auto.setModello((Modello) parameter.getValue("Modello"));
		auto.setChilometraggio(chilometraggio);
		auto.setTarga(targa);

		return auto;
	}
	
	/*public static void main(String... args) throws Exception {
		ApplicationServiceAuto appServ = new ApplicationServiceAuto();
		Parameter par = new Parameter();
		
		// poiche' per aggiungere nuove auto l'operatore
		// dovra' essere autenticato simulo una autenticazione
		ApplicationServiceAutenticazione appServAuth = new ApplicationServiceAutenticazione();
		Agenzia tempAgenzia = new Agenzia();
		tempAgenzia.setId("1");
		
		par.setValue("id", "1");
		par.setValue("Username", new String("simonemarz"));
		par.setValue("Password", new String("miaPassword"));
		par.setValue("TipoUtente", TipoUtente.OPERATORE);
		par.setValue("Agenzia", tempAgenzia);
		System.out.println("Utente loggato: " + appServAuth.isAuthenticated(par));
		
		
		//scrittura
		Modello tempModello = new Modello();
		tempModello.setId("3");
		
		par.setValue("Targa", new String("tocassa"));
		par.setValue("Chilometraggio", new Integer(56));
		par.setValue("Modello", tempModello);
		appServ.create(par);
		
		
	    // lettura
		par.setValue("id", new String("1"));
		Auto mod = appServ.read(par);
		
		
		
		// readall
		for(Auto e : appServ.readAll()) {
			System.out.println(e.getId());
		}
		
		
		
		// delete
		par.setValue("id", new String("3"));
		appServ.delete(par);
		
		
		
		// update
		Modello tempModello = new Modello();
		tempModello.setId("1");
		
		par.setValue("id", "4");
		par.setValue("Targa", new String("1234567"));
		par.setValue("Chilometraggio", new Integer(12));
		par.setValue("Modello", tempModello);
		appServ.update(par);
		
	}*/

}
