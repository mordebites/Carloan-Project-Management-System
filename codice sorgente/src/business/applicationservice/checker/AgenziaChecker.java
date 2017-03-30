package business.applicationservice.checker;

//import java.util.ArrayList;
import java.util.List;

/**
 * Classe per il controllo del formato dei valori di una agenzia.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class AgenziaChecker implements Checker {
	
	public void check(List<Object> values) throws Exception {
		//controllo sul nome
		CheckerUtility.checkNome((String)values.get(0));
		//controllo sul numero di telefono
		CheckerUtility.checkNumTel((String) values.get(1));
		//controllo sull'indirizzo
		CheckerUtility.checkIndirizzo((String) values.get(2));
	}
	
	/*
	public static void main(String[] args) {
		List<Object> pippo = new ArrayList<Object>();
		pippo.add("Simone Marzulli Co.");
		pippo.add("(+39)0997796886");
		pippo.add("via c. battisti 637");
		pippo.add(0);
		
		Checker ch = new AgenziaChecker();
		ch.check(pippo);
	}
	*/
}
