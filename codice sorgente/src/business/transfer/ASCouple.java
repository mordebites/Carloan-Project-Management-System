package business.transfer;

import business.applicationservice.factory.ApplicationService;

/**
 * Classe per l'utilizzo del transfer object pattern ASCouple.
 * Associa un application service al suo metodo sotto forma di stringa.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class ASCouple {
	private ApplicationService as;
	private String method;
	
	public ASCouple(ApplicationService as, String method) {
		this.as = as;
		this.method = method;
	}
	
	/**
	 * Restituisce un application service
	 * @return l'application service
	 */
	
	public ApplicationService getAs() {
		return as;
	}
	
	/**
	 * Imposta l'application service
	 * @param as l'application service
	 */
	public void setAs(ApplicationService as) {
		this.as = as;
	}
	
	/**
	 * Ritorna il metodo sotto forma di stringa appartenente all'application service.
	 * @return il metodo in formato stringa
	 */
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
