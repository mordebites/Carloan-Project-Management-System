package business.applicationservice;

import integration.dao.DAOCR;
import integration.dao.DAOFactory;
import utility.PasswordHash;
import utility.SessionManager;
import business.applicationservice.factory.ApplicationService;
import business.entity.Utente;
import business.transfer.Parameter;

/**
 * Application service per effettuare l'autenticazione di un utente all'interno del sistema.
 * @author Simone Marzulli e Morena De Liddo
 *
 */

public class ApplicationServiceAutenticazione implements ApplicationService {
	// DAO Utente per la scrittura dei dati nel db
	@SuppressWarnings("unchecked")
	private DAOCR<Utente> daoUtente = (DAOCR<Utente>) DAOFactory.getDAOEntity("DAOUtente");
	
	// checker utilizzato per il controllo dei campi utente: username e password
	//private Checker checker = CheckerFactory.buildInstance(Utente.class);

	/*
	 * poiche' la registrazione non e' strutturata basandoci su dei CRUD
	 * creeremo dei metodi specifici per la lettura degli utenti nel db mediante
	 * il DAOUtente. Per questo motivo non implementermo alcuna interfaccia CRUD
	 * nella firma della classe.
	 */

	/*
	 * il metodo quando invocato passandogli parameter ci dira'
	 * se i valori di parameter coincidono con i valori presenti nel database
	 * in base all'id passato in parameter. 
	 */ 
	
	/**
	 * Il metodo quando invocato passandogli il transfer object parameter ci restituira' un valore boolean
	 * se i valori di parameter coincidono con i valori presenti nel sistema di persistenza dei dati.
	 * 
	 * @param par il transfer object contente i dati per effettuare l'autenticazione
	 * @return un valore boolean che indica se l'autenticazione e' andata a buon fine
	 * @throws Exception
	 */ 
	public boolean isAuthenticated(Parameter par) throws Exception {
		boolean auth = false;

		try {
			// leggo l'utente nel database in base all'id passato
			// dalla classe parameter
			Utente utenteInDB = daoUtente.read(par);
		
			par.setValue("id", utenteInDB.getId());
			
			/*
			discommentare per provare,
			facendo finta che la password sia "miaPassword"
			utenteInDB.setPassword("1000:770d15ff994a4de9d09f30bbce9579336b2520cf099ad5e4:929dd6e13bb9046752bd192b4a9cfbfac693e0cf4362edba");
			utenteInDB.setUsername("simonemarz");
			utenteInDB.setTipo(TipoUtente.OPERATORE);
			*/
			
			// leggo i parametri passati da parameter e costruisco un
			// oggetto di tipo utente
			Utente utenteInParameter = readParameter(par);
			
			// infine controllo se le stringhe degli utenti combaciano, se
			// la password inserita dal parameter coincide con l'hash salvato nel database
			// e se il tipo dell'utente combacia
			if (utenteInDB.getUsername().equals(utenteInParameter.getUsername()) &&
				PasswordHash.validatePassword(utenteInParameter.getPassword(), utenteInDB.getPassword())) {
				auth = true;
				
				SessionManager.setLoggedUser(utenteInDB);
			}
		} catch (IndexOutOfBoundsException e) {
			auth = false;
		}
		
		return auth;		
	}

	// metodo privato che ci servira' per leggere gli attributi di Utente
	// estrapolandoli dal Transfer Object Parameter
	private Utente readParameter(Parameter parameter) throws Exception {
		String id = (String) parameter.getValue("id");
		String username = (String) parameter.getValue("Username");
		String password = (String) parameter.getValue("Password");
		
		// creiamo l'oggetto utente che successivamente restituiremo
		Utente utente = new Utente();

		utente.setId(id);
		utente.setUsername(username);
		utente.setPassword(password);

		return utente;
	}
	
	
	/*
	public static void main(String[] args) throws Exception {
		ApplicationServiceAutenticazione appServ = new ApplicationServiceAutenticazione();
		Parameter par = new Parameter();

		par.setValue("Username", new String(" "));
		par.setValue("Password", new String(" +"));
		
		System.out.println(appServ.isAuthenticated(par));
		
	}
	*/
}
