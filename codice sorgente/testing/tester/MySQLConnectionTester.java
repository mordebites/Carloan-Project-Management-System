package tester;

import integration.connector.Connector;
import integration.connector.MySQLConnector;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import business.applicationservice.exception.ConnessioneAlDataBaseException;

public class MySQLConnectionTester extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public void testConnection() {
		// controllo che sbagliando i valori per stabilire la connessione
		// con il database mysql venga sollevata l'eccezione ConnessioneAlDataBaseException.
		
		try {
			@SuppressWarnings("unused")
			Connector connector = new MySQLConnector("wrong path", "wrong user", "wrong password");
		} catch (Exception e) {
			assertEquals("La connessione al database e' errata", ConnessioneAlDataBaseException.class, e.getClass());
		}
	}

}
