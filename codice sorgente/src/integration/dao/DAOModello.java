package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Fascia;
import business.entity.Modello;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle agenzie nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di agenzie.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOModello extends MySQLDAO<Modello> implements DAOCRUD<Modello>{
	private static final String INSERT_QUERY = "INSERT INTO Modelli(Nome, Fascia) "
											 + "VALUES ('?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE Modelli SET Nome = '?', Fascia = '?' "
											 + "WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM Modelli WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Nome, Fascia "
										   + "FROM Modelli WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Nome, Fascia "
											  + "FROM Modelli";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	
	private static final String ID_COLUMN = "ID";
	private static final String NOME_COLUMN = "Nome";
	private static final String FASCIA_COLUMN = "Fascia";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOModello.class.getName());
	
	public DAOModello() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un modello specificato come parametro.
	 * 
	 * @param modello contiene le informazioni sul modello necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del modello dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Modello modello) throws Exception {
		String insertQuery = INSERT_QUERY;
		
		String nome = modello.getNome();
		insertQuery = insertQuery.replaceFirst("[?]", nome);
		
		Fascia fascia = modello.getFascia();
		String idFascia = fascia.getId();
		insertQuery = insertQuery.replaceFirst("[?]", idFascia);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String modelloId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			modelloId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return modelloId;
	}
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sul modello, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id del modello da cercare nel database
	 * @return un oggetto di tipo modello con le informazioni prese dal database
	 */	
	public Modello read(Parameter par){
		String readQuery = READ_QUERY;
		String id = null;
		
		try{
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e){
			log.error("Chiave non disponibile all'interno del transfer object!");
		}
		
		readQuery = readQuery.replaceFirst("[?]", id);

		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Modello> list = RStoList(readQueryResultSet);
		
		Modello tempModello = null;
		
		if(!list.isEmpty()) {
			tempModello = list.get(0);
		}
		
		return tempModello;	
	}
	
	/**
	 * Una lista contenente tutti i modelli presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutti i modelli. Viene settato a null se si
	 * vogliono tutte le informazioni su tutti i modelli
	 * @return una lista contente tutti i modelli presenti nel database
	 */
	public List<Modello> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Modello> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un modello presente nel database.
	 * 
	 * @param modello l'oggetto di tipo modello contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(Modello modello) throws Exception {
		String updateQuery = UPDATE_QUERY;
		
		String nome = modello.getNome();
		updateQuery = updateQuery.replaceFirst("[?]", nome);
		
		Fascia fascia = modello.getFascia();
		String idFascia = fascia.getId();
		updateQuery = updateQuery.replaceFirst("[?]", idFascia);
		
		String idModello = modello.getId();
		updateQuery = updateQuery.replaceFirst("[?]", idModello);
		
		try {
			connector.executeUpdateQuery(updateQuery);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
		}
	}
	
	/**
	 * Elimina un'istanza di modello dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco del modello da cancellare dal database
	 */
	public void delete(String id){
		String deleteQuery = DELETE_QUERY;
		deleteQuery = deleteQuery.replaceFirst("[?]", id);
		
		try {
			connector.executeUpdateQuery(deleteQuery);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
	}
	
	/*
	 * Converte il resultset inserito come parametro in una lista di oggetti di
	 * tipo modello, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<Modello> RStoList(ResultSet rs){
		List<Modello> risultato = new LinkedList<Modello>();
		DAOCRUD<Fascia> daoFascia = (DAOCRUD<Fascia>) DAOFactory.getDAOEntity("DAOFascia");
		Modello temp = null;
		Parameter par = null;
		
		try{
			while(rs.next()){
				temp = new Modello();
				par = new Parameter();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				String nome = rs.getString(NOME_COLUMN);
				temp.setNome(nome);
				
				
				String idFascia = rs.getString(FASCIA_COLUMN);
				par.setValue("id", idFascia);
				Fascia fascia = daoFascia.read(par);
				temp.setFascia(fascia);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}

}
