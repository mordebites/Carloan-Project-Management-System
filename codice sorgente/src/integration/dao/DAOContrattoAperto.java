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
import business.entity.Base;
import business.entity.Chilometraggio;
import business.entity.Cliente;
import business.entity.Contratto;
import business.entity.Fascia;
import business.entity.ModNoleggio;
import business.transfer.Parameter;

/**
 * Classe che implementa il pattern architetturale Data Access Object per
 * la gestione dei contratti aperti nel sistema di persistenza dei dati.
 * Implementa l'interfaccia CRUD che contiene metodi per la creazione, lettura
 * aggiornamento ed eliminazione di contratti aperti.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class DAOContrattoAperto extends MySQLDAO<Contratto> implements DAOCRUD<Contratto> {

	private static final String INSERT_QUERY = "INSERT INTO ContrattiAperti(DataInizio, DataLimite, Acconto, "
											 + "BaseNoleggio, Chilometraggio, FasciaMacchina, LuogoRest, "
											 + "Agenzia, Cliente, ContrattoAperto) "
											 + "VALUES ('?', '?', '?', '?', '?', '?', '?', '?', '?', '?')"; 
	private static final String UPDATE_QUERY = "UPDATE ContrattiAperti SET DataInizio = '?', DataLimite = '?', "
											 + "Acconto = '?', BaseNoleggio = '?', Chilometraggio = '?', "
											 + "FasciaMacchina = '?', LuogoRest = '?', Agenzia = '?', "
											 + "Cliente = '?', Auto = '?', ContrattoAperto = '?' "
											 + "WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM ContrattiAperti WHERE ID = '?'";
	private static final String READ_QUERY = "SELECT ID, DataInizio, DataLimite, Acconto, "
											 + "BaseNoleggio, Chilometraggio, FasciaMacchina, "
											 + "LuogoRest, Agenzia, Cliente, Auto, ContrattoAperto "
											 + "FROM ContrattiAperti WHERE ID = '?'";
	private static final String READALL_QUERY = "SELECT ID, DataInizio, DataLimite, Acconto, "
			 								  + "BaseNoleggio, Chilometraggio, FasciaMacchina, "
			 								  + "LuogoRest, Agenzia, Cliente, Auto, ContrattoAperto "
			 								  + "FROM ContrattiAperti";
	private static final String READ_ID = "SELECT LAST_INSERT_ID() AS UltimoID";
	
	
	private static final String ID_COLUMN = "ID";
	private static final String DATA_INIZIO_COLUMN = "DataInizio";
	private static final String DATA_LIMITE_COLUMN = "DataLimite";
	private static final String ACCONTO_COLUMN = "Acconto";
	private static final String BASE_COLUMN = "BaseNoleggio";
	private static final String CHILOMETRAGGIO_COLUMN = "Chilometraggio";
	private static final String FASCIA_MACCHINA_COLUMN = "FasciaMacchina";
	private static final String LUOGO_REST_COLUMN = "LuogoRest";
	private static final String AGENZIA_COLUMN = "Agenzia";
	private static final String CLIENTE_COLUMN = "Cliente";
	private static final String AUTO_COLUMN = "Auto";
	private static final String CONTRATTOAPERTO_COLUMN = "ContrattoAperto";
	private static final String ULTIMOID_COLUMN = "UltimoID";
	
	private static final int MYSQL_ERROR_DUPLICATED_ENTRY = 1062;
	
	static Logger log = Logger.getLogger(DAOContrattoAperto.class.getName());
	
	public DAOContrattoAperto() throws Exception {
		super();
	}
	
	/**
	 * Metodo per la creazione di una istanza nel database contenente informazioni
	 * su un contratto aperto specificata come parametro.
	 * 
	 * @param contratto contiene le informazioni sul contratto necessarie per creare l'istanza nel database
	 * @return una stringa contenente l'id del contratto aperto dell'istanza appena creata nel database.
	 * @throws Exception 
	 */
	public String create(Contratto contratto) throws Exception{
		String[] values = new String[10];
		values[0] =  contratto.getDataInizio().toString();
		values[1] = contratto.getDataLimite().toString();
		values[2] = Float.toString(contratto.getAcconto());
		values[3] = contratto.getModNoleggio().getBase().name();
		values[4] = contratto.getModNoleggio().getKm().name();
		values[5] = contratto.getFascia().getId();
		values[6] = contratto.getLuogoRest().getId();
		values[7] = contratto.getAgenzia().getId();
		values[8] = contratto.getCliente().getId();
		
		String aperto = "0";
		if (contratto.isContrattoAperto()) {
			aperto = "1";
		}
		values[9] = aperto;
		
		String insertQuery = QueryStringReplacer.replaceValues(INSERT_QUERY, values);
		
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
	 * @param par transfer object contente l'id dell'agenzia da cercare nel database
	 * @return un oggetto di tipo agenzia con le informazioni prese dal database
	 */	
	public Contratto read(Parameter par){
		String readQuery = READ_QUERY;
		String id = null;
		
		try{
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e){
			log.error("Chiave non disponibile all'interno del transfer object!");
		}
		
		//TODO usare il string replacer
		readQuery = readQuery.replaceFirst("[?]", id);
		
		ResultSet readQueryResultSet = null;
		try {
			readQueryResultSet = connector.executeReadQuery(readQuery);
		} catch (SQLException e) {
			log.error("Errore nella query!");
		}
		List<Contratto> list = RStoList(readQueryResultSet);
		
		Contratto tempCont = null;
		
		if(!list.isEmpty()) {
			tempCont = list.get(0);
		}
		
		return tempCont;
	}
	
	/**
	 * Una lista contenente tutti i contratti aperti presenti nel database.
	 * 
	 * @param par transfer object contenente determinate informazioni da filtrare 
	 * per la restituzione di tutti i contratti aperti. Viene settato a null se si
	 * vogliono tutte le informazioni su tutti i contratti aperti
	 * @return una lista contente tutti i contratti aperti presenti nel database
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
	 * Aggiorna le informazioni su un contratto aperto presente nel database.
	 * 
	 * @param contratto l'oggetto di tipo contratto contenente i dati aggiornati
	 * @throws Exception
	 */
	public void update(Contratto contratto) throws Exception {		
		String[] values = new String[9];
		values[0] =  contratto.getDataInizio().toString();
		values[1] = contratto.getDataLimite().toString();
		values[2] = Float.toString(contratto.getAcconto());
		values[3] = contratto.getModNoleggio().getBase().name();
		values[4] = contratto.getModNoleggio().getKm().name();
		values[5] = contratto.getFascia().getId();
		values[6] = contratto.getLuogoRest().getId();
		values[7] = contratto.getAgenzia().getId();
		values[8] = contratto.getCliente().getId();
		
		String updateQuery = QueryStringReplacer.replaceValues(UPDATE_QUERY, values);
		
		Auto auto = contratto.getAuto();
		String autoId = null;
		if (auto != null) {
			autoId = auto.getId();
			updateQuery = updateQuery.replaceFirst("[?]", autoId); //togli apicetti
		} else {
			autoId = "NULL";
			updateQuery = updateQuery.replaceFirst("\\'\\?\\'", autoId); //togli apicetti
		}
		
		updateQuery = updateQuery.replaceFirst("[?]", contratto.isContrattoAperto() ? "1" : "0");
		updateQuery = updateQuery.replaceFirst("[?]", contratto.getId());
		
		try {
			connector.executeUpdateQuery(updateQuery);
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_ERROR_DUPLICATED_ENTRY) {
				throw new CampoDuplicatoException();
			}
		}
	}
	
	/**
	 * Elimina un'istanza di contratto aperto dal database in base all'id passato come parametro.
	 * 
	 * @param id indice univoco dell'istanza da cancellare dal database
	 */
	public void delete(String id) {
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
	 * tipo contratto, basandosi sul nome delle colonne presenti nel sistema di persistenza dei dati.	
	 */
	@SuppressWarnings("unchecked")
	private List<Contratto> RStoList(ResultSet rs){
		List<Contratto> risultato = new LinkedList<Contratto>();
		DAOCRUD<Fascia> daoFascia = (DAOCRUD<Fascia>) DAOFactory.getDAOEntity("DAOFascia");
		DAOCRUD<Agenzia> daoAgenzia = (DAOCRUD<Agenzia>) DAOFactory.getDAOEntity("DAOAgenzia");
		DAOCRU<Cliente> daoCliente = (DAOCRU<Cliente>) DAOFactory.getDAOEntity("DAOCliente");
		DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
		Parameter par = new Parameter();
		Contratto temp = null;
		ModNoleggio mod = null;
		
		try{
			while(rs.next()){
				temp = new Contratto();
				
				String id = rs.getString(ID_COLUMN);
				temp.setId(id);
				
				Date dataInizio = Date.valueOf(rs.getString(DATA_INIZIO_COLUMN)); 
				temp.setDataInizio(dataInizio);
				
				Date dataLimite = Date.valueOf(rs.getString(DATA_LIMITE_COLUMN)); 
				temp.setDataLimite(dataLimite);
				
				float acconto = Float.parseFloat(rs.getString(ACCONTO_COLUMN).trim());
				temp.setAcconto(acconto);
				
				mod = new ModNoleggio();
				mod.setBase(Base.valueOf(rs.getString(BASE_COLUMN)));
				mod.setKm(Chilometraggio.valueOf(rs.getString(CHILOMETRAGGIO_COLUMN)));
				temp.setModNoleggio(mod);
				
				String idFascia = rs.getString(FASCIA_MACCHINA_COLUMN);
				par.setValue("id", idFascia);
				Fascia fascia = daoFascia.read(par);
				temp.setFascia(fascia);
				
				String idLuogoRest = rs.getString(LUOGO_REST_COLUMN);
				par.setValue("id", idLuogoRest);
				Agenzia luogoRest = daoAgenzia.read(par);
				temp.setLuogoRest(luogoRest);
				
				String idAgenzia = rs.getString(AGENZIA_COLUMN);
				par.setValue("id", idAgenzia);
				Agenzia agenzia = daoAgenzia.read(par);
				temp.setAgenzia(agenzia);
				
				String idCliente = rs.getString(CLIENTE_COLUMN);
				par.setValue("id", idCliente);
				Cliente cliente = daoCliente.read(par);
				temp.setCliente(cliente);
				
				
				boolean b = rs.getBoolean(CONTRATTOAPERTO_COLUMN);
				temp.setContrattoAperto(b);
				
				String idAuto = rs.getString(AUTO_COLUMN);
				Auto auto = null;
				
				if (idAuto != null) {
					par.setValue("id", idAuto);
					auto = daoAuto.read(par);
				}
				
				temp.setAuto(auto);
				
				risultato.add(temp);
			}
		} catch (SQLException e) {
			log.error("Errore nella query SQL!");
        }
		return risultato;
	}
}
