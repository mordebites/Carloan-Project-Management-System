package utility;

import business.entity.Agenzia;
import business.entity.TipoUtente;
import business.entity.Utente;

/**
 * Classe che ci servira' per gestire le informazioni dell'utente
 * loggato attualmente nel programma. Queste informazioni saranno
 * utilizzate da vari application service. L'attributo che conserva l'informazione
 * sara' inizializzato al momento dell'autenticazione.
 * @author Simone Marzulli e Morena De Liddo
 */
public class SessionManager {
	
	private static Utente loggedUser = null;
	
	public static void setLoggedUser(Utente utente) {
		loggedUser = utente;
	}
	
	public static Agenzia getAgenzia() {
		return loggedUser.getAgenzia();
	}
	
	public static TipoUtente getTipoUtente() {
		return loggedUser.getTipo();
	}
	
	public static String getUsername() {
		return loggedUser.getUsername();
	}
}
