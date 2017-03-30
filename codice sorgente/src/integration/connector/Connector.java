package integration.connector;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfaccia che ci servira' per eseguire le query di aggiornamento
 * e lettura dal sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public interface Connector {

    public abstract ResultSet executeReadQuery(String query) throws SQLException;

    public abstract void executeUpdateQuery(String query) throws SQLException;

}