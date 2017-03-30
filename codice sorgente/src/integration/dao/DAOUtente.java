package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Agenzia;
import business.entity.TipoUtente;
import business.entity.Utente;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione degli utenti nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di utenti.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOUtente extends MySQLDAO<Utente> implements DAOCR<Utente> {
	private static final String INSERT_QUERY = "INSERT INTO Utenti (Username, Password, TipoUtente, Agenzia) "
			+ "VALUES ('?', '?', '?', '?')";
	//private static final String UPDATE_QUERY = "UPDATE Utenti SET Username = '?', Password = '?', Tipo = '?' "
	//		+ "WHERE ID = ?";
	//private static final String DELETE_QUERY = "DELETE FROM Utenti WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Username, Password, TipoUtente, Agenzia FROM Utenti WHERE Username = '?'";
	private static final String READALL_QUERY = "SELECT ID, Username, Password, TipoUtente, Agenzia FROM Utenti";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";

	private static final String ID_COLUMN = "ID";
	private static final String USERNAME_COLUMN = "Username";
	private static final String PASSWORD_COLUMN = "Password";
	private static final String TIPO_COLUMN = "TipoUtente";
	private static final String AGENZIA_COLUMN = "Agenzia";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOUtente.class.getName());
	
	public DAOUtente() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un utente specificata come parametro.
	 * 
	 * @param utente contiene le informazioni sull'utente necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id dell'utente dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Utente utente) throws Exception {
		String insertQuery = INSERT_QUERY;
		
		String username = utente.getUsername();
		insertQuery = insertQuery.replaceFirst("[?]", username);
		
		String password = utente.getPassword();
		insertQuery = insertQuery.replaceFirst("[?]", password);
		
		String tipo = utente.getTipo().toString();
		insertQuery = insertQuery.replaceFirst("[?]", tipo);
		
		Agenzia agenzia = utente.getAgenzia();
		String idAgenzia = agenzia.getId();
		insertQuery = insertQuery.replaceFirst("[?]", idAgenzia);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String utenteId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			utenteId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return utenteId;
	}
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sull'utente, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id dell'utente da cercare nel database
	 * @return un oggetto di tipo utente con le informazioni prese dal database
	 */
	public Utente read(Parameter par) {
		String readQuery = READ_QUERY;
		String username = null;
		
		try{
			username = (String) par.getValue("Username");
		} catch (ChiaveNonDisponibileException e){
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		readQuery = readQuery.replaceFirst("[?]", username);
		
		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Utente> list = RStoList(readQueryResultSet);
		
		Utente tempUtente = null;
		
		if(!list.isEmpty()) {
			tempUtente = list.get(0);
		}
		
		return tempUtente;
	}

	/**
	 * Una lista contenente tutti gli utenti presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutti gli utenti. Viene settato a null se si
	 * vogliono tutte le informazioni su tutte le agenzie
	 * @return una lista contente tutti gli utenti presenti nel database
	 */
	public List<Utente> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Utente> list = RStoList(readAllQueryResultSet);
		
		return list;
	}
	
	/*
	 * Converte il resultset inserito come parametro in una lista di oggetti di
	 * tipo agenzia, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<Utente> RStoList(ResultSet rs){
		List<Utente> risultato = new LinkedList<Utente>();
		DAOCRUD<Agenzia> daoAgenzia = (DAOCRUD<Agenzia>) DAOFactory.getDAOEntity("DAOAgenzia");
		
		Parameter par = new Parameter();
		
		try{
			while(rs.next()){
				Utente temp = new Utente();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				String username = rs.getString(USERNAME_COLUMN);
				temp.setUsername(username);
				
				String password = rs.getString(PASSWORD_COLUMN);
				temp.setPassword(password);
				
				TipoUtente tipo = TipoUtente.valueOf(rs.getString(TIPO_COLUMN));
				temp.setTipo(tipo);
				
				String idAgenzia = rs.getString(AGENZIA_COLUMN);
				par.setValue("id", idAgenzia);
				Agenzia agenzia = daoAgenzia.read(par);
				temp.setAgenzia(agenzia);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}

}
