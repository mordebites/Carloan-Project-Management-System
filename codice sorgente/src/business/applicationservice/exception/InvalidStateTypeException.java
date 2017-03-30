package business.applicationservice.exception;

/**
 * Eccezione lanciata quando si identifica uno stato per le auto non valido.
 * 
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class InvalidStateTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidStateTypeException() {
		
	}
	
	public InvalidStateTypeException(String message) {
		super(message);
	}
}
