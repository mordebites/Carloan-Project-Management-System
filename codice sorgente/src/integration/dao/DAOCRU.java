package integration.dao;

/**
 * Interfaccia DAO che contiene la firma del metodo per l'aggiornamento
 * delle istanze del database.
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */

public interface DAOCRU<Entity> extends DAOCR<Entity>{
	public void update(Entity entity) throws Exception;
}

