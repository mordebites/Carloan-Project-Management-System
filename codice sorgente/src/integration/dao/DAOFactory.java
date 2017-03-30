package integration.dao;

import org.apache.log4j.Logger;

/**
 * Implementazione del pattern factory method per la gestione delle classi DAO.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class DAOFactory {

	private static final String DAO_PATH = "integration.dao.";
	
	static Logger log = Logger.getLogger(DAOFactory.class.getName());
	
	/**
	 * Restituisce un DAO partendo dal nome del dao inserito come parametro
	 * sotto forma di stringa.
	 * @param daoName il nome del dao di cui si vuole sapere l'oggetto dao
	 * @return l'oggetto di tipo dao
	 */
	
	public static DAO getDAOEntity(String daoName) {
		String finalDAOName = DAO_PATH + daoName;
		Class<?> daoClass = getDAOClass(finalDAOName);
		return getDAOInstance(daoClass);
	}
	
	private static Class<?> getDAOClass(String daoCanonicalName) {
        Class<?> daoClass = null;

        try {
            daoClass = Class.forName(daoCanonicalName);
        } catch (ClassNotFoundException e) {
			log.error("La classe non e' stata trovata!");
        }
        return daoClass;
    }
	
	/*
	 * Restituisce una istanza di tipo dao partendo dalla classe inserita come
	 * parametro della funzione.
	 */
	private static DAO getDAOInstance(Class<?> daoClass) {
		DAO daoInstance = null;

		try {
			daoInstance = (DAO) daoClass.newInstance();
		} catch (InstantiationException e) {
			log.error("Errore di istanziazione");
		} catch (IllegalAccessException e) {
			log.error("Accesso non consentito!");
		}

		return daoInstance;
	}

}
