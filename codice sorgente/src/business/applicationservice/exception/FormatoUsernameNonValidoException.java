package business.applicationservice.exception;

/**
 * Eccezione lanciata si inserisce un username in un formato non valido.
 * 
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class FormatoUsernameNonValidoException extends Exception {
	private static final long serialVersionUID = 1L;

	public FormatoUsernameNonValidoException() {
		
	}
	
	public FormatoUsernameNonValidoException(String message) {
		super(message);
	}
}
