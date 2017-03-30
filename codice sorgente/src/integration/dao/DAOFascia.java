package integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Fascia;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle fasce nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di fasce.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOFascia extends MySQLDAO<Fascia> implements DAOCRUD<Fascia>{
	private static final String INSERT_QUERY = "INSERT INTO Fasce (Tipo, Descrizione, CostoKm, "
											 + "CostoGiornoLim, CostoSettimanaLim, CostoGiornoIllim, CostoSettimanaIllim) "
											 + "VALUES ('?', '?', '?', '?', '?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE Fasce SET Tipo = '?', Descrizione = '?', CostoKm = '?', "
											 + "CostoGiornoLim = '?', CostoSettimanaLim = '?', "
											 + "CostoGiornoIllim = '?', CostoSettimanaIllim = '?' "
											 + "WHERE ID = '?'";
	private static final String DELETE_QUERY = "DELETE FROM Fasce WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Tipo, Descrizione, CostoKm, CostoGiornoLim, CostoSettimanaLim,"
									       + " CostoGiornoIllim, CostoSettimanaIllim "
										   + "FROM Fasce WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Tipo, Descrizione, CostoKm, CostoGiornoLim, CostoSettimanaLim,"
											  + " CostoGiornoIllim, CostoSettimanaIllim "
											  + "FROM Fasce";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	private static final String ID_COLUMN = "ID";
	private static final String TIPO_COLUMN = "Tipo";
	private static final String DESCRIZIONE_COLUMN = "Descrizione";
	private static final String COSTO_KM_COLUMN = "CostoKm";
	private static final String COSTOGIORNO_LIM_COLUMN = "CostoGiornoLim";
	private static final String COSTOSETTIMANA_LIM_COLUMN = "CostoSettimanaLim";
	private static final String COSTOGIORNO_ILLIM_COLUMN = "CostoGiornoIllim";
	private static final String COSTOSETTIMANA_ILLIM_COLUMN = "CostoSettimanaIllim";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOFascia.class.getName());
	
	public DAOFascia() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su una fascia specificata come parametro.
	 * 
	 * @param fascia contiene le informazioni sulla fascia necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id della fascia dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(Fascia fascia) throws Exception {
		String insertQuery = INSERT_QUERY;
		
		String tipo = fascia.getTipo();
		insertQuery = insertQuery.replaceFirst("[?]", tipo);
		
		String descrizione = fascia.getDescrizione();
		insertQuery = insertQuery.replaceFirst("[?]", descrizione);
		
		String costoKm = String.valueOf(fascia.getCostoKm());
		insertQuery = insertQuery.replaceFirst("[?]", costoKm);
		
		String costoGiornoLim = String.valueOf(fascia.getCostoGiornoLim());
		insertQuery = insertQuery.replaceFirst("[?]", costoGiornoLim);
		
		String costoSettimanaLim = String.valueOf(fascia.getCostoSettimanaLim());
		insertQuery = insertQuery.replaceFirst("[?]", costoSettimanaLim);
		
		String costoGiornoIllim = String.valueOf(fascia.getCostoGiornoIllim());
		insertQuery = insertQuery.replaceFirst("[?]", costoGiornoIllim);
		
		String costoSettimanaIllim = String.valueOf(fascia.getCostoSettimanaIllim());
		insertQuery = insertQuery.replaceFirst("[?]", costoSettimanaIllim);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String fasciaId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			fasciaId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return fasciaId;
	}
	
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sulla fascia, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id della fascia da cercare nel database
	 * @return un oggetto di tipo fascia con le informazioni prese dal database
	 */	
	public Fascia read(Parameter par){
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
		List<Fascia> list = RStoList(readQueryResultSet);
		
		Fascia tempFascia = null;
		
		if(!list.isEmpty()) {
			tempFascia = list.get(0);
		}
		
		return tempFascia;
	
	}
	
	/**
	 * Una lista contenente tutte le fasce presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutte le fasce. Viene settato a null se si
	 * vogliono tutte le informazioni su tutte le fasce
	 * @return una lista contente tutte le fasce presenti nel database
	 */
	public List<Fascia> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Fascia> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su una fascia presente nel database.
	 * 
	 * @param agenzia l'oggetto di tipo fascia contenente i dati aggiornati
	 * @throws exception
	 */
	public void update(Fascia fascia) throws Exception {
		String updateQuery = UPDATE_QUERY;
		
		String tipo = fascia.getTipo();
		updateQuery = updateQuery.replaceFirst("[?]", tipo);
		
		String descrizione = fascia.getDescrizione();
		updateQuery = updateQuery.replaceFirst("[?]", descrizione);
		
		String costoKm = String.valueOf(fascia.getCostoKm());
		updateQuery = updateQuery.replaceFirst("[?]", costoKm);
		
		String costoGiornoLim = String.valueOf(fascia.getCostoGiornoLim());
		updateQuery = updateQuery.replaceFirst("[?]", costoGiornoLim);
		
		String costoSettimanaLim = String.valueOf(fascia.getCostoSettimanaLim());
		updateQuery = updateQuery.replaceFirst("[?]", costoSettimanaLim);
		
		String costoGiornoIllim = String.valueOf(fascia.getCostoGiornoIllim());
		updateQuery = updateQuery.replaceFirst("[?]", costoGiornoIllim);
		
		String costoSettimanaIllim = String.valueOf(fascia.getCostoSettimanaIllim());
		updateQuery = updateQuery.replaceFirst("[?]", costoSettimanaIllim);
		
		String idFascia = fascia.getId();
		updateQuery = updateQuery.replaceFirst("[?]", idFascia);
		
		try {
			connector.executeUpdateQuery(updateQuery);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
		}
	}
	
	/**
	 * Elimina un'istanza di fascia dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco dell'istanza da cancellare dal database
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
	 * tipo fascia, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	private List<Fascia> RStoList(ResultSet rs){
		List<Fascia> risultato = new LinkedList<Fascia>();
		
		try{
			while(rs.next()){
				Fascia temp = new Fascia();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				String tipo = rs.getString(TIPO_COLUMN);
				temp.setTipo(tipo);
				
				String descrizione = rs.getString(DESCRIZIONE_COLUMN);
				temp.setDescrizione(descrizione);
				
				float costoKm = Float.parseFloat(rs.getString(COSTO_KM_COLUMN));
				temp.setCostoKm(costoKm);
				
				float costoGiornoLim = Float.parseFloat(rs.getString(COSTOGIORNO_LIM_COLUMN));
				temp.setCostoGiornoLim(costoGiornoLim);
				
				float costoSettimanaLim = Float.parseFloat(rs.getString(COSTOSETTIMANA_LIM_COLUMN));
				temp.setCostoSettimanaLim(costoSettimanaLim);
				
				float costoGiornoIllim = Float.parseFloat(rs.getString(COSTOGIORNO_ILLIM_COLUMN));
				temp.setCostoGiornoIllim(costoGiornoIllim);
				
				float costoSettimanaIllim = Float.parseFloat(rs.getString(COSTOSETTIMANA_ILLIM_COLUMN));
				temp.setCostoSettimanaIllim(costoSettimanaIllim);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}

}
