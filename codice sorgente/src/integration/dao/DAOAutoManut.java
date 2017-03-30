package integration.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.QueryStringReplacer;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.entity.Auto;
import business.entity.PeriodoManut;
import business.entity.TipoManut;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle auto in manutenzione nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di auto in menutenzione.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOAutoManut extends MySQLDAO<PeriodoManut> implements DAOCRUD<PeriodoManut>{

	private static final String INSERT_QUERY = "INSERT INTO AutoManutenzione(Auto, TipoManut, DataInizio, DataFine) "
											 + "VALUES ('?', '?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE AutoManutenzione SET Auto = '?', TipoManut = '?', "
											 + "DataInizio = '?', DataFine = '?' "
											 + "WHERE ID = '?'";
	private static final String DELETE_QUERY = "DELETE FROM AutoManutenzione WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, Auto, TipoManut, DataInizio, DataFine FROM AutoManutenzione WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, Auto, TipoManut, DataInizio, DataFine FROM AutoManutenzione";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	private static final String ID_COLUMN = "ID";
	private static final String AUTO_COLUMN = "Auto";
	private static final String TIPOMANUT_COLUMN = "TipoManut";
	private static final String DATAINIZIO_COLUMN = "DataInizio";
	private static final String DATAFINE_COLUMN = "DataFine";
	private static final String ULTIMOID_COLUMN = "UltimoId";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOAutoManut.class.getName());
	
	public DAOAutoManut() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un periodo di manutenzione specificata come parametro.
	 * 
	 * @param manut contiene le informazioni sul periodo necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del periodo dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(PeriodoManut manut) throws Exception {
		String[] values = new String[4];
		values[0] = manut.getAuto().getId();
		values[1] = manut.getTipo().toString();
		values[2] = manut.getDataInizio().toString();
		values[3] = manut.getDataFine().toString();
		
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String manutId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			manutId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return manutId;
	}
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sul periodo, in base all'id specificato come parametro.
	 * 
	 * @param par transfer object contente l'id del periodo da cercare nel database
	 * @return un oggetto di tipo periodo manutenzione con le informazioni prese dal database
	 */	
	public PeriodoManut read(Parameter par){ 
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
		List<PeriodoManut> list = RStoList(readQueryResultSet);
		
		PeriodoManut periodo = null;
		
		if(!list.isEmpty()) {
			periodo = list.get(0);
		}
		
		return periodo;
	
	}
	
	/**
	 * Una lista contenente tutti i periodi di manutenzione presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutte le auto in manutenzione. Viene settato a null se si
	 * vogliono tutte le informazioni su tutti i periodi
	 * @return una lista contente tutti i periodi di manutenzione presenti nel database
	 */
	public List<PeriodoManut> readAll(Parameter par){
		String readAllQuery = READALL_QUERY;
		ResultSet readAllQueryResultSet = null;
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<PeriodoManut> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un periodo di manutenzione presente nel database.
	 * 
	 * @param disp l'oggetto di tipo periodo manutenzione contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(PeriodoManut manut) throws Exception {
		
		String[] values = new String[5];
		values[0] = manut.getAuto().getId();
		values[1] = manut.getTipo().toString();
		values[2] = manut.getDataInizio().toString();
		values[3] = manut.getDataFine().toString();
		values[4] = manut.getId();
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
	 * Elimina un'istanza di periodo menutenzione dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco del periodo da cancellare dal database
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
	 * tipo periodo manutenzione, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<PeriodoManut> RStoList(ResultSet rs){
		List<PeriodoManut> risultato = new LinkedList<PeriodoManut>();
		DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
		Parameter par = new Parameter();
		PeriodoManut temp = null;
		Auto auto = null;
		
		try{
			while(rs.next()){
				temp = new PeriodoManut();
				
				temp.setId(rs.getString(ID_COLUMN));
				
				par.setValue("id", rs.getString(AUTO_COLUMN));
				auto = daoAuto.read(par);
				temp.setAuto(auto);
				
				temp.setTipo(TipoManut.valueOf(rs.getString(TIPOMANUT_COLUMN)));	
				temp.setDataInizio(Date.valueOf(rs.getString(DATAINIZIO_COLUMN)));
				temp.setDataFine(Date.valueOf(rs.getString(DATAFINE_COLUMN)));
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}
}
