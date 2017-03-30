package business.entity;

import java.sql.Date;

/**
 * Classe entity per il periodo di disponibilita'.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class PeriodoDisp implements Entity{
	private String id;
	private Auto auto;
	private Agenzia agenzia;
	private Date dataInizio;
	private Date dataFine;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Auto getAuto() {
		return auto;
	}
	public void setAuto(Auto auto) {
		this.auto = auto;
	}
	public Agenzia getAgenzia() {
		return agenzia;
	}
	public void setAgenzia(Agenzia agenzia) {
		this.agenzia = agenzia;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
}
