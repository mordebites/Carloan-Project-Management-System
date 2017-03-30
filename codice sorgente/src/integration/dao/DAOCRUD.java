package integration.dao;

/**
 * Interfaccia DAO che contiene la firma del metodo per l'eliminazione
 * delle istanze del database.
 * @author Morena De Liddo e Simone Marzulli
 *
 * @param <Entity>
 */

public interface DAOCRUD<Entity> extends DAOCRU<Entity> {
	public void delete(String id);
}
