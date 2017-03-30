package business.applicationservice;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.SessionManager;
import utility.TimeUtility;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.exception.ContrattoChiusoException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Agenzia;
import business.entity.Auto;
import business.entity.Base;
import business.entity.Chilometraggio;
import business.entity.Cliente;
import business.entity.Contratto;
import business.entity.Fascia;
import business.entity.ModNoleggio;
import business.transfer.Parameter;


/**
 * Application service per la gestione dei contratti.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di contratti nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
@SuppressWarnings("unchecked")
public class ApplicationServiceContratto implements ApplicationService, CRUD<Contratto> {	
	private DAOCRUD<Contratto> daoContrattoAperto = (DAOCRUD<Contratto>) DAOFactory.getDAOEntity("DAOContrattoAperto");
	private DAOCRUD<Contratto> daoContrattoChiuso = (DAOCRUD<Contratto>) DAOFactory.getDAOEntity("DAOContrattoChiuso");
	private DAOCRUD<Fascia> daoFascia = (DAOCRUD<Fascia>) DAOFactory.getDAOEntity("DAOFascia");
	private DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
	private ApplicationServiceAutoDisp asad = new ApplicationServiceAutoDisp();
	
	static Logger log = Logger.getLogger(ApplicationServiceContratto.class.getName());
	
	private static final int KM_GIORNALIERI = 200;
	private static final int GIORNI_SETTIMANA = 7;
	private static final float MULTA_GIORNALIERA = 200;

	/**
	 * Crea un nuovo contratto prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti il contratto.
	 * 
	 * @param par l'oggetto che contiene informazioni sul contratto da creare
	 * @throws Exception
	 */
	@Override
	public void create(Parameter par) throws Exception {
		par.setValue("Agenzia", SessionManager.getAgenzia());
		par.setValue("LuogoRest", SessionManager.getAgenzia());
		Contratto contratto = fill(par);
		
		daoContrattoAperto.create(contratto);
	}

	/**
	 * Restituisce un contratto presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sul contratto da cercare
	 * @return l'oggetto contratto trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovato il contratto richiesto.
	 */
	@Override
	public Contratto read(Parameter par) {
		boolean aperto = false;
		Contratto contratto = null;
		
		try {
			aperto = (boolean) par.getValue("ContrattoAperto");
		} catch (ChiaveNonDisponibileException e) {
			log.error("La chiave non e' disponibile!");
		}
		
		if(aperto){
			contratto = daoContrattoAperto.read(par);
		} else {
			//se il contratto e' chiuso il dao prende 
			//automaticamente anche i dati dal contratto aperto 
			contratto = daoContrattoChiuso.read(par); 
		}

		return contratto;
	}

	/**
	 * Restituisce una lista di tutti i contratti presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista di contratti presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti contratti.
	 */
	@Override
	public List<Contratto> readAll() {
		List<Contratto> temp = daoContrattoAperto.readAll(null);
		List<Contratto> result = new LinkedList<Contratto>();
		Parameter par = new Parameter();
		
		for(Contratto c :  temp){
			if (c.isContrattoAperto()) {
				result.add(c);
			} else {
				par.setValue("id", c.getId()); //controlla che non dia problemi
				Contratto cont = daoContrattoChiuso.read(par);
				result.add(cont);
			}
		}
		return result;
	}

	/**
	 * Aggiorna le informazioni su un contratto specificato come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sul parametro da aggiornare
	 * @throws Exception
	 */
	@Override
	public void update(Parameter par) throws Exception {
		try {
			Contratto contratto = fill(par);
			
			if(!daoContrattoAperto.read(par).isContrattoAperto()){ //il contratto e' gia' stato chiuso
				throw new ContrattoChiusoException();
			}
			contratto.setId((String) par.getValue("id"));
			daoContrattoAperto.update(contratto);
			if(contratto.getAuto() != null) {
				asad.aggiungiDisp(contratto.getAuto(), contratto.getAgenzia(), 
						contratto.getLuogoRest(), contratto.getDataInizio(), contratto.getDataLimite());
			}
		} catch (ChiaveNonDisponibileException e) {
			log.error("La chiave non e' disponibile!");
		} catch (ContrattoChiusoException e){
			log.error("La chiave non e' disponibile!");
		}
	}

	/**
	 * Elimina dal sistema di persistenza dei dati le informazioni su un contratto
	 * specificata come parametro.
	 * 
	 * @param par l'oggetto contente le informazioni sul contratto da eliminare
	 */
	@Override
	public void delete(Parameter par) { //si possono cancellare solo prima che arrivi la data di inizio
										// poi rimangono nel db
		try {	
			Contratto cont = daoContrattoAperto.read(par);
			daoContrattoAperto.delete((String) par.getValue("id"));
			if(cont.getAuto() != null) {
				asad.aggiungiDisp(cont.getAuto(), cont.getAgenzia(),
						cont.getLuogoRest(), cont.getDataInizio(), cont.getDataLimite());
			}
			
		} catch (ChiaveNonDisponibileException e) {
			log.error("La chiave non e' disponibile!");
		} 
	}
	
	/**
	 * Metodo per la chiusura di un contratto. Consente di chiudere un contratto
	 * gia' esistente nel sistema di persistenza, inserendo alcune informazioni
	 * aggiuntive sul contratto.
	 * 
	 * @param par transfer object contenente informazioni aggiuntive sul contratto da inserire in fase di chiusura
	 * @throws Exception
	 */
	public void close(Parameter par) throws Exception {		
		
		try {
			Contratto tempContratto = (Contratto) par.getValue("Contratto");
			
			Auto auto = tempContratto.getAuto();
			par.setValue("id", auto.getId());
			auto = daoAuto.read(par);
			auto.setChilometraggio(auto.getChilometraggio() + tempContratto.getKmPercorsi());
			daoAuto.update(auto);
			tempContratto.setAuto(auto);
			
			Fascia fascia = tempContratto.getFascia();
			par.setValue("id", fascia.getId());
			fascia = daoFascia.read(par);
			tempContratto.setFascia(fascia);
			
			
			float importoEffettivo = calcoloImportoEffettivo(tempContratto);
			tempContratto.setImportoEffettivo(importoEffettivo);
	        tempContratto.setSaldo(importoEffettivo - tempContratto.getAcconto());
			
			daoContrattoChiuso.create(tempContratto);
			
			//aggiorna il flag in ContrattiAperti
			tempContratto.setContrattoAperto(false);
			daoContrattoAperto.update(tempContratto);
			
			asad.aggiungiDisp(tempContratto.getAuto(), tempContratto.getAgenzia(), 
					tempContratto.getLuogoRest(), tempContratto.getDataInizio(), tempContratto.getDataRientro());
		} catch (ChiaveNonDisponibileException e) {
			log.error("La chiave non e' disponibile!");
		}
	}
	
	
	
	private Contratto fill(Parameter parameter) throws ChiaveNonDisponibileException {
       
        Date dataInizio = (Date) parameter.getValue("DataInizio");
        Date dataLimite = (Date) parameter.getValue("DataLimite");
        float acconto = (float) parameter.getValue("Acconto");
        ModNoleggio mod = (ModNoleggio) parameter.getValue("ModNoleggio");

        Contratto contratto = new Contratto();

        contratto.setDataInizio(dataInizio);
        contratto.setDataLimite(dataLimite);
        contratto.setAcconto(acconto);
        contratto.setModNoleggio(mod);
        contratto.setFascia((Fascia) parameter.getValue("Fascia"));
        contratto.setLuogoRest((Agenzia) parameter.getValue("LuogoRest"));
        contratto.setAgenzia((Agenzia) parameter.getValue("Agenzia"));
        contratto.setCliente((Cliente) parameter.getValue("Cliente"));
        contratto.setAuto((Auto) parameter.getValue("Auto"));
        
        return contratto;
    }

	/*
	 * Metodo per il calcolo dell'importo effettivo dato come parametro in ingresso
	 * il contratto di cui si vuole calcolare l'importo.
	 */
	private float calcoloImportoEffettivo(Contratto cont){
		float importo = 0;
		float tariffaBase = 0;
		
		long distanzaTemporale = TimeUtility.getDistance(cont.getDataInizio(), cont.getDataRientro());
		long giorniMulta = TimeUtility.getDistance(cont.getDataLimite(), cont.getDataRientro());
		Fascia tempFascia = cont.getFascia();
		ModNoleggio tempMod = cont.getModNoleggio();
		int kmPercorsi = cont.getKmPercorsi();
		float costoKm = cont.getFascia().getCostoKm();
		
		long kmPrevisti = distanzaTemporale * KM_GIORNALIERI;
		
		if (tempMod.getBase() == Base.SETTIMANALE) {
			distanzaTemporale /= GIORNI_SETTIMANA;
			
			if(tempMod.getKm() == Chilometraggio.LIMITATO){
				tariffaBase = tempFascia.getCostoSettimanaLim();
			} else if(tempMod.getKm() == Chilometraggio.ILLIMITATO) {
				tariffaBase = tempFascia.getCostoSettimanaIllim();
			}
			
		} else if (tempMod.getBase() == Base.GIORNALIERA) {
			if (tempMod.getKm() == Chilometraggio.LIMITATO){
				tariffaBase = tempFascia.getCostoGiornoLim();
			} else if(tempMod.getKm() == Chilometraggio.ILLIMITATO) {
				tariffaBase = tempFascia.getCostoGiornoIllim();
			}
		}
		
		importo = tariffaBase * distanzaTemporale;
		
		if (tempMod.getKm() == Chilometraggio.LIMITATO) {
			long differenzaKm = kmPercorsi - kmPrevisti;
			if(differenzaKm > 0){
				importo += costoKm * differenzaKm;
			}
		}
		
		if(giorniMulta > 0){
			importo += giorniMulta*MULTA_GIORNALIERA;
		}
		
		return importo;
	}
	
	
	
	/*public static void main(String... args) throws Exception {
		ApplicationServiceContratto appServ = new ApplicationServiceContratto();
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
		Fascia tempFascia = new Fascia();
		tempFascia.setId("7");
		
		Cliente tempCliente = new Cliente();
		tempCliente.setId("1");
		
		ModNoleggio tempMod = new ModNoleggio();
		tempMod.setBase(Base.SETTIMANALE);
		tempMod.setKm(Chilometraggio.ILLIMITATO);
		
		Date dataInizio = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Date dataLimite = dataInizio;
		
		Date dataRientro = dataInizio;
		
		par.setValue("DataInizio", dataInizio);
		par.setValue("DataLimite", dataLimite);
		par.setValue("Acconto", new Float("40.99"));
		par.setValue("ModNoleggio", tempMod);
		par.setValue("Fascia", tempFascia);
		par.setValue("Cliente", tempCliente);
		par.setValue("Auto", null);
		
		appServ.create(par);
		
		
		
		// chiusura contratto

		Date dataRientro = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Auto tempAuto = new Auto();
		tempAuto.setId("5");
		
		par.setValue("KmPercorsi", 3);
		par.setValue("DataRientro", dataRientro);
		par.setValue("ContrattoAperto", true);
		par.setValue("id", "2");
		par.setValue("Auto", tempAuto);
		
		par.setValue("DataInizio", TimeUtility.getDay(dataRientro, -1));
        par.setValue("DataLimite", TimeUtility.getDay(dataRientro, 1));
        par.setValue("Acconto", new Float(100));
        ModNoleggio mod = new ModNoleggio();
        mod.setBase(Base.GIORNALIERA);
        mod.setKm(Chilometraggio.ILLIMITATO);
        par.setValue("ModNoleggio", mod);
        
        Fascia tempFascia = new Fascia();
        tempFascia.setId("8");
        par.setValue("Fascia", tempFascia);
        
        par.setValue("Agenzia", SessionManager.getAgenzia());
        par.setValue("LuogoRest", SessionManager.getAgenzia());
        Cliente tempCliente = new Cliente();
        tempCliente.setId("2");
        par.setValue("Cliente", tempCliente);
		
		appServ.close(par);
		
		
		
	    // lettura contratto aperto
		par.setValue("id", new String("2"));
		par.setValue("contrattoAperto", true);
		Contratto countdooku = appServ.read(par);
		
		
		
		// readall
		for(Contratto e : appServ.readAll()) {
			System.out.println(e.getId());
		}
		
		
		
		
		// delete
		par.setValue("id", new String("2"));
		appServ.delete(par);
		
		
		
		// update
		par.setValue("Acconto", new Float(4.02));
		appServ.update(par);
		
	}*/
}
