package business.applicationservice.checker;

import java.util.List;

/**
 * Interfaccia per il controllo dei valori dei business object gestiti dal sistema.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public interface Checker {
	public abstract void check(List<Object> values) throws Exception;
}
