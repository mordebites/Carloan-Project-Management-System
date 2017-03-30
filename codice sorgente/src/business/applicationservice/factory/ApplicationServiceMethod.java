package business.applicationservice.factory;

import business.transfer.Parameter;

/**
 * Classe astratta che implementa il pattern command per l'application service.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public abstract class ApplicationServiceMethod {
	/**
	 * l'application service che verra' poi unito ad un metodo che compone lo use case
	 */
	protected ApplicationService as;
	protected String method;
	
	ApplicationServiceMethod(ApplicationService as, String method) {
		this.as = as;
		this.method = method;
	}
	
	/**
	 * Invoca il metodo dell'application service passando il trasfer object parameter
	 * @param par il trasfer object contente le informazioni che serviranno per il metodo invocato
	 * @return l'oggetto ritornato dal metodo richiamato per l'application service
	 * @throws Exception 
	 */
	
	public abstract Object invoke(Parameter par) throws Exception;
}
