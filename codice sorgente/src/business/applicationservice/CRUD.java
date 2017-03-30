package business.applicationservice;

import business.transfer.Parameter;

/**
 * Interfaccia CRUD che estende l'interfaccia CRU
 * aggiungendo il metodo per l'eliminazione dei dati dal sistema.
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */
public interface CRUD<Entity> extends CRU<Entity> {
	public void delete(Parameter par);
}
