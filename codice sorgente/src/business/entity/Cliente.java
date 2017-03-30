package business.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Cliente.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Cliente implements Entity {
	
	private String id;
	private String nome;
	private String cognome;
	private String numTel;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SimpleStringProperty getIdProperty(){
		return new SimpleStringProperty(id);
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public SimpleStringProperty getNomeProperty(){
		return new SimpleStringProperty(nome);
	}
	
	
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public SimpleStringProperty getCognomeProperty(){
		return new SimpleStringProperty(cognome);
	}
	
	
	public String getNumTel() {
		return numTel;
	}
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	public SimpleStringProperty getNumTelProperty(){
		return new SimpleStringProperty(numTel);
	}
}
