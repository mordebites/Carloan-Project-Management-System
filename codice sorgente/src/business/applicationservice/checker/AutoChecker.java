package business.applicationservice.checker;

import java.util.List;

import business.applicationservice.exception.TargaNonValidaException;

/**
 * Classe per il controllo del formato dei valori di un'auto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class AutoChecker implements Checker {
	private static final String PLATE_FORMAT = "[A-Z]{2}[0-9]{3}[A-Z]{2}";
	
	public void check(List<Object> values) throws Exception{
		checkTarga((String) values.get(0));
		
		// controlla che il chilometraggio sia un numero positivo
		CheckerUtility.checkQuantitaPositiva((int) values.get(1));
	}
	
	//controllo sulla targa
	private void checkTarga(String targa) throws TargaNonValidaException {

		if (targa == null || !targa.matches(PLATE_FORMAT)){
			throw new TargaNonValidaException();
		}
	}
	
	/*
	public static void main(String[] args) {
		List<Object> pippo = new ArrayList<Object>();
		pippo.add("ST000CZ");
		
		Checker ch = new AutoChecker();
		ch.check(pippo);
	}
	*/
}
