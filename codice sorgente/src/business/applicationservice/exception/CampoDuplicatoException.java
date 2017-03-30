package business.applicationservice.exception;

/**
 * Eccezione lanciata quando viene inserito un campo gia' presente
 * nel sistema di persistenza dei dati. Ad esempio quando si tratta di
 * campi che hanno impostato il valore a unique.
 * 
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class CampoDuplicatoException extends Exception {
	private static final long serialVersionUID = 1L;
}

