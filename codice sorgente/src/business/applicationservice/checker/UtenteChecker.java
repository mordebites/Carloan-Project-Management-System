package business.applicationservice.checker;

//import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.FormatoPasswordNonValidoException;
import business.applicationservice.exception.FormatoUsernameNonValidoException;


/**
 * Classe per il controllo del formato dei valori di un utente.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class UtenteChecker implements Checker {
	private final String USERNAME_FORMAT = "^[a-zA-Z0-9_-]{3,15}$";
	/*
	^                 # start-of-string
	(?=.*[0-9])       # a digit must occur at least once
	(?=.*[a-zA-Z])    # a word must occur at least once
	(?=.*[@#$%^&+=])  # a special character must occur at least once
	(?=\S+$)          # no whitespace allowed in the entire string
	.{8,20}           # anything, at least eight places until 20
	$                 # end-of-string
	*/
	private final String PASSWORD_FORMAT = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
	
	static Logger log = Logger.getLogger(UtenteChecker.class.getName());
	
	public void check(List<Object> values) throws Exception{
		checkUsernameFormat((String)values.get(0));
		checkPasswordFormat((String)values.get(1));
	}

	private void checkUsernameFormat(String username) throws FormatoUsernameNonValidoException {
		if (!username.matches(USERNAME_FORMAT)) {
			throw new FormatoUsernameNonValidoException();
		}
	}
	
	private void checkPasswordFormat(String password) throws FormatoPasswordNonValidoException {		
		if (!password.matches(PASSWORD_FORMAT)) {
			throw new FormatoPasswordNonValidoException();
		}
	}
	
	/*
	public static void main(String[] args) {
		List<Object> pippo = new ArrayList<Object>();
		pippo.add("SimoneMarzulli");
		pippo.add("pr0Ova#_3");
		
		Checker ch = new UtenteChecker();
		ch.check(pippo);
	}
	*/
}
