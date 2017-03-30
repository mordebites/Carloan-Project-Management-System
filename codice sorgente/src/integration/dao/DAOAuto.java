package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.QueryStringReplacer;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Auto;
import business.entity.Modello;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle auto nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di auto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOAuto extends MySQLDAO<Auto> implements DAOCRUD<Auto> {

	private static final String INSERT_QUERY = "INSERT INTO Auto(Targa, Chilometraggio, Modello) "
			+ "VALUES ('?', '?', '?')";
	private static final String UPDATE_QUERY = "UPDATE Auto SET Targa = '?', Chilometraggio = '?', "
			+ "Modello = '?' " + "WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM Auto WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Targa, Chilometraggio, Modello "
			+ "FROM Auto WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Targa, Chilometraggio, Modello FROM Auto";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";

	private static final String ID_COLUMN = "ID";
	private static final String TARGA_COLUMN = "Targa";
	private static final String CHILOMETRAGGIO_COLUMN = "Chilometraggio";
	private static final String MODELLO_COLUMN = "Modello";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOAuto.class.getName());
	
	public DAOAuto() throws Exception {
		super();
	}

	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su una auto specificata come parametro.
	 * 
	 * @param auto contiene le informazioni sull'auto necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id auto dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Auto auto) throws Exception {
		String[] values = new String[3];
		values[0] = auto.getTarga();
		values[1] = String.valueOf(auto.getChilometraggio());
		values[2] = auto.getModello().getId();
		
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);

		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}

		String autoId = null;

		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			autoId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}

		return autoId;
	}

	/**
	 * Legge una istanza dal database contente informazioni
	 * sull'auto, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id dell'auto da cercare nel database
	 * @return un oggetto di tipo auto con le informazioni prese dal database
	 */	
	public Auto read(Parameter par) {
		String id = null;

		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		String[] values = {id};
		String readQuery = QueryStringReplacer.replaceValues(READ_QUERY, values);
		
		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Auto> list = RStoList(readQueryResultSet);
		
		Auto auto = null;
		
		if(!list.isEmpty()) {
			auto = list.get(0);
		}
		
		return auto;

	}

	/**
	 * Una lista contenente tutte le auto presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutte le auto. Viene settato a null se si
	 * vogliono tutte le informazioni su tutte le auto
	 * @return una lista contente tutte le auto presenti nel database
	 */
	public List<Auto> readAll(Parameter par) {
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Auto> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un'auto presente nel database.
	 * 
	 * @param auto l'oggetto di tipo auto contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(Auto auto) throws Exception {
		String[] values = new String[4];
		values[0] = auto.getTarga();
		values[1] = String.valueOf(auto.getChilometraggio());
		values[2] = auto.getModello().getId();
		values[3] = auto.getId();
		
		String updateQuery = QueryStringReplacer.replaceValues(UPDATE_QUERY, values);

		try {
			connector.executeUpdateQuery(updateQuery);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
		}
	}

	/**
	 * Elimina un'istanza di auto dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco dell'istanza da cancellare dal database
	 */
	public void delete(String id) {
		String[] values = {id};
		String deleteQuery = QueryStringReplacer.replaceValues(DELETE_QUERY, values);

		try {
			connector.executeUpdateQuery(deleteQuery);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
	}

	/*
	 * Converte il resultset inserito come parametro in una lista di oggetti di
	 * tipo auto, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Auto> RStoList(ResultSet rs) {
		List<Auto> risultato = new LinkedList<Auto>();
		DAOCRUD<Modello> daoModello = (DAOCRUD) DAOFactory
				.getDAOEntity("DAOModello");
		Parameter par = new Parameter();
		Auto temp = null;

		try {
			while (rs.next()) {
				temp = new Auto();

				String id = rs.getString(ID_COLUMN);
				temp.setId(id);

				String targa = rs.getString(TARGA_COLUMN);
				temp.setTarga(targa);

				int chilometraggio = Integer.parseInt(rs.getString(
						CHILOMETRAGGIO_COLUMN).trim());
				temp.setChilometraggio(chilometraggio);

				String idModello = rs.getString(MODELLO_COLUMN);
				par.setValue("id", idModello);
				Modello modello = daoModello.read(par);
				temp.setModello(modello);

				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		return risultato;
	}
}
