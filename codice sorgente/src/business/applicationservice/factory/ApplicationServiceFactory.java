package business.applicationservice.factory;

import business.transfer.ASCouple;

/**
 * Implementazione del pattern factory method per la gestione degli application service.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class ApplicationServiceFactory {
	/**
	 * Metodo che ci consente di otterenere il metodo di un application service
	 * indicando come parametro il caso d'uso sotto forma di stringa.
	 * @param useCase il caso d'uso di cui si vuole sapere l'application service
	 * @return l'application service
	 */
	public static ApplicationServiceMethod getMethod(String useCase) {
		
		ASCouple couple = ApplicationServiceSelector.getServiceMethod(useCase);
		return new CLApplicationServiceMethod(couple.getAs(), couple.getMethod());
	}
}
