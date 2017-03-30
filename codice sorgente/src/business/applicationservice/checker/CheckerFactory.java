package business.applicationservice.checker;

import org.apache.log4j.Logger;

import business.entity.Entity;

/**
 * Implementazione del pattern factory method per la gestione delle classi Checker.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class CheckerFactory {
	private static final String CHECKER_PATH = "business.applicationservice.checker.";
	
	static Logger log = Logger.getLogger(CheckerFactory.class.getName());
	
	/**
	 * Metodo per creare l'istanza di tipo checker partendo dalla classe entity
	 * inserita come parametro.
	 * @param entity la classe di cui si vuole avere il checker
	 * @return l'oggetto checker costruito
	 */
	public static Checker buildInstance(Class<? extends Entity> entity) {
		Checker ch = null;
		
		try {
			String checker_name = CHECKER_PATH + entity.getSimpleName() + "Checker";
			Class<?> chClass = Class.forName(checker_name);
			
			ch = (Checker) chClass.newInstance();
		} catch (ClassNotFoundException e) {
			log.error("La classe non e' stata trovata!");
        } catch (InstantiationException e) {
        	log.error("Problema di istanziazione");
        } catch (IllegalAccessException e) {
        	log.error("Accesso non consentito");
        }
		
		return ch;
	}
}
