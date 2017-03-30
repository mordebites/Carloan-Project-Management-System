package business.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Modello.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Modello implements Entity {

	private String id;
	private String nome;
	private Fascia fascia;
	
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
	
	
	public Fascia getFascia() {
		return fascia;
	}
	public void setFascia(Fascia fascia) {
		this.fascia = fascia;
	}
	public SimpleStringProperty getFasciaProperty(){
		return new SimpleStringProperty(fascia.getId());
	}
	
	
}
