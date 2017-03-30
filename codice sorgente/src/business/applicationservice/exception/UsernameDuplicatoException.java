package business.applicationservice.exception;

/**
 * Eccezione lanciata quando viene inserito un username gia' presente
 * nel sistema di persistenza dei dati.
 * 
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class UsernameDuplicatoException extends Exception {
	private static final long serialVersionUID = 1L;
}
