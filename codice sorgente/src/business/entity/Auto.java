package business.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Auto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Auto implements Entity {
	
	private String id;
	private Modello modello;
	private String targa;
	private int chilometraggio;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SimpleStringProperty getIdProperty(){
		return new SimpleStringProperty(id);
	}
	
	public Modello getModello() {
		return modello;
	}
	public void setModello(Modello modello) {
		this.modello = modello;
	}
	public SimpleStringProperty getModelloProperty(){
		return new SimpleStringProperty(modello.getId());
	}
	
	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}
	public SimpleStringProperty getTargaProperty(){
		return new SimpleStringProperty(targa);
	}
	
	public int getChilometraggio() {
		return chilometraggio;
	}
	public void setChilometraggio(int chilometraggio) {
		this.chilometraggio = chilometraggio;
	}
	public SimpleStringProperty getChilometraggioProperty(){
		return new SimpleStringProperty(chilometraggio+"");
	}
}
