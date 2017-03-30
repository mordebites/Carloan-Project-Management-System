package business.applicationservice.checker;

//import java.util.ArrayList;
import java.util.List;

/**
 * Classe per il controllo del formato dei valori di un cliente.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ClienteChecker implements Checker {
	public void check(List<Object> values) throws Exception {
		//controllo sul nome
		CheckerUtility.checkNome((String)values.get(0));
		
		// stesso metodo di nome anche per controllo cognome
		CheckerUtility.checkNome((String)values.get(1));
		
		//controllo sul numero di telefono
		CheckerUtility.checkNumTel((String) values.get(2));
	}
	
	/*
	public static void main(String[] args) {
		List<Object> pippo = new ArrayList<Object>();
		pippo.add("Simone");
		pippo.add("d'marzulli");
		pippo.add("0997796886");
		
		Checker ch = new ClienteChecker();
		ch.check(pippo);
	}
	*/
}
