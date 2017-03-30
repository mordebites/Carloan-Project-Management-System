package integration.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ConnessioneAlDataBaseException;

/**
 * Classe che implementa l'interfaccia connector per la comunicazione con il
 * sistema di persistenza dei dati. Utilizzza il driver jdbc per l'utilizzo
 * del RDBMS MySQL.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class MySQLConnector implements Connector {
	private Connection connessione;
	
	static Logger log = Logger.getLogger(MySQLConnector.class.getName());
	
	private static final int MYSQL_ERROR_CONNECTION = 0;
	
	/**
	 * Costruttore della classe che servira' per stabilire la connessione con il DBMS.
	 * Se la connessione dovesse dare problemi allora viene lanciata una eccezione.
	 * @throws Exception
	 */
	public MySQLConnector(String server_path, String username, String password) throws Exception {
		try {
			new com.mysql.jdbc.Driver();	
			connessione = DriverManager.getConnection(server_path, username, password);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_CONNECTION) {
				throw new ConnessioneAlDataBaseException();
			}
		}
	}

	/**
	 * Esegue la query di lettura SQL inserita come parametro e restituisce il result set
	 * ottenuto dal DBMS.
	 * @param query la query da far eseguire
	 * @return il result set ottenuto dal DBMS
	 */
	public ResultSet executeReadQuery(String query)  throws SQLException {
		ResultSet risultato = null;
		
		try {
			Statement istruzione = connessione.createStatement();
			risultato = istruzione.executeQuery(query);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return risultato;
	}

	/**
	 * Esegue la query di aggiornamento/creazione/eliminazione SQL
	 * inserita come parametro e restituisce il result set ottenuto dal DBMS.
	 * @param query la query da far eseguire
	 */
	public void executeUpdateQuery(String query) throws SQLException {		

		Statement istruzione = connessione.createStatement();
		istruzione.executeUpdate(query);
	}

}
