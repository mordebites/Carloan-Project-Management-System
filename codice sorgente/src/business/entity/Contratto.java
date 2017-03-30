package business.entity;

import java.sql.Date;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Classe entity Contratto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Contratto implements Entity {
	private String id; //nro d'ordine
	private Date dataInizio;
	private Date dataLimite;
	private float acconto;
	private ModNoleggio modNoleggio;
	private Fascia fascia;
	private Agenzia luogoRest;
	private Date dataRientro; //chiuso
	private int kmPercorsi;//chiuso
	private float importoEffettivo;//chiuso
	private float saldo;//chiuso
	private Agenzia agenzia;
	private Cliente cliente;
	private Auto auto;
	private boolean contrattoAperto = true;
	
	public Fascia getFascia() {
		return fascia;
	}
	public void setFascia(Fascia fascia) {
		this.fascia = fascia;
	}
	public SimpleStringProperty getFasciaProperty(){
		return new SimpleStringProperty(fascia.getId());
	}
	
	
	public Agenzia getLuogoRest() {
		return luogoRest;
	}
	public void setLuogoRest(Agenzia luogoRest) {
		this.luogoRest = luogoRest;
	}
	public SimpleStringProperty getLuogoRestProperty(){
		return new SimpleStringProperty(luogoRest.getId());
	}
	
	
	public Agenzia getAgenzia() {
		return agenzia;
	}
	public void setAgenzia(Agenzia agenzia) {
		this.agenzia = agenzia;
	}
	public SimpleStringProperty getAgenziaProperty(){
		return new SimpleStringProperty(agenzia.getId());
	}
	
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public SimpleStringProperty getClienteProperty(){
		return new SimpleStringProperty(cliente.getId());
	}
	
	
	public Auto getAuto() {
		return auto;
	}
	public void setAuto(Auto auto) {
		this.auto = auto;
	}
	public SimpleStringProperty getAutoProperty(){
		if (auto == null) {
			return new SimpleStringProperty("ND");
		} else {
			return new SimpleStringProperty(auto.getId());
		}
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
	
	
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public SimpleStringProperty getDataInizioProperty(){
		return new SimpleStringProperty(dataInizio.toString());
	}
	
	
	public Date getDataLimite() {
		return dataLimite;
	}
	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}
	public SimpleStringProperty getDataLimiteProperty(){
		return new SimpleStringProperty(dataLimite.toString());
	}
	
	
	public float getAcconto() {
		return acconto;
	}
	public void setAcconto(float acconto) {
		this.acconto = acconto;
	}
	public SimpleFloatProperty getAccontoProperty(){
		return new SimpleFloatProperty(acconto);
	}
	
	
	public ModNoleggio getModNoleggio() {
		return modNoleggio;
	}
	public void setModNoleggio(ModNoleggio modNoleggio) {
		this.modNoleggio = modNoleggio;
	}
	public SimpleStringProperty getBaseProperty(){
		return new SimpleStringProperty(modNoleggio.getBase().toString());
	}
	public SimpleStringProperty getChilometraggioProperty(){
		return new SimpleStringProperty(modNoleggio.getKm().toString());
	}
	
	
	public Date getDataRientro() {
		return dataRientro;
	}
	public void setDataRientro(Date dataRientro) {
		this.dataRientro = dataRientro;
	}
	public SimpleStringProperty getDataRientroProperty(){
		return new SimpleStringProperty(dataRientro.toString());
	}
	
	
	public int getKmPercorsi() {
		return kmPercorsi;
	}
	public void setKmPercorsi(int kmPercorsi) {
		this.kmPercorsi = kmPercorsi;
	}
	public SimpleIntegerProperty getKmPercorsiProperty(){
		return new SimpleIntegerProperty(kmPercorsi);
	}
	
	
	public float getImportoEffettivo() {
		return importoEffettivo;
	}
	public void setImportoEffettivo(float importoEffettivo) {
		this.importoEffettivo = importoEffettivo;
	}
	public SimpleFloatProperty getImportoProperty(){
		return new SimpleFloatProperty(importoEffettivo);
	}
	
	
	public float getSaldo() {
		return saldo;
	}
	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
	public SimpleFloatProperty getSaldoProperty(){
		return new SimpleFloatProperty(saldo);
	}
	
	public boolean isContrattoAperto() {
		return contrattoAperto;
	}
	public void setContrattoAperto(boolean contrattoAperto) {
		this.contrattoAperto = contrattoAperto;
	}
	public SimpleStringProperty getApertoProperty(){
		String aperto = "No";
		if (contrattoAperto) {
			aperto = "Si'";
		}
		return new SimpleStringProperty(aperto);
	}

}
