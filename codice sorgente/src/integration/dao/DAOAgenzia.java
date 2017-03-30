package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.QueryStringReplacer;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Agenzia;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle agenzie nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di agenzie.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOAgenzia extends MySQLDAO<Agenzia> implements DAOCRUD<Agenzia> {

	private static final String INSERT_QUERY = "INSERT INTO Agenzie (Nome, NumTel, Indirizzo) "
											 + "VALUES ('?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE Agenzie SET Nome = '?', NumTel = '?', Indirizzo = '?' "
											 + "WHERE ID = '?'";
	private static final String DELETE_QUERY = "DELETE FROM Agenzie WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Nome, NumTel, Indirizzo "
										   + "FROM Agenzie WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Nome, NumTel, Indirizzo "
											  + "FROM Agenzie";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	private static final String ID_COLUMN = "ID";
	private static final String NOME_COLUMN = "Nome";
	private static final String NUMEROTEL_COLUMN = "NumTel";
	private static final String INDIRIZZO_COLUMN = "Indirizzo";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOAgenzia.class.getName());
	
	public DAOAgenzia() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su una agenzia specificata come parametro.
	 * 
	 * @param agenzia contiene le informazioni sull'agenzia necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id dell'agenzia dell'istanza appena creata nel database.
	 * @throws Exception 
	 */
	public String create(Agenzia agenzia) throws Exception{
		String[] values = new String[3];
		values[0] = agenzia.getNome();
		values[1] = agenzia.getNumTel();
		values[2] = agenzia.getIndirizzo();
		
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String agenziaId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			agenziaId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return agenziaId;
	}
	

	/**
	 * Legge una istanza dal database contente informazioni
	 * sull'agenzia, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id dell'agenzia da cercare nel database
	 * @return un oggetto di tipo agenzia con le informazioni prese dal database
	 */
	public Agenzia read(Parameter par){
		String id = null;
		
		try{
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e){
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
		List<Agenzia> list = RStoList(readQueryResultSet);
		
		Agenzia agenzia = null;
		
		if(!list.isEmpty()) {
			agenzia = list.get(0);
		}
		
		return agenzia;
	}
	
	/**
	 * Una lista contenente tutte le agenzie presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutte le agenzie. Viene settato a null se si
	 * vogliono tutte le informazioni su tutte le agenzie
	 * @return una lista contente tutte le agenzie presenti nel database
	 */
	public List<Agenzia> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Agenzia> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	
	/**
	 * Aggiorna le informazioni su una agenzia presente nel database.
	 * 
	 * @param agenzia l'oggetto di tipo agenzia contenente i dati aggiornati
	 */
	public void update(Agenzia agenzia) throws Exception {
		String[] values = new String[4];
		values[0] = agenzia.getNome();
		values[1] = agenzia.getNumTel();
		values[2] = agenzia.getIndirizzo();
		values[3] = agenzia.getId();
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
	 * Elimina un'istanza di agenzia dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco dell'istanza da cancellare dal database
	 */
	public void delete(String id){
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
	 * tipo agenzia, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	private List<Agenzia> RStoList(ResultSet rs){
		List<Agenzia> risultato = new LinkedList<Agenzia>();
		
		try{
			while(rs.next()){
				Agenzia temp = new Agenzia();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				String nome = rs.getString(NOME_COLUMN);
				temp.setNome(nome);
				
				String numTel = rs.getString(NUMEROTEL_COLUMN);
				temp.setNumTel(numTel);
				
				String indirizzo = rs.getString(INDIRIZZO_COLUMN);
				temp.setIndirizzo(indirizzo);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}
}
