package business.transfer;

import business.applicationservice.exception.ChiaveNonDisponibileException;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe per l'utilizzo del transfer object parameter.
 * Ci servira' per scambiare informazioni attraverso diversi tier all'interno
 * del sistema. Associa un oggetto ad una chiave di tipo stringa.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Parameter {
	
    private Map<String, Object> dizionario = new HashMap<>();

    /**
     * Inserisce all'interno di una mappa di valori l'oggetto con la rispettiva chiave
     * @param key la chiave identificativa sotto forma di stringa
     * @param value il valore oggetto
     */
    public void setValue(String key, Object value) {
        dizionario.put(key, value);
    }

    /**
     * Restituisce l'oggetto partendo dalla chiave in formato stringa ottenuta come parametro
     * @param key la chiave identificativa
     * @return l'oggetto identificato dalla chiave
     * @throws ChiaveNonDisponibileException
     */
    public Object getValue(String key) throws ChiaveNonDisponibileException {

        Object result = null;

        if (dizionario.containsKey(key)) {
            result = dizionario.get(key);
        } else {
            throw new ChiaveNonDisponibileException();
        }

        return result;
    }

    /**
     * Controlla se e' presente una determinata chiave all'interno della struttura dati.
     * @param key la chiave da cercare
     * @return valore che ci indica se la chiave e' presente nella struttura dati.
     */
    public boolean containsKey(String key) {
        return dizionario.containsKey(key);
    }
}
