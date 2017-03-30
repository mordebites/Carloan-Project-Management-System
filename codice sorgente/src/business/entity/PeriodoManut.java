package business.entity;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity per il periodo di manutenzione.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class PeriodoManut implements Entity {
	private String id;
	private Auto auto;
	private TipoManut tipo;
	private Date dataInizio;
	private Date dataFine;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SimpleStringProperty getIdProperty(){
		return new SimpleStringProperty(id);
	}
	
	
	public Auto getAuto() {
		return auto;
	}
	public void setAuto(Auto auto) {
		this.auto = auto;
	}
	public SimpleStringProperty getAutoProperty(){
		return new SimpleStringProperty(auto.getId());
	}
	
	
	public TipoManut getTipo() {
		return tipo;
	}
	public void setTipo(TipoManut tipo) {
		this.tipo = tipo;
	}
	public SimpleStringProperty getTipoProperty(){
		return new SimpleStringProperty(tipo.toString());
	}
	
	
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public SimpleStringProperty getDataInizioProperty(){
		return new SimpleStringProperty(dataInizio.toString());
	}
	
	
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public SimpleStringProperty getDataFineProperty(){
		return new SimpleStringProperty(dataFine.toString());
	}
}
