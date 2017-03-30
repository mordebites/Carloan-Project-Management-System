package presentation.controller;

import org.apache.log4j.Logger;

import presentation.boundary.Boundary;
import presentation.boundary.factory.BoundaryFactory;
import business.applicationservice.exception.CampoDuplicatoException;
import business.applicationservice.factory.ApplicationServiceFactory;
import business.applicationservice.factory.ApplicationServiceMethod;
import business.transfer.Parameter;

public class ApplicationController {
	
	static Logger log = Logger.getLogger(ApplicationController.class.getName());
	
	private static final String SHOW_GUI_SYNTAX = "Mostra[a-zA-Z]+";
	
	public static Object handleRequest(String request, Parameter par) {
		Object result = null;

        try {
            if (request.matches(SHOW_GUI_SYNTAX)) {
                result = dispatchGUI(request, par);
            } else {
                result = execute(request, par);
            }
        } catch (Exception e) {
        	if (e.getClass() == CampoDuplicatoException.class) {
	        	result = new CampoDuplicatoException();
        	} else {
        		log.error("Errore nell'application controller!");
        	}
        }

        return result;
	}

	private static Object execute(String serviceName, Parameter parameter) throws Exception{
        ApplicationServiceMethod appServiceMethod = ApplicationServiceFactory.getMethod(serviceName);
        return appServiceMethod.invoke(parameter);
    }

	// si dovrebbe ricontrollare per la nostra gestione delle interfacce grafiche
	
    private static Object dispatchGUI(String serviceName, Parameter parameter) {
        Boundary boundary = BoundaryFactory.getBoundary(serviceName, parameter);
        return boundary.showWindow(parameter);
    }
}
