package business.entity;

/**
 * Classe entity per la modalita' di noleggio.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class ModNoleggio implements Entity {
	
	private Base base;
	private Chilometraggio km;
	
	
	public Base getBase() {
		return base;
	}
	public void setBase(Base base) {
		this.base = base;
	}
	public Chilometraggio getKm() {
		return km;
	}
	public void setKm(Chilometraggio km) {
		this.km = km;
	}
}
