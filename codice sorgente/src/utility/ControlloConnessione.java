package utility;

import integration.dao.MySQLDAO;

/**
 * Classe che servirà per effettuare un controllo di connessione
 * verso il database MySQL. Se tale connessione dovesse dare problemi
 * il costruttore lancia una eccezione che verra' gestita in tier superiori
 * @author Simone Marzulli e Morena De Liddo
 *
 */

public class ControlloConnessione extends MySQLDAO<Object> {
	
	/**
	 * Costruttore della classe che istanzia un oggetto di tipo MySQLConnector
	 * per la verifica di errori nella connessione con il DBMS
	 * @throws Exception
	 */
	public ControlloConnessione() throws Exception {
		super();
	}
}
