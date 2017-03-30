package presentation.boundary.factory;

import presentation.boundary.Boundary;
import business.transfer.Parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

/**
 * Implementazione del pattern factory method per la gestione delle classi boundary.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class BoundaryFactory {
    private static final String BOUNDARY_PACKAGE_PATH = "presentation.boundary.";
    private static final String SERVICE_NAME_HEAD = "Mostra"; //DA CAMBIARE?
    private static final int BOUNDARY_NAME_START_POSITION = SERVICE_NAME_HEAD.length();

    static Logger log = Logger.getLogger(BoundaryFactory.class.getName());

    /**
     * Restituisce un oggetto di tipo boundary partendo dal nome del servizio e il trasfer
     * object parameter contente vari attributi.
     * @param serviceName il nome dell'app service
     * @param parameter il trasfer object contente i dati
     * @return un oggetto di tipo boundary
     */
    public static Boundary getBoundary(String serviceName, Parameter parameter) {
        Class<?> boundaryClass = getBoundaryClass(serviceName);
        return newBoundaryInstance(boundaryClass, parameter);
    }


    private static Class<?> getBoundaryClass(String serviceName) {
        String simpleClassName = serviceName.substring(BOUNDARY_NAME_START_POSITION);
        String canonicalClassName = BOUNDARY_PACKAGE_PATH + simpleClassName;

        Class<?> boundaryClass = null;

        try {
            boundaryClass = Class.forName(canonicalClassName);
        } catch (ClassNotFoundException e) {
        	log.error("La classe non e' stata trovata!");
        }

        return boundaryClass;
    }

    private static Boundary newBoundaryInstance(Class<?> boundaryClass, Parameter parameter) {
        Boundary boundaryInstance = null;

        try {
            @SuppressWarnings("rawtypes")
			Constructor constructor = boundaryClass.getConstructor(Parameter.class);
            boundaryInstance = (Boundary) constructor.newInstance(parameter);
        } catch (InstantiationException e) {
        	log.error("Problema di istanziazione");
        } catch (IllegalAccessException e) {
        	log.error("Accesso non consentito");
        } catch (NoSuchMethodException e) {
        	log.error("Non esiste il metodo!");
        } catch (InvocationTargetException e) {
        	log.error("Errore nell'invocazione del target");
        }

        return boundaryInstance;
    }
}
