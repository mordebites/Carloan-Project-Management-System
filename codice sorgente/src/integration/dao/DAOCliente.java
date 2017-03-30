package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.QueryStringReplacer;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Cliente;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione dei clienti nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento di clienti.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOCliente extends MySQLDAO<Cliente> implements DAOCRU<Cliente> {

	private static final String INSERT_QUERY = "INSERT INTO Clienti (Nome, Cognome, NumTel) "
											 + "VALUES ('?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE Clienti SET Nome = '?', Cognome = '?', NumTel = '?' "
											 + "WHERE ID = ?";
	//private static final String DELETE_QUERY = "DELETE FROM Clienti WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Nome, Cognome, NumTel FROM Clienti WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Nome, Cognome, NumTel FROM Clienti";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	private static final String ID_COLUMN = "ID";
	private static final String NOME_COLUMN = "Nome";
	private static final String NUMTEL_COLUMN = "NumTel";
	private static final String COGNOME_COLUMN = "Cognome";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOCliente.class.getName());
	
	public DAOCliente() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un cliente specificata come parametro.
	 * 
	 * @param cliente contiene le informazioni sul cliente necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del cliente dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Cliente cliente) throws Exception {
		String[] values = new String[3];
		values[0] = cliente.getNome();
		values[1] = cliente.getCognome();
		values[2] = cliente.getNumTel();
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String clienteId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			clienteId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return clienteId;
	}
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sul cliente, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id del cliente da cercare nel database
	 * @return un oggetto di tipo cliente con le informazioni prese dal database
	 */	
	public Cliente read(Parameter par){
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
		List<Cliente> list = RStoList(readQueryResultSet);
		
		Cliente tempCliente = null;
		
		if(!list.isEmpty()) {
			tempCliente = list.get(0);
		}
		
		return tempCliente;
	}
	
	/**
	 * Una lista contenente tutti clientei presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutti i clienti. Viene settato a null se si
	 * vogliono tutte le informazioni su tutti i clienti.
	 * @return una lista contente tutti clienti presenti nel database
	 */
	public List<Cliente> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Cliente> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un cliente presente nel database.
	 * 
	 * @param cliente l'oggetto di tipo cliente contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(Cliente cliente) throws Exception {		
		String[] values = new String[4];
		values[0] = cliente.getNome();
		values[1] = cliente.getCognome();
		values[2] = cliente.getNumTel();
		values[3] = cliente.getId();
		
		String updateQuery = QueryStringReplacer.replaceValues(UPDATE_QUERY, values);
		
		try {
			connector.executeUpdateQuery(updateQuery);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
		}
	}
	
	/*
	 * Converte il resultset inserito come parametro in una lista di oggetti di
	 * tipo cliente, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	private List<Cliente> RStoList(ResultSet rs){
		List<Cliente> risultato = new LinkedList<Cliente>();
		
		try{
			while(rs.next()){
				Cliente temp = new Cliente();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				String nome = rs.getString(NOME_COLUMN);
				temp.setNome(nome);
				
				String cognome = rs.getString(COGNOME_COLUMN);
				temp.setCognome(cognome);
				
				String numTel = rs.getString(NUMTEL_COLUMN);
				temp.setNumTel(numTel);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}

}
