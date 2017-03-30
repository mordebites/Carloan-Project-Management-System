package integration.dao;

import integration.connector.Connector;
import integration.connector.MySQLConnector;

/**
 * Classe astratta per l'implementazione del dao per la
 * comunicazione con il RDBMS MySQL.
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */

public abstract class MySQLDAO<Entity>{
	/**
	 * Il connettore specifico che servira' per eseguire le query
	 * verso il database management system.
	 */
	protected Connector connector;
	
	// campi per stabilire la connessione con il database
	private static final String SERVER_PATH = "jdbc:mysql://localhost/carloan";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	
	public MySQLDAO() throws Exception {
		 connector = new MySQLConnector(SERVER_PATH, USERNAME, PASSWORD);
	}
	
	public void setConnector(Connector conn) {
		this.connector = conn;
	}
}
