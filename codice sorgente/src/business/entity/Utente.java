package business.entity;

/**
 * Classe entity per utente.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class Utente implements Entity {
	private String id;
	private String username;
	private String password;
	private Agenzia agenzia;
	private TipoUtente tipo;
	
	public Agenzia getAgenzia() {
		return agenzia;
	}
	public void setAgenzia(Agenzia agenzia) {
		this.agenzia = agenzia;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TipoUtente getTipo() {
		return tipo;
	}
	public void setTipo(TipoUtente tipo) {
		this.tipo = tipo;
	}	
}
