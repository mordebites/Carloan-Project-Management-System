package business.applicationservice.checker;

import business.applicationservice.exception.IndirizzoNonValidoException;
import business.applicationservice.exception.NomeNonValidoException;
import business.applicationservice.exception.QuantitaNegativaException;
import business.applicationservice.exception.TelefonoNonValidoException;


/**
 * Classe di supporto per i vari checker.
 * Contiene metodi utilizzati da piu' di un checker.
 * 
 * @author Morena De Liddo e Simone Marzulli
 * 
 */
public class CheckerUtility {
	private static final String NAME_FORMAT = "([a-zA-Z]|\\s|\\.|\\'){1,35}";
	private static final String TEL_FORMAT = "(\\(\\+39\\))?[0-9]{10}";
	private static final String ADDRESS_FORMAT = "(?i)(via|piazza|viale|vico) ([a-zA-Z]|\\s|\\.|\\'){1,35} [0-9]{1,3}";
	
	/**
	 * Controlla il formato di un nome inserito come parametro di tipo stringa.
	 * @param nome la stringa da controllare
	 * @throws LunghezzaNomeNonValidaException
	 */
	public static void checkNome(String nome) throws NomeNonValidoException {

		if(!nome.matches(NAME_FORMAT)){
			throw new NomeNonValidoException();
		}
	}
	
	/**
	 * Controlla il formato di un numero di telefono inserito come parametro di tipo stringa.
	 * @param numTel numero di telefono da controllare
	 * @throws LunghezzaNomeNonValidaException
	 */
	public static void checkNumTel(String numTel) throws TelefonoNonValidoException {
		if(!numTel.matches(TEL_FORMAT)) {
			throw new TelefonoNonValidoException();
		}
	}
	

	/**
	 * Controlla il formato di un indirizzo inserito come parametro di tipo stringa.
	 * @param indirizzo l'indirizzo da controllare
	 * @throws LunghezzaNomeNonValidaException
	 */
	public static void checkIndirizzo(String indirizzo) throws IndirizzoNonValidoException{
		if (!indirizzo.matches(ADDRESS_FORMAT)) {
			throw new IndirizzoNonValidoException();
		}
	}
	

	/**
	 * Controlla se una data quantita' presa come parametro di tipo float e' positiva.
	 * @param qta la quantita' da controllare
	 * @throws QuantitaNegativaException
	 */
	public static void checkQuantitaPositiva(float qta) throws QuantitaNegativaException {
		if(qta < 0){
			throw new QuantitaNegativaException();
		}
	}
}
