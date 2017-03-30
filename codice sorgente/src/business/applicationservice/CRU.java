package business.applicationservice;

import java.util.List;

import business.transfer.Parameter;
/**
 * Interfaccia CRU che definisce i metodi 
 * per la creazione, lettura e aggiornamento dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */
public interface CRU<Entity> {
	public void create(Parameter par) throws Exception;
	public Entity read(Parameter par);
	public List<Entity> readAll();
	public void update(Parameter par) throws Exception;
}
