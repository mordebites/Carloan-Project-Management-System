package business.entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Fascia.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Fascia implements Entity {
	
	private String id;
	private String tipo;
	private String descrizione;
	private float costoKm;
	private float costoGiornoLim;
	private float costoSettimanaLim;
	private float costoGiornoIllim;
	private float costoSettimanaIllim;
	
	public float getCostoGiornoLim() {
		return costoGiornoLim;
	}
	public void setCostoGiornoLim(float costoGiornoLim) {
		this.costoGiornoLim = costoGiornoLim;
	}
	public SimpleFloatProperty getcostoGiornoLimProperty(){
		return new SimpleFloatProperty(costoGiornoLim);
	}
	
	
	public float getCostoSettimanaLim() {
		return costoSettimanaLim;
	}
	public void setCostoSettimanaLim(float costoSettimanaLim) {
		this.costoSettimanaLim = costoSettimanaLim;
	}
	public SimpleFloatProperty getcostoSettimanaLimProperty(){
		return new SimpleFloatProperty(costoSettimanaLim);
	}
	
	
	public float getCostoGiornoIllim() {
		return costoGiornoIllim;
	}
	public void setCostoGiornoIllim(float costoGiornoIllim) {
		this.costoGiornoIllim = costoGiornoIllim;
	}
	public SimpleFloatProperty getcostoGiornoIllimProperty(){
		return new SimpleFloatProperty(costoGiornoIllim);
	}
	
	
	public float getCostoSettimanaIllim() {
		return costoSettimanaIllim;
	}
	public void setCostoSettimanaIllim(float costoSettimanaIllim) {
		this.costoSettimanaIllim = costoSettimanaIllim;
	}
	public SimpleFloatProperty getcostoSettimanaIllimProperty(){
		return new SimpleFloatProperty(costoSettimanaIllim);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SimpleStringProperty getIdProperty(){
		return new SimpleStringProperty(id);
	}
	
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public SimpleStringProperty getTipoProperty(){
		return new SimpleStringProperty(tipo);
	}
	
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public SimpleStringProperty getDescrizioneProperty(){
		return new SimpleStringProperty(descrizione);
	}

	
	public float getCostoKm() {
		return costoKm;
	}
	public void setCostoKm(float costoKm) {
		this.costoKm = costoKm;
	}
	public SimpleFloatProperty getCostoKmProperty(){
		return new SimpleFloatProperty(costoKm);
	}
}
