package tester;
import integration.connector.Connector;
import integration.connector.MySQLConnector;
import integration.dao.DAOAgenzia;
import integration.dao.DAOFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.entity.Agenzia;
import business.transfer.Parameter;

public class DAOAgenziaTester extends TestCase {
	private final static String SERVER_PATH = "jdbc:mysql://localhost/carloan-test";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "";
	
	static Logger log = Logger.getLogger(DAOAgenziaTester.class.getName());
	
	private DAOAgenzia daoAgenzia;
	private Connector connector;
	
	private Agenzia agenzia;
	private String idIstanzaTestata = null;

	private String[] valoriAttesi = { "Carloan SRL", "0805412359",
			"Via Amendola 184" };

	private boolean deleteTested = false;

	protected void setUp() throws Exception {
		daoAgenzia = (DAOAgenzia) DAOFactory.getDAOEntity("DAOAgenzia");
		connector = new MySQLConnector(SERVER_PATH, USERNAME, PASSWORD);
		daoAgenzia.setConnector(connector);
		
		agenzia = new Agenzia();

		// creo un oggetto agenzia e successivamente lo passo
		// al dao dell'agenzia in modo da scrivere una istanza
		// all'interno del database. Conservo inoltre l'id
		// dell'istanza di prova salvata nel database
		agenzia.setNome(valoriAttesi[0]);
		agenzia.setNumTel(valoriAttesi[1]);
		agenzia.setIndirizzo(valoriAttesi[2]);
		
		idIstanzaTestata = daoAgenzia.create(agenzia);
	}

	protected void tearDown() throws Exception {
		// cancello l'istanza di prova dal database
		// e setto a null l'oggetto agenzia

		// poiche' dovremo testare anche la delete del DAO
		// al momento della teardown se e' stata gia' effettuata una
		// delete allora non cancelliamo nuovamente dal database lo stesso id
		if (!deleteTested) {
			daoAgenzia.delete(idIstanzaTestata);
		} else {
			deleteTested = false;
		}

		idIstanzaTestata = null;
		agenzia = null;
		daoAgenzia = null;

		// resetto dal database l'id autoincrementate al valore piu' alto
		// prima che venisse eseguito il test in modo da
		// lasciare invariato lo stato del database
		resetMySQLCounter();
		
		// distruggiamo il connector dopo aver eseguito
		// la funzione per il reset del counter di MySQL
		connector = null;
	}

	public void testDuplicatesCreate() {
		// per testare i duplicati creiamo una nuova istanza nel database
		// con gli stessi dati presenti nel metodo setup di junit. In questo modo
		// viene sollevata l'eccezione CampoDuplicatoException e controlliamo che
		// l'eccezione sia effettivamente quella
		
		try {
			idIstanzaTestata = daoAgenzia.create(agenzia);
		} catch (Exception e) {
			assertEquals("Eccezione campo duplicato non trovata!", CampoDuplicatoException.class, e.getClass());
		}
	}
	
	public void testDuplicatesUpdate() throws Exception {
		// creo una istanza temporanea di cui controllero' i duplicati
		// in fase di aggiornamento
		Agenzia tempAgenzia = new Agenzia();
		String idIstanzaTemporanea = null;
		
		String[] valoriTemporanei = {"Carloan 2 SRL", "0997754230", "Via Caputo 3"};
		
		tempAgenzia.setNome(valoriTemporanei[0]);
		tempAgenzia.setNumTel(valoriTemporanei[1]);
		tempAgenzia.setIndirizzo(valoriTemporanei[2]);
		
		idIstanzaTemporanea = daoAgenzia.create(tempAgenzia);
		
		// aggiorno i valori dell'agenzia creata nella setup
		// quindi avrò come riferimento la variabile idIstanzaTestata
		
		String[] valoriAttesiModificati = { "Carloan 2 SRL", "3374651023",
		"Via C. Battisti 654" };

		Agenzia agenziaModificata = new Agenzia();
		agenziaModificata.setId(idIstanzaTestata);
		agenziaModificata.setNome(valoriAttesiModificati[0]);
		agenziaModificata.setNumTel(valoriAttesiModificati[1]);
		agenziaModificata.setIndirizzo(valoriAttesiModificati[2]);
		
		try {
			daoAgenzia.update(agenziaModificata);
		} catch (Exception e) {
			assertEquals("Eccezione campo duplicato non trovata!", CampoDuplicatoException.class, e.getClass());
		}
		
		// elimino l'istanza temporanea per questo test
		daoAgenzia.delete(idIstanzaTemporanea);
		resetMySQLCounter();
	}
	
	public void testCreate() {
		assertTrue("Impossibile creare l'istanza nel database, variabile id errata",
				idIstanzaTestata.compareTo("0") != 0);
	}

	public void testRead() {
		// costruisco il parameter passandogli come campo id
		// quello ottenuto dopo la create principale nel metodo setUp
		Parameter par = new Parameter();
		par.setValue("id", idIstanzaTestata);

		Agenzia tempAgenzia = daoAgenzia.read(par);

		assertEquals("nome non coincide con valore atteso", valoriAttesi[0],
				tempAgenzia.getNome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesi[1], tempAgenzia.getNumTel());
		assertEquals("indirizzo non voincide con valore atteso",
				valoriAttesi[2], tempAgenzia.getIndirizzo());
	}

	/*
	 * Per evitare problemi concorrenziali durante il test nella lettura e
	 * scrittura del database, si suppone che il test venga eseguito quando il
	 * sistema non e' operativo. Il test sulla readAll legge l'ultimo valore
	 * inserito durante il test e se il sistema fosse operativo, i valori
	 * risulterebbero errati.
	 */
	public void testReadAll() {
		List<Agenzia> tempAgenzie = daoAgenzia.readAll(null);

		int lastValue = tempAgenzie.size() - 1;

		// controllo gli ultimi valori della lista delle agenzie
		// che corrispondon all'ultimo inserimento fatto

		assertEquals("nome non coincide con valore atteso", valoriAttesi[0],
				tempAgenzie.get(lastValue).getNome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesi[1], tempAgenzie.get(lastValue).getNumTel());
		assertEquals("indirizzo non voincide con valore atteso",
				valoriAttesi[2], tempAgenzie.get(lastValue).getIndirizzo());
	}

	public void testUpdate() throws Exception {
		// creo un nuovo oggetto agenzia che deve sostituire quello attualmente
		// nel database ed aggiorno il database che contiene
		// la tupla fittizia creata nella setUp

		String[] valoriAttesiModificati = { "Mattacchioni SRL", "3374651023",
				"Via C. Battisti 654" };

		Agenzia agenziaModificata = new Agenzia();
		agenziaModificata.setId(idIstanzaTestata);
		agenziaModificata.setNome(valoriAttesiModificati[0]);
		agenziaModificata.setNumTel(valoriAttesiModificati[1]);
		agenziaModificata.setIndirizzo(valoriAttesiModificati[2]);

		daoAgenzia.update(agenziaModificata);

		// dopo l'update rileggo la tupla modificata in base all'id
		// dell'istanza sotto test e controllo i valori se coincidono
		// con quelli modificati

		Parameter par = new Parameter();
		par.setValue("id", idIstanzaTestata);

		Agenzia tempAgenzia = daoAgenzia.read(par);

		assertEquals("nome non coincide con valore atteso",
				valoriAttesiModificati[0], tempAgenzia.getNome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesiModificati[1], tempAgenzia.getNumTel());
		assertEquals("indirizzo non voincide con valore atteso",
				valoriAttesiModificati[2], tempAgenzia.getIndirizzo());
	}

	public void testDelete() {
		// effettuo una cancellazione dell'istanza fittizia
		// e setto la variabile di controllo per la delete nel teardown a true
		// in questo modo non cancello nuovamente nella teardown l'istanza
		// creata nella setup

		daoAgenzia.delete(idIstanzaTestata);
		deleteTested = true;

		List<Agenzia> tempAgenzie = daoAgenzia.readAll(null);

		int lastValue = tempAgenzie.size() - 1;

		// controllo che dopo la delete la tabella non sia vuota
		if (lastValue >= 0) {
			// controllo gli ultimi valori della lista delle agenzie
			// che corrispondon all'ultimo inserimento fatto

			assertNotSame("nome non coincide con valore atteso",
					valoriAttesi[0], tempAgenzie.get(lastValue).getNome());
			assertNotSame("numero di telefono non coincide con valore atteso",
					valoriAttesi[1], tempAgenzie.get(lastValue).getNumTel());
			assertNotSame("indirizzo non voincide con valore atteso",
					valoriAttesi[2], tempAgenzie.get(lastValue).getIndirizzo());
		}
	}
	
	// questo metodo ci consente di resettare il contatore auto incrementate
	// settato per la tabella di mysql.
	private void resetMySQLCounter() throws Exception {
		String nomeTabella = "Agenzie";
		String nomeColonnaId = "ID";

		ResultSet rsId = connector.executeReadQuery("SELECT MAX( `"
				+ nomeColonnaId + "` ) AS " + nomeColonnaId + " FROM `"
				+ nomeTabella + "` ;");
		String maxId;

		try {
			rsId.next();
			maxId = (String) rsId.getString("ID");
			
			// se la tabella non ha istanze precedenti durante il test
			// allora alla fine deve essere svuotata completamente
			// altrimenti resettiamo il contatore autoincrementate all'id
			// dell'istanza vecchia piu' alto

			if (maxId == null) {
				connector.executeUpdateQuery("TRUNCATE TABLE `" + nomeTabella
						+ "`");
			} else {
				connector.executeUpdateQuery("ALTER TABLE `" + nomeTabella
						+ "` AUTO_INCREMENT = " + maxId + ";");
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
	}

}
