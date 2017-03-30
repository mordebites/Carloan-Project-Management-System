package business.applicationservice.checker;

import java.util.List;

import business.applicationservice.exception.FormatoTipoFasciaNonValidoException;

/**
 * Classe per il controllo del formato dei valori di una fascia.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class FasciaChecker implements Checker {
	private final String TYPE_FORMAT = "([a-zA-Z]){1,15}";
	
	public void check(List<Object> values) throws Exception {
		checkTipo((String)values.get(0));
		
		// controllo sul costo chilometrico
		CheckerUtility.checkQuantitaPositiva((float)values.get(1)); 
		//costo giorn. lim.
		CheckerUtility.checkQuantitaPositiva((float)values.get(2));
		//costo sett. lim.
		CheckerUtility.checkQuantitaPositiva((float)values.get(3));
		//costo giorno illim.
		CheckerUtility.checkQuantitaPositiva((float)values.get(4));
		//costo sett. illim.
		CheckerUtility.checkQuantitaPositiva((float)values.get(5));
	}
	
	private void checkTipo(String tipo) throws FormatoTipoFasciaNonValidoException { //controllo sul tipo, cioe' sul nome della fascia
		if (!tipo.matches(TYPE_FORMAT)) {
			throw new FormatoTipoFasciaNonValidoException();
		}
		
	}
}
