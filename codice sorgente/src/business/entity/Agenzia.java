package business.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Agenzia.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Agenzia implements Entity {
	private String id;
	private String nome;
	private String numTel;
	private String indirizzo;
	
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
	
	
	public String getNumTel() {
		return numTel;
	}
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	public SimpleStringProperty getNumTelProperty(){
		return new SimpleStringProperty(numTel);
	}
	
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public SimpleStringProperty getIndirizzoProperty(){
		return new SimpleStringProperty(indirizzo);
	}
}
