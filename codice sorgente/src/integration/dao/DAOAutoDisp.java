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
import business.entity.Agenzia;
import business.entity.Auto;
import business.entity.Contratto;
import business.entity.Modello;
import business.entity.PeriodoDisp;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione delle auto disponibili nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di auto disponibili.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOAutoDisp extends MySQLDAO<PeriodoDisp> implements DAOCRUD<PeriodoDisp>{

	/**
	 * Indica che si vuole cercare nel database un periodo di disponibilita'
	 * compatibile con le caratteristiche del contratto ricevute in input.
	 */
	public static final int RICHIESTA_CONTRATTO = 1;
	
	/**
	 * Indica che si vuole cercare nel database un periodo di disponibilita'
	 * compatibile con le caratteristiche del periodo di manutenzione
	 * ricevuto in input.
	 */
	public static final int RICHIESTA_MANUTENZIONE = 2;
	
	/**
	 * Indica che si vuole cercare nel database un periodo di disponibilita'
	 * che abbia l'id passato in input.
	 */
	public static final int RICHIESTA_ID = 3;
	
	/**
	 * Indica che si vuole cercare nel database un periodo di disponibilita'
	 * che termini nella data passata in input. 
	 */
	public static final int RICHIESTA_PREV = 4;
	
	/**
	 * Indica che si vuole cercare nel database un periodo di disponibilita'
	 * che inizi nella data passata in input. 
	 */
	public static final int RICHIESTA_NEXT = 5;
	
	
	private static final String INSERT_QUERY = "INSERT INTO AutoDisponibili(Auto, Agenzia, DataInizio, DataFine) "
											 + "VALUES ('?', '?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE AutoDisponibili SET Auto = '?', Agenzia = '?', "
											 + "DataInizio = '?', DataFine = '?' " 
											 + "WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM AutoDisponibili WHERE ID = '?'";
	private static final String READ_PREV = "SELECT AutoDisponibili.ID AS ID, Auto, " 
										  + "Agenzia, DataInizio, "
										  + "DataFine FROM AutoDisponibili, Auto "
										  + "WHERE AutoDisponibili.Auto = Auto.ID AND "
										  + "AutoDisponibili.Auto = '?' "
								   		  + "AND Agenzia = '?' AND DataFine = '?'";
	private static final String READ_NEXT = "SELECT AutoDisponibili.ID AS ID, Auto, "
										  + "Agenzia, DataInizio, "
										  + "DataFine FROM AutoDisponibili, Auto "
										  + "WHERE AutoDisponibili.Auto = Auto.ID AND "
										  + "AutoDisponibili.Auto = '?' "
								   		  + "AND Agenzia = '?' AND DataInizio = '?'";
	private static final String READCONTRATTO_QUERY = "SELECT AutoDisponibili.ID AS ID, Auto, "
													+ "Modelli.Nome AS NomeModello, Agenzia, DataInizio, "
													+ "DataFine FROM AutoDisponibili, Auto, Modelli "
													+ "WHERE AutoDisponibili.Auto = Auto.ID AND "
													+ "Auto.Modello = Modelli.ID AND Fascia = '?' "
										   			+ "AND Agenzia = '?' AND DataInizio <= '?' "
										   			+ "AND (DataFine IS NULL OR DataFine >= '?')";
	private static final String READMANUT_QUERY = "SELECT ID, Auto, Agenzia, DataInizio, DataFine "
												+ "FROM AutoDisponibili "
												+ "WHERE Auto = '?' AND Agenzia = '?' AND DataInizio <= '?' AND (DataFine >= '?' OR DataFine IS NULL)";
	private static final String READ_QUERY = "SELECT ID, Auto, Agenzia, DataInizio, DataFine "
										   + "FROM AutoDisponibili WHERE ID = '?'";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	
	private static final String ID_COLUMN = "ID";
	private static final String AUTO_COLUMN = "Auto";
	private static final String AGENZIA_COLUMN = "Agenzia";
	private static final String DATAINIZIO_COLUMN = "DataInizio";
	private static final String DATAFINE_COLUMN = "DataFine";
	private static final String NOMEMODELLO_COLUMN = "NomeModello";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOAutoDisp.class.getName());
	
	public DAOAutoDisp() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su una periodo di disponibilita' specificata come parametro.
	 * 
	 * @param disp contiene le informazioni sul periodo necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del periodo dell'istanza appena creata nel database.
	 * @throws Exception
	 */
	public String create(PeriodoDisp disp) throws Exception {
		String[] values = new String[3];
		values[0] = disp.getAuto().getId();
		values[1] = disp.getAgenzia().getId();
		values[2] = disp.getDataInizio().toString();
		
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);
		
		Date dFine = disp.getDataFine();
		String dataFine = null;
		if (dFine != null) {
			dataFine = dFine.toString();
			insertQuery = insertQuery.replaceFirst("[?]", dataFine); //togli apicetti
		} else {
			dataFine = "NULL";
			insertQuery = insertQuery.replaceFirst("\\'\\?\\'", dataFine); //togli apicetti
		}
		
		
		try {
			connector.executeUpdateQuery(insertQuery);
		} catch (SQLException e1) {
			if (e1.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
			
			log.error("Errore nella query SQL!");
		}
		
		String periodoDispId = null;
				
		try {
			ResultSet rsId = connector.executeReadQuery(READ_ID);
			
			rsId.next();
			periodoDispId = (String) rsId.getString(ULTIMOID_COLUMN);
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
		}
		
		return periodoDispId;
	}
	
	
	/**
	 * Legge una istanza dal database contente informazioni
	 * sull'auto, in base al tipo di richiesta specificato,
	 * che puo' essere in base all'id, una richiesta per la gestione
	 * della manutenzione e una richiesta per un periodo precedente o
	 * successivo ad una certa data.
	 * 
	 * @param par transfer object contente l'id dell'auto da cercare nel database
	 * @return un oggetto di tipo auto disponibile con le informazioni prese dal database
	 */	
	@SuppressWarnings("static-access")
	public PeriodoDisp read(Parameter par){
		PeriodoDisp disp = null;
		String readQuery = null;
		Auto auto = null;
		Agenzia agenzia = null;
		String dataInizio = null;
		String dataFine = null;
		int tipoRichiesta = 0;
		String[] values;
		
		try{
			tipoRichiesta = (Integer) par.getValue("tipoRichiesta");
			
			if(tipoRichiesta == RICHIESTA_MANUTENZIONE){
				auto = (Auto) par.getValue("auto");
				agenzia = (Agenzia) par.getValue("agenzia");
				dataInizio = ((Date) par.getValue("dataInizio")).toString();
				
				Date finale = (Date) par.getValue("dataFine");
				if (finale == null) {
					dataFine = "NULL";
				} else {
					dataFine = finale.toString();
				}

				values = new String[4];
				values[0] = auto.getId();
				values[1] = agenzia.getId();
				values[2] = dataInizio;
				values[3] = dataFine;
				
				readQuery = QueryStringReplacer.replaceValues(READMANUT_QUERY, values);
				
			} else if (tipoRichiesta == RICHIESTA_ID){
				
				values =  new String[1];
				values[0] = (String) par.getValue("id");
				readQuery = QueryStringReplacer.replaceValues(READ_QUERY, values);
				
			} else if (tipoRichiesta == RICHIESTA_PREV || tipoRichiesta == RICHIESTA_NEXT) {
				auto = (Auto) par.getValue("auto");
				agenzia = (Agenzia) par.getValue("agenzia");
				values = new String[3];
				values[0] = auto.getId();
				values[1] = agenzia.getId();
				
				if(tipoRichiesta == this.RICHIESTA_PREV){
					values[2] = ((Date) par.getValue("dataFine")).toString();
					readQuery = QueryStringReplacer.replaceValues(READ_PREV, values);
				} else {
					values[2] = ((Date) par.getValue("dataInizio")).toString();
					readQuery = QueryStringReplacer.replaceValues(READ_NEXT, values);
				}
				
			}
		} catch (ChiaveNonDisponibileException e){
			log.error("La chiave non e' disponibile nel transfer object!");
		}

		
		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<PeriodoDisp> list = RStoList(readQueryResultSet);
				
		if(!list.isEmpty()) {
			disp = list.get(0);
		}
		
		return disp;
	
	}
	
	/**
	 * Una lista contenente tutti i periodi di disponibilita' compatibili
	 * con un certo contratto per quanto riguarda l'assegnazione dell'auto.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutte le auto disponibili.
	 * @return una lista contente i periodi di disponibilita' richiesti
	 */
	public List<PeriodoDisp> readAll(Parameter par) {
		Contratto temp = null;
		try {
			temp = (Contratto) par.getValue("Contratto");
		} catch (ChiaveNonDisponibileException e) {
			log.error("La chiave non e' disponibile");
		}
		String[] values = new String[4];
		values[0] = temp.getFascia().getId();
		values[1] = temp.getAgenzia().getId();
		values[2] = temp.getDataInizio().toString();
		values[3] = temp.getDataLimite().toString();

		String readAllQuery = QueryStringReplacer.replaceValues(READCONTRATTO_QUERY, values);
		ResultSet readAllQueryResultSet = null;
		
		try {
			readAllQueryResultSet = connector.executeReadQuery(readAllQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		
		List<PeriodoDisp> list = RStoList(readAllQueryResultSet);
		
		return list;
	}

	/**
	 * Aggiorna le informazioni su un periodo di disponibilita' presente nel database.
	 * 
	 * @param disp l'oggetto di tipo periodo disponibilita' contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(PeriodoDisp disp) throws Exception {
		String[] values = new String[5];
		values[0] = disp.getAuto().getId();
		values[1] = disp.getAgenzia().getId();
		values[2] = disp.getDataInizio().toString();
		values[3] = disp.getDataFine().toString();
		values[4] = disp.getId();
		
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
	 * Elimina un'istanza di periodo disponibilita' dal database in base all'id passato come parametro.
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
	 * tipo periodo disponibilita', basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<PeriodoDisp> RStoList(ResultSet rs){
		List<PeriodoDisp> risultato = new LinkedList<PeriodoDisp>();
		DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
		DAOCRUD<Agenzia> daoAgenzia = (DAOCRUD<Agenzia>) DAOFactory.getDAOEntity("DAOAgenzia");
		Parameter par = new Parameter();
		PeriodoDisp temp = null;
		Auto auto = null;
		Agenzia agenzia = null;
		
		try{
			while(rs.next()){
				temp = new PeriodoDisp();
				
				temp.setId(rs.getString(ID_COLUMN));
				
				par.setValue("id", rs.getString(AUTO_COLUMN));
				auto = daoAuto.read(par);
				
				try {
					Modello mod = auto.getModello();
					mod.setNome(rs.getString(NOMEMODELLO_COLUMN));
					auto.setModello(mod);
				} catch (SQLException e) {
					log.info("Query senza modello!");
				}
				temp.setAuto(auto);
				
				par.setValue("id", rs.getString(AGENZIA_COLUMN));
				agenzia = daoAgenzia.read(par);
				temp.setAgenzia(agenzia);
				
				temp.setDataInizio(Date.valueOf(rs.getString(DATAINIZIO_COLUMN)));
				if(rs.getString(DATAFINE_COLUMN) == null){
					temp.setDataFine(null);
				} else {
					temp.setDataFine(Date.valueOf(rs.getString(DATAFINE_COLUMN)));
				}
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}
}
