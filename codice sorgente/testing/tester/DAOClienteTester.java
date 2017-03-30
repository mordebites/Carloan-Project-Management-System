package tester;
import integration.connector.Connector;
import integration.connector.MySQLConnector;
import integration.dao.DAOCliente;
import integration.dao.DAOFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import business.entity.Cliente;
import business.transfer.Parameter;

public class DAOClienteTester extends TestCase {
	private final static String SERVER_PATH = "jdbc:mysql://localhost/carloan-test";
	private final static String USERNAME = "root";
	private final static String PASSWORD = "";
	
	static Logger log = Logger.getLogger(DAOClienteTester.class.getName());
	
	private DAOCliente daoCliente;
	private Connector connector;
	
	private Cliente cliente;
	private String idIstanzaTestata = null;

	private String[] valoriAttesi = { "Mario", "Rossi",
			"3384562109" };

	protected void setUp() throws Exception {
		daoCliente = (DAOCliente) DAOFactory.getDAOEntity("DAOCliente");
		connector = new MySQLConnector(SERVER_PATH, USERNAME, PASSWORD);
		daoCliente.setConnector(connector);
		
		cliente = new Cliente();

		// creo un oggetto cliente e successivamente lo passo
		// al dao dell'cliente in modo da scrivere una istanza
		// all'interno del database. Conservo inoltre l'id
		// dell'istanza di prova salvata nel database
		cliente.setNome(valoriAttesi[0]);
		cliente.setCognome(valoriAttesi[1]);
		cliente.setNumTel(valoriAttesi[2]);
		
		idIstanzaTestata = daoCliente.create(cliente);
	}

	protected void tearDown() throws Exception {
		// resetto dal database l'id autoincrementate al valore piu' alto
		// prima che venisse eseguito il test in modo da
		// lasciare invariato lo stato del database
		resetMySQLCounter();
		
		idIstanzaTestata = null;
		cliente = null;
		daoCliente = null;
		// distruggiamo il connector dopo aver eseguito
		// la funzione per il reset del counter di MySQL
		connector = null;
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

		Cliente tempCliente = daoCliente.read(par);

		assertEquals("nome non coincide con valore atteso", valoriAttesi[0],
				tempCliente.getNome());
		assertEquals("cognome non voincide con valore atteso",
				valoriAttesi[1], tempCliente.getCognome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesi[2], tempCliente.getNumTel());
		
	}

	/*
	 * Per evitare problemi concorrenziali durante il test nella lettura e
	 * scrittura del database, si suppone che il test venga eseguito quando il
	 * sistema non e' operativo. Il test sulla readAll legge l'ultimo valore
	 * inserito durante il test e se il sistema fosse operativo, i valori
	 * risulterebbero errati.
	 */
	public void testReadAll() {
		List<Cliente> tempAgenzie = daoCliente.readAll(null);

		int lastValue = tempAgenzie.size() - 1;

		// controllo gli ultimi valori della lista degli utenti
		// che corrispondon all'ultimo inserimento fatto

		assertEquals("nome non coincide con valore atteso", valoriAttesi[0],
				tempAgenzie.get(lastValue).getNome());
		assertEquals("cognome non voincide con valore atteso",
				valoriAttesi[1], tempAgenzie.get(lastValue).getCognome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesi[2], tempAgenzie.get(lastValue).getNumTel());
		
	}

	public void testUpdate() throws Exception {
		// creo un nuovo oggetto cliente che deve sostituire quello attualmente
		// nel database ed aggiorno il database che contiene
		// la tupla fittizia creata nella setUp

		String[] valoriAttesiModificati = { "Giovanni", "Caputo",
				"0805612304" };

		Cliente clienteModificato = new Cliente();
		clienteModificato.setId(idIstanzaTestata);
		clienteModificato.setNome(valoriAttesiModificati[0]);
		clienteModificato.setCognome(valoriAttesiModificati[1]);
		clienteModificato.setNumTel(valoriAttesiModificati[2]);

		daoCliente.update(clienteModificato);

		// dopo l'update rileggo la tupla modificata in base all'id
		// dell'istanza sotto test e controllo i valori se coincidono
		// con quelli modificati

		Parameter par = new Parameter();
		par.setValue("id", idIstanzaTestata);

		Cliente tempCliente = daoCliente.read(par);

		assertEquals("nome non coincide con valore atteso",
				valoriAttesiModificati[0], tempCliente.getNome());
		assertEquals("cognome non voincide con valore atteso",
				valoriAttesiModificati[1], tempCliente.getCognome());
		assertEquals("numero di telefono non coincide con valore atteso",
				valoriAttesiModificati[2], tempCliente.getNumTel());
		
	}
	
	// questo metodo ci consente di resettare il contatore auto incrementate
	// settato per la tabella di mysql.
	private void resetMySQLCounter() throws Exception {
		
		String nomeTabella = "Clienti";
		String nomeColonnaId = "ID";
		String maxId;
				
		// elimino l'istanza appena creata
		
		String deleteQuery = "DELETE FROM " + nomeTabella + " WHERE ID = '" + idIstanzaTestata + "'";
		
		try {
			connector.executeUpdateQuery(deleteQuery);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}

		ResultSet rsId = connector.executeReadQuery("SELECT MAX( `"
				+ nomeColonnaId + "` ) AS " + nomeColonnaId + " FROM `"
				+ nomeTabella + "` ;");
		
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
