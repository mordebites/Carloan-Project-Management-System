package business.applicationservice.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import business.applicationservice.exception.CampoDuplicatoException;
import business.transfer.Parameter;

/**
 * Classe per l'implementazione del pattern command per gli application service.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class CLApplicationServiceMethod extends ApplicationServiceMethod {
	
	static Logger log = Logger.getLogger(CLApplicationServiceMethod.class.getName());

	CLApplicationServiceMethod(ApplicationService as, String method) {
		super(as, method);
	}
	
	/**
	 * Invoca il metodo dell'application service passando il trasfer object parameter
	 * @param par il trasfer object contente le informazioni che serviranno per il metodo invocato
	 * @return l'oggetto ritornato dal metodo richiamato per l'application service
	 */
	
	public Object invoke(Parameter par) throws Exception {
		//CONTROLLA
		@SuppressWarnings("rawtypes")
		Class[] parClass = {};
		Parameter[] pars = {};
		if(par != null){
			parClass = new Class[1];
			parClass[0] = Parameter.class;
			pars = new Parameter[1];
			pars[0] = par;
		}
		
		Object result = null;
		Class<?> asClass;
        try {
        	asClass = as.getClass();
            Method asMethod = asClass.getMethod(method, parClass);
            result = asMethod.invoke(as, (Object[]) pars);
        } catch(Exception e){
        	if (e.getClass() == InvocationTargetException.class) {
        		// Answer:
        	    if (e.getCause().getClass() == CampoDuplicatoException.class) {
        	    	throw new CampoDuplicatoException();
        	    }
        	}
        	
        	log.error("Errore  nell'invocazione del pattern command!");
        }
		return result;
	}

}
