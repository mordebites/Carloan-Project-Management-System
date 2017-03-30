package business.applicationservice.exception;

/**
 * Eccezione lanciata quando si inserisce una password nel formato non valido.
 * 
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class FormatoPasswordNonValidoException extends Exception {
	private static final long serialVersionUID = 1L;

	public FormatoPasswordNonValidoException() {
		
	}
	
	public FormatoPasswordNonValidoException(String message) {
		super(message);
	}
}
