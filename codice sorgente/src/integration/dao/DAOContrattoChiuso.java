package integration.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Contratto;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione dei contratti chiusi nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di contratti chiusi.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOContrattoChiuso extends MySQLDAO<Contratto> implements DAOCRUD<Contratto> {
	private static final String INSERT_QUERY = "INSERT INTO ContrattiChiusi(ContrattoAperto, DataRientro, KmPercorsi, "
											 + "ImportoEffettivo, Saldo) "
											 + "VALUES ('?', '?', '?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE ContrattiChiusi SET ContrattoAperto = '?', "
											 + "DataRientro = '?', KmPercorsi = '?', "
											 + "ImportoEffettivo = '?', Saldo = '?' "
											 + "WHERE ContrattoAperto = ?";
	private static final String DELETE_QUERY = "DELETE FROM ContrattiChiusi WHERE ContrattoAperto = '?'";
	private static final String DELETE_APERTO_QUERY = "DELETE FROM ContrattiAperti WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ContrattoAperto, DataRientro, KmPercorsi, ImportoEffettivo, Saldo "
										   + "FROM ContrattiChiusi "
										   + "WHERE ContrattoAperto = '?'";
	private static final String READALL_QUERY = "SELECT ContrattoAperto, DataRientro, KmPercorsi, ImportoEffettivo, Saldo "
											  + "FROM ContrattiChiusi";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	
	//private static final String ID_COLUMN = "ID";
	private static final String CONTRATTOAPERTO_COLUMN = "ContrattoAperto";
	private static final String DATA_RIENTRO_COLUMN = "DataRientro";
	private static final String KM_PERCORSI_COLUMN = "KmPercorsi";
	private static final String IMPORTO_EFFETTIVO_COLUMN = "ImportoEffettivo";
	private static final String SALDO_COLUMN = "Saldo";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOContrattoChiuso.class.getName());
	
	public DAOContrattoChiuso() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un contratto chiuso specificata come parametro.
	 * 
	 * @param contratto contiene le informazioni sul contratto necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del contratto chiuso dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Contratto contratto) throws Exception {
		String insertQuery = INSERT_QUERY;
		
		String contrattoAperto = contratto.getId();
		insertQuery = insertQuery.replaceFirst("[?]", contrattoAperto);
		
		String dataRientro = contratto.getDataRientro().toString();
		insertQuery = insertQuery.replaceFirst("[?]", dataRientro);
		
		String kmPercorsi = Integer.toString(contratto.getKmPercorsi());
		insertQuery = insertQuery.replaceFirst("[?]", kmPercorsi);
		
		String importoEffettivo = Float.toString(contratto.getImportoEffettivo());
		insertQuery = insertQuery.replaceFirst("[?]", importoEffettivo);
		
		String saldo = Float.toString(contratto.getSaldo());
		insertQuery = insertQuery.replaceFirst("[?]", saldo);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String contrattoId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			contrattoId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return contrattoId;
	}
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sul contratto chiuso, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id del contratto chiuso da cercare nel database
	 * @return un oggetto di tipo contratto con le informazioni prese dal database
	 */	
	public Contratto read(Parameter par){//id deve essere l'id del contratto aperto
		String readQuery = READ_QUERY;
		String id = null;
		
		try{
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e){
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		readQuery = readQuery.replaceFirst("[?]", id);
		
		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Contratto> list = RStoList(readQueryResultSet);
		
		Contratto tempContratto = null;
		
		if(!list.isEmpty()) {
			tempContratto = list.get(0);
		}
		
		return tempContratto;
	}
	
	/**
	 * Una lista contenente tutti i contratti chiusi presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutti i contratti chiusi. Viene settato a null se si
	 * vogliono tutte le informazioni su tutti i contratti chiusi
	 * @return una lista contente tutti i contratti chiusi presenti nel database
	 */
	public List<Contratto> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Contratto> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un contratto chiuso presente nel database.
	 * 
	 * @param contratto l'oggetto di tipo contratto contenente i dati aggiornati
	 */
	public void update(Contratto contratto) throws Exception {
		String updateQuery = UPDATE_QUERY;
		
		String contrattoAperto = contratto.getId();
		updateQuery = updateQuery.replaceFirst("[?]", contrattoAperto);
		
		String dataRientro = contratto.getDataRientro().toString();
		updateQuery = updateQuery.replaceFirst("[?]", dataRientro);
		
		String kmPercorsi = Integer.toString(contratto.getKmPercorsi());
		updateQuery = updateQuery.replaceFirst("[?]", kmPercorsi);
		
		String importoEffettivo = Float.toString(contratto.getImportoEffettivo());
		updateQuery = updateQuery.replaceFirst("[?]", importoEffettivo);
		
		String saldo = Float.toString(contratto.getSaldo());
		updateQuery = updateQuery.replaceFirst("[?]", saldo);
		
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
	 * tipo contratto, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<Contratto> RStoList(ResultSet rs){
		List<Contratto> risultato = new LinkedList<Contratto>();
		DAOCRUD<Contratto> daoContrattoAperto = (DAOCRUD<Contratto>) DAOFactory.getDAOEntity("DAOContrattoAperto");

		try{
			while(rs.next()){
				Parameter par = new Parameter();
				par.setValue("id", rs.getString(CONTRATTOAPERTO_COLUMN));
				Contratto temp = daoContrattoAperto.read(par);
				
				Date dataRientro = Date.valueOf(rs.getString(DATA_RIENTRO_COLUMN)); 
				temp.setDataRientro(dataRientro);
				
				int kmPercorsi = Integer.parseInt(rs.getString(KM_PERCORSI_COLUMN));
				temp.setKmPercorsi(kmPercorsi);
				
				float importoEffettivo = Float.parseFloat(rs.getString(IMPORTO_EFFETTIVO_COLUMN));
				temp.setImportoEffettivo(importoEffettivo);

				float saldo = Float.parseFloat(rs.getString(SALDO_COLUMN));
				temp.setSaldo(saldo);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}

	/**
	 * Elimina un'istanza di contratto chiuso dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco dell'istanza da cancellare dal database
	 */
	public void delete(String id) {
		String deleteQuery = DELETE_QUERY;
		deleteQuery = deleteQuery.replaceFirst("[?]", id);
		
		String deleteApertoQuery = DELETE_APERTO_QUERY;
		deleteApertoQuery.replaceFirst("[?]", id);
		
		try {
			connector.executeUpdateQuery(deleteQuery);
			connector.executeUpdateQuery(deleteApertoQuery);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}		
	}
}
