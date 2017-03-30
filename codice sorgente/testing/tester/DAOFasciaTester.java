package tester;

import integration.connector.Connector;
import integration.connector.MySQLConnector;
import integration.dao.DAOFactory;
import integration.dao.DAOFascia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.entity.Fascia;
import business.transfer.Parameter;

public class DAOFasciaTester extends TestCase {
	private final static String SERVER_PATH = "jdbc:mysql://localhost/carloan-test";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "";
	
	static Logger log = Logger.getLogger(DAOFasciaTester.class.getName());
	
	private DAOFascia daoFascia;
	private Connector connector;

	private Fascia fascia;
	private String idIstanzaTestata = null;

	private Object[] valoriAttesi = { "ZXY", "Berlina 4 Porte", new Float(0.1),
			new Float(60), new Float(320), new Float(65), new Float(340) };

	private boolean deleteTested = false;

	protected void setUp() throws Exception {
		daoFascia = (DAOFascia) DAOFactory.getDAOEntity("DAOFascia");
		connector = new MySQLConnector(SERVER_PATH, USERNAME, PASSWORD);
		daoFascia.setConnector(connector);
		
		fascia = new Fascia();

		// creo un oggetto fascia e successivamente lo passo
		// al dao della fascia in modo da scrivere una istanza
		// all'interno del database. Conservo inoltre l'id
		// dell'istanza di prova salvata nel database
		fascia.setTipo((String) valoriAttesi[0]);
		fascia.setDescrizione((String) valoriAttesi[1]);
		fascia.setCostoKm((Float) valoriAttesi[2]);
		fascia.setCostoGiornoLim((Float) valoriAttesi[3]);
		fascia.setCostoSettimanaLim((Float) valoriAttesi[4]);
		fascia.setCostoGiornoIllim((Float) valoriAttesi[5]);
		fascia.setCostoSettimanaIllim((Float) valoriAttesi[6]);

		idIstanzaTestata = daoFascia.create(fascia);
	}

	protected void tearDown() throws Exception {
		// cancello l'istanza di prova dal database
		// e setto a null l'oggetto fascia

		// poiche' dovremo testare anche la delete del DAO
		// al momento della teardown se e' stata gia' effettuata una
		// delete allora non cancelliamo nuovamente dal database lo stesso id
		if (!deleteTested) {
			daoFascia.delete(idIstanzaTestata);
		} else {
			deleteTested = false;
		}

		idIstanzaTestata = null;
		fascia = null;
		daoFascia = null;

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
		// con gli stessi dati presenti nel metodo setup di junit. In questo
		// modo
		// viene sollevata l'eccezione CampoDuplicatoException e controlliamo
		// che
		// l'eccezione sia effettivamente quella

		try {
			idIstanzaTestata = daoFascia.create(fascia);
		} catch (Exception e) {
			assertEquals("Eccezione campo duplicato non trovata!",
					CampoDuplicatoException.class, e.getClass());
		}
	}

	public void testDuplicatesUpdate() throws Exception {
		// creo una istanza temporanea di cui controllero' i duplicati
		// in fase di aggiornamento
		Fascia tempFascia = new Fascia();
		String idIstanzaTemporanea = null;

		Object[] valoriTemporanei = { "ZXX", "Berlina 4 Porte", new Float(0.1),
				new Float(60), new Float(320), new Float(65), new Float(340) };

		tempFascia.setTipo((String) valoriTemporanei[0]);
		tempFascia.setDescrizione((String) valoriTemporanei[1]);
		tempFascia.setCostoKm((Float) valoriTemporanei[2]);
		tempFascia.setCostoGiornoLim((Float) valoriTemporanei[3]);
		tempFascia.setCostoSettimanaLim((Float) valoriTemporanei[4]);
		tempFascia.setCostoGiornoIllim((Float) valoriTemporanei[5]);
		tempFascia.setCostoSettimanaIllim((Float) valoriTemporanei[6]);

		idIstanzaTemporanea = daoFascia.create(tempFascia);

		// aggiorno i valori della fascia creata nella setup
		// quindi avrò come riferimento la variabile idIstanzaTestata

		Object[] valoriAttesiModificati = { "ZXX", "Berlina 4 Porte",
				new Float(0.1), new Float(60), new Float(320), new Float(65),
				new Float(340) };

		Fascia fasciaModificata = new Fascia();
		fasciaModificata.setId(idIstanzaTestata);
		fasciaModificata.setTipo((String) valoriAttesiModificati[0]);
		fasciaModificata.setDescrizione((String) valoriAttesiModificati[1]);
		fasciaModificata.setCostoKm((Float) valoriAttesiModificati[2]);
		fasciaModificata.setCostoGiornoLim((Float) valoriAttesiModificati[3]);
		fasciaModificata
				.setCostoSettimanaLim((Float) valoriAttesiModificati[4]);
		fasciaModificata.setCostoGiornoIllim((Float) valoriAttesiModificati[5]);
		fasciaModificata
				.setCostoSettimanaIllim((Float) valoriAttesiModificati[6]);

		try {
			daoFascia.update(fasciaModificata);
		} catch (Exception e) {
			assertEquals("Eccezione campo duplicato non trovata!",
					CampoDuplicatoException.class, e.getClass());
		}

		// elimino l'istanza temporanea per questo test
		daoFascia.delete(idIstanzaTemporanea);
		resetMySQLCounter();
	}

	public void testCreate() {
		assertTrue(
				"Impossibile creare l'istanza nel database, variabile id errata",
				idIstanzaTestata.compareTo("0") != 0);
	}

	public void testRead() {
		// costruisco il parameter passandogli come campo id
		// quello ottenuto dopo la create principale nel metodo setUp
		Parameter par = new Parameter();
		par.setValue("id", idIstanzaTestata);

		Fascia tempFascia = daoFascia.read(par);

		assertEquals("tipo non coincide con valore atteso", valoriAttesi[0],
				tempFascia.getTipo());
		assertEquals("descrizione non coincide con valore atteso",
				valoriAttesi[1], tempFascia.getDescrizione());
		assertEquals("costo chilometrico non coincide con valore atteso",
				valoriAttesi[2], tempFascia.getCostoKm());
		assertEquals("costo giorno limitato non coincide con valore atteso",
				valoriAttesi[3], tempFascia.getCostoGiornoLim());
		assertEquals("costo settimana limitato non coincide con valore atteso",
				valoriAttesi[4], tempFascia.getCostoSettimanaLim());
		assertEquals("costo giorno illimitato non coincide con valore atteso",
				valoriAttesi[5], tempFascia.getCostoGiornoIllim());
		assertEquals(
				"costo settimana illimitato non coincide con valore atteso",
				valoriAttesi[6], tempFascia.getCostoSettimanaIllim());
	}

	/*
	 * Per evitare problemi concorrenziali durante il test nella lettura e
	 * scrittura del database, si suppone che il test venga eseguito quando il
	 * sistema non e' operativo. Il test sulla readAll legge l'ultimo valore
	 * inserito durante il test e se il sistema fosse operativo, i valori
	 * risulterebbero errati.
	 */
	public void testReadAll() {
		List<Fascia> tempFasce = daoFascia.readAll(null);

		int lastValue = tempFasce.size() - 1;

		// controllo gli ultimi valori della lista delle fasce
		// che corrispondon all'ultimo inserimento fatto

		assertEquals("tipo non coincide con valore atteso", valoriAttesi[0],
				tempFasce.get(lastValue).getTipo());
		assertEquals("descrizione non coincide con valore atteso",
				valoriAttesi[1], tempFasce.get(lastValue).getDescrizione());
		assertEquals("costo chilometrico non coincide con valore atteso",
				valoriAttesi[2], tempFasce.get(lastValue).getCostoKm());
		assertEquals("costo giorno limitato non coincide con valore atteso",
				valoriAttesi[3], tempFasce.get(lastValue).getCostoGiornoLim());
		assertEquals("costo settimana limitato non coincide con valore atteso",
				valoriAttesi[4], tempFasce.get(lastValue)
						.getCostoSettimanaLim());
		assertEquals("costo giorno illimitato non coincide con valore atteso",
				valoriAttesi[5], tempFasce.get(lastValue).getCostoGiornoIllim());
		assertEquals(
				"costo settimana illimitato non coincide con valore atteso",
				valoriAttesi[6], tempFasce.get(lastValue)
						.getCostoSettimanaIllim());
	}

	public void testUpdate() throws Exception {
		// creo un nuovo oggetto fascia che deve sostituire quello attualmente
		// nel database ed aggiorno il database che contiene
		// la tupla fittizia creata nella setUp

		Object[] valoriAttesiModificati = { "ZXX", "Berlina 4 Porte",
				new Float(0.1), new Float(60), new Float(320), new Float(65),
				new Float(340) };

		Fascia fasciaModificata = new Fascia();
		fasciaModificata.setId(idIstanzaTestata);
		fasciaModificata.setTipo((String) valoriAttesiModificati[0]);
		fasciaModificata.setDescrizione((String) valoriAttesiModificati[1]);
		fasciaModificata.setCostoKm((Float) valoriAttesiModificati[2]);
		fasciaModificata.setCostoGiornoLim((Float) valoriAttesiModificati[3]);
		fasciaModificata
				.setCostoSettimanaLim((Float) valoriAttesiModificati[4]);
		fasciaModificata.setCostoGiornoIllim((Float) valoriAttesiModificati[5]);
		fasciaModificata
				.setCostoSettimanaIllim((Float) valoriAttesiModificati[6]);

		daoFascia.update(fasciaModificata);

		// dopo l'update rileggo la tupla modificata in base all'id
		// dell'istanza sotto test e controllo i valori se coincidono
		// con quelli modificati

		Parameter par = new Parameter();
		par.setValue("id", idIstanzaTestata);

		Fascia tempFascia = daoFascia.read(par);

		assertEquals("tipo non coincide con valore atteso",
				valoriAttesiModificati[0], tempFascia.getTipo());
		assertEquals("descrizione non coincide con valore atteso",
				valoriAttesiModificati[1], tempFascia.getDescrizione());
		assertEquals("costo chilometrico non coincide con valore atteso",
				valoriAttesiModificati[2], tempFascia.getCostoKm());
		assertEquals("costo giorno limitato non coincide con valore atteso",
				valoriAttesiModificati[3], tempFascia.getCostoGiornoLim());
		assertEquals("costo settimana limitato non coincide con valore atteso",
				valoriAttesiModificati[4], tempFascia.getCostoSettimanaLim());
		assertEquals("costo giorno illimitato non coincide con valore atteso",
				valoriAttesiModificati[5], tempFascia.getCostoGiornoIllim());
		assertEquals(
				"costo settimana illimitato non coincide con valore atteso",
				valoriAttesiModificati[6], tempFascia.getCostoSettimanaIllim());
	}

	public void testDelete() {
		// effettuo una cancellazione dell'istanza fittizia
		// e setto la variabile di controllo per la delete nel teardown a true
		// in questo modo non cancello nuovamente nella teardown l'istanza
		// creata nella setup

		daoFascia.delete(idIstanzaTestata);
		deleteTested = true;

		List<Fascia> tempFasce = daoFascia.readAll(null);

		int lastValue = tempFasce.size() - 1;

		// controllo che dopo la delete la tabella non sia vuota
		if (lastValue >= 0) {
			// controllo gli ultimi valori della lista delle fasce
			// che corrispondon all'ultimo inserimento fatto

			assertNotSame("tipo non coincide con valore atteso",
					valoriAttesi[0], tempFasce.get(lastValue).getTipo());
			assertNotSame("descrizione non coincide con valore atteso",
					valoriAttesi[1], tempFasce.get(lastValue).getDescrizione());
			assertNotSame("costo chilometrico non coincide con valore atteso",
					valoriAttesi[2], tempFasce.get(lastValue).getCostoKm());
			assertNotSame(
					"costo giorno limitato non coincide con valore atteso",
					valoriAttesi[3], tempFasce.get(lastValue)
							.getCostoGiornoLim());
			assertNotSame(
					"costo settimana limitato non coincide con valore atteso",
					valoriAttesi[4], tempFasce.get(lastValue)
							.getCostoSettimanaLim());
			assertNotSame(
					"costo giorno illimitato non coincide con valore atteso",
					valoriAttesi[5], tempFasce.get(lastValue)
							.getCostoGiornoIllim());
			assertNotSame(
					"costo settimana illimitato non coincide con valore atteso",
					valoriAttesi[6], tempFasce.get(lastValue)
							.getCostoSettimanaIllim());
		}
	}

	// questo metodo ci consente di resettare il contatore auto incrementate
	// settato per la tabella di mysql.
	private void resetMySQLCounter() throws Exception {
		String nomeTabella = "Fasce";
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
