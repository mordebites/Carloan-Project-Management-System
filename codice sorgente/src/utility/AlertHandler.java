package utility;

import java.util.HashMap;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import business.applicationservice.exception.AutoNonAssegnabileException;
import business.applicationservice.exception.AutoNonValidaException;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.exception.ConnessioneAlDataBaseException;
import business.applicationservice.exception.ContrattoChiusoException;
import business.applicationservice.exception.DataNonValidaException;
import business.applicationservice.exception.FormatoPasswordNonValidoException;
import business.applicationservice.exception.FormatoTipoFasciaNonValidoException;
import business.applicationservice.exception.FormatoUsernameNonValidoException;
import business.applicationservice.exception.IndirizzoNonValidoException;
import business.applicationservice.exception.LunghezzaNomeNonValidaException;
import business.applicationservice.exception.NessunElementoSelezionatoException;
import business.applicationservice.exception.NoleggioIniziatoException;
import business.applicationservice.exception.QuantitaNegativaException;
import business.applicationservice.exception.TargaNonValidaException;
import business.applicationservice.exception.TelefonoNonValidoException;
import business.applicationservice.exception.UsernameDuplicatoException;

public class AlertHandler {
	private static HashMap<Class<?>, String> alMap = new HashMap<Class<?>, String>();
	
	static {
		alMap.put(ConnessioneAlDataBaseException.class, "Si e' verificato un errore di connessione con il database!");
		alMap.put(CampoDuplicatoException.class, "Il campo inserito e' gia' esistente nel database!");
		alMap.put(ContrattoChiusoException.class, "Il contratto selezionato e' gia' chiuso!");
		alMap.put(AutoNonValidaException.class, "L'auto non e' valida!");
		alMap.put(DataNonValidaException.class, "La data non e' valida!");
		alMap.put(IndirizzoNonValidoException.class, "L'indirizzo non e' valido!");
		alMap.put(FormatoPasswordNonValidoException.class, "La password inserita non e' nel formato valido!");
		alMap.put(TelefonoNonValidoException.class, "Il numero di telefono inserito non e' valido!");
		alMap.put(TargaNonValidaException.class, "La targa inserita non e' valida!");
		alMap.put(FormatoTipoFasciaNonValidoException.class, "Il tipo di fascia non e' nel formato valido!");
		alMap.put(FormatoUsernameNonValidoException.class, "Il formato dello username non e' valido!");
		alMap.put(NoleggioIniziatoException.class, "Il noleggio e' gia' iniziato!");
		alMap.put(LunghezzaNomeNonValidaException.class, "La lunghezza del nome non e' valida!");
		alMap.put(UsernameDuplicatoException.class, "Lo username inserito e' duplicato, inserirne uno nuovo!");
		alMap.put(QuantitaNegativaException.class, "Inserire una quantita' positiva!");
		alMap.put(AutoNonAssegnabileException.class, "L'auto e' gia' stata assegnata o il contratto e' gia' chiuso");
		alMap.put(NessunElementoSelezionatoException.class, "Si prega di selezionare un elemento per "
														  + "completare l'operazione");
		alMap.put(NullPointerException.class, "Si e' verificato uno dei seguenti errori:\n "
											+ "- Non hai selezionato nessun elemento\n"
											+ "- Non hai compilato tutti i campi richiesti");
	}
	
	public static Alert getAlertMessage(Object e, AlertType tipo){
		Class<?> classe = e.getClass();
		
		String msg = "";
		String header = "";
		String title = classe.getSimpleName();
		 if(classe.getSuperclass() == Exception.class || classe.getSuperclass() == RuntimeException.class){
			if(title.compareTo("NullPointerException") == 0){
				title = "Si e' verificato un errore";
			} else {
				title = title.split("Exception")[0];
				String[] sentence = title.split("(?=\\p{Upper})");
				title = "";
				StringBuffer builder = new StringBuffer(title);
				
				for(String word : sentence){
					builder.append(word + " ");
				}
				
				title = builder.toString();
			}
			header = title;
			msg = alMap.get(e.getClass());
		} else {
			title = title + " gestito/a";
			header = title + " con successo!";
			msg = "I dati sono stati correttamente aggiornati nel sistema!";
		}
		
		Alert alert = new Alert(tipo);
		alert.setHeaderText(header);
		alert.setTitle(title);
		alert.setContentText(msg);
		
		return alert;
	}
}
