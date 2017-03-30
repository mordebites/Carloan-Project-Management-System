package business.applicationservice;

import integration.dao.DAOCR;
import integration.dao.DAOFactory;
import utility.PasswordHash;
import business.applicationservice.factory.ApplicationService;
import business.entity.Agenzia;
import business.entity.TipoUtente;
import business.entity.Utente;
import business.transfer.Parameter;

/**
 * Application service per effettuare la registrazione di un utente all'interno del sistema.
 * poiche' la registrazione non e' strutturata basandoci su dei CRUD
 * creeremo dei metodi specifici per la creazione dell'utente nel db
 * mediante il DAOUtente. Per questo motivo non implementermo alcuna
 * interfaccia CRUD nella firma della classe.
 * 
 * Per la crittazione dei dati dell'utente utilizza l'algoritmo di hashing PBKDF2.
 * 
 * @author Simone Marzulli e Morena De Liddo
 */
public class ApplicationServiceRegistrazione implements ApplicationService {
	
	// DAO Utente per la scrittura dei dati nel db
	@SuppressWarnings("unchecked")
	private DAOCR<Utente> daoUtente = (DAOCR<Utente>) DAOFactory.getDAOEntity("DAOUtente");

	// checker utilizzato per il controllo dei campi utente: username e password
	//private Checker checker = CheckerFactory.buildInstance(Utente.class);

	/*
	 * poiche' la registrazione non e' strutturata basandoci su dei CRUD
	 * creeremo dei metodi specifici per la creazione dell'utente nel db
	 * mediante il DAOUtente. Per questo motivo non implementermo alcuna
	 * interfaccia CRUD nella firma della classe.
	 */

	/**
	 * Metodo per effettuare la registrazione e creazione dell'utente all'interno
	 * del sistema di persistenza dei dati.
	 * 
	 * @param par transfer object che contiene le informazioni per la registrazione dell'utente
	 * @throws Exception
	 */
	public void create(Parameter par) throws Exception {
		Utente nuovoUtente = fill(par);
		daoUtente.create(nuovoUtente);
	}

	/*
	 * metodo fill servira' per controllare alcuni campi che passa parameter
	 * in modo tale da validarli e interrompere l'esecuzione se non rispettano
	 * il formato.
	 */
	private Utente fill(Parameter parameter) throws Exception {
		String username = (String) parameter.getValue("Username");
		String password = (String) parameter.getValue("Password");
		TipoUtente tipo = (TipoUtente) parameter.getValue("TipoUtente");
		Agenzia agenzia = (Agenzia) parameter.getValue("Agenzia");
		
		// creiamo l'oggetto utente che successivamente restituiremo
		
		Utente utente = new Utente();
		
		utente.setUsername(username);
		
		// mi creo un hash della password memorizzandomi
		// iterazioni salt ed hash della password originale
		String hash = PasswordHash.createHash(password);
		
		// memorizzo quindi nel database solo l'hash della password
		utente.setPassword(hash);
		
		utente.setTipo(tipo);
		utente.setAgenzia(agenzia);
		
		return utente;
	}
	
	/*
	public static void main(String[] args) throws Exception {
		ApplicationServiceRegistrazione appServ = new ApplicationServiceRegistrazione();
		Parameter par = new Parameter();
		
		Agenzia tempAgenzia = new Agenzia();
		tempAgenzia.setId("1");
		
		par.setValue("Username", new String("deldildo"));
		par.setValue("Password", new String("CiaoMondo96+"));
		par.setValue("TipoUtente", TipoUtente.AMMINISTRATORE);
		par.setValue("Agenzia", tempAgenzia);
		
		Checker checker = CheckerFactory.buildInstance(Utente.class);
		List<Object> values = new ArrayList<Object>();
		values.add(par.getValue("Username"));
		values.add(par.getValue("Password"));
		
		checker.check(values);
		
		appServ.create(par);
		System.out.println("Utente registrato con successo!");		
	}
	*/
}
