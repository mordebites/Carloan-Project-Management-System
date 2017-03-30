package integration.dao;

import java.util.List;

import business.transfer.Parameter;

/**
 * Interfaccia DAO che contiene anche le firme dei metodi per la creazione
 * e la lettura
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */

public interface DAOCR<Entity> extends DAO {
	public String create(Entity entity) throws Exception;
	public Entity read(Parameter par);
	public List<Entity> readAll(Parameter par);
}

