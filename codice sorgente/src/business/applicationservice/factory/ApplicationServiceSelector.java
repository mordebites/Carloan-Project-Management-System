package business.applicationservice.factory;

import java.util.HashMap;

import business.applicationservice.ApplicationServiceAgenzia;
import business.applicationservice.ApplicationServiceAssegnaAuto;
import business.applicationservice.ApplicationServiceAutenticazione;
import business.applicationservice.ApplicationServiceAuto;
import business.applicationservice.ApplicationServiceCliente;
import business.applicationservice.ApplicationServiceConcordaLuogo;
import business.applicationservice.ApplicationServiceContratto;
import business.applicationservice.ApplicationServiceFascia;
import business.applicationservice.ApplicationServiceManutenzione;
import business.applicationservice.ApplicationServiceModello;
import business.applicationservice.ApplicationServiceRegistrazione;
import business.transfer.ASCouple;

/**
 * Implementazione del pattern Selector per le classi che formano gli application service.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class ApplicationServiceSelector {
	private static HashMap<String, ASCouple> asMap = new HashMap<String, ASCouple>();
	
	static {
		asMap.put("InserisciAuto", new ASCouple(new ApplicationServiceAuto(), "create"));
		asMap.put("RicercaAuto", new ASCouple(new ApplicationServiceAuto(), "read"));
		asMap.put("RimuoviAuto", new ASCouple(new ApplicationServiceAuto(), "delete"));
		asMap.put("ModificaAuto", new ASCouple(new ApplicationServiceAuto(), "update"));
		asMap.put("VisualizzaOgniAuto", new ASCouple(new ApplicationServiceAuto(), "readAll"));
		
		asMap.put("InserisciAgenzia", new ASCouple(new ApplicationServiceAgenzia(), "create"));
		asMap.put("RicercaAgenzia", new ASCouple(new ApplicationServiceAgenzia(), "read"));
		asMap.put("RimuoviAgenzia", new ASCouple(new ApplicationServiceAgenzia(), "delete"));
		asMap.put("ModificaAgenzia", new ASCouple(new ApplicationServiceAgenzia(), "update"));
		asMap.put("VisualizzaOgniAgenzia", new ASCouple(new ApplicationServiceAgenzia(), "readAll"));
		
		asMap.put("InserisciCliente", new ASCouple(new ApplicationServiceCliente(), "create"));
		asMap.put("RicercaCliente", new ASCouple(new ApplicationServiceCliente(), "read"));
		asMap.put("ModificaCliente", new ASCouple(new ApplicationServiceCliente(), "update"));
		asMap.put("VisualizzaOgniCliente", new ASCouple(new ApplicationServiceCliente(), "readAll"));
		
		asMap.put("InserisciContratto", new ASCouple(new ApplicationServiceContratto(), "create"));
		asMap.put("ModificaContratto", new ASCouple(new ApplicationServiceContratto(), "update"));
		asMap.put("RicercaContratto", new ASCouple(new ApplicationServiceContratto(), "read"));
		asMap.put("ChiudiContratto", new ASCouple(new ApplicationServiceContratto(), "close"));
		asMap.put("RimuoviContratto", new ASCouple(new ApplicationServiceContratto(), "delete"));
		asMap.put("VisualizzaOgniContratto", new ASCouple(new ApplicationServiceContratto(), "readAll"));
		
		asMap.put("ConcordaLuogo", new ASCouple(new ApplicationServiceConcordaLuogo(), "concordaLuogo"));
		asMap.put("AssegnaAuto", new ASCouple(new ApplicationServiceAssegnaAuto(), "assegnaAuto"));
		
		asMap.put("InserisciAutoManutenzione", new ASCouple(new ApplicationServiceManutenzione(), "create"));
		asMap.put("ModificaAutoManutenzione", new ASCouple(new ApplicationServiceManutenzione(), "update"));
		asMap.put("RicercaAutoManutenzione", new ASCouple(new ApplicationServiceManutenzione(), "read"));
		asMap.put("RimuoviAutoManutenzione", new ASCouple(new ApplicationServiceManutenzione(), "delete"));
		asMap.put("VisualizzaOgniAutoManutenzione", new ASCouple(new ApplicationServiceManutenzione(), "readAll"));
		
		asMap.put("AutenticaUtente", new ASCouple(new ApplicationServiceAutenticazione(), "isAuthenticated"));
		asMap.put("RegistraUtente", new ASCouple(new ApplicationServiceRegistrazione(), "create"));
		
		asMap.put("InserisciFascia", new ASCouple(new ApplicationServiceFascia(), "create"));
		asMap.put("RicercaFascia", new ASCouple(new ApplicationServiceFascia(), "read"));
		asMap.put("RimuoviFascia", new ASCouple(new ApplicationServiceFascia(), "delete"));
		asMap.put("ModificaFascia", new ASCouple(new ApplicationServiceFascia(), "update"));
		asMap.put("VisualizzaOgniFascia", new ASCouple(new ApplicationServiceFascia(), "readAll"));
		
		asMap.put("InserisciModello", new ASCouple(new ApplicationServiceModello(), "create"));
		asMap.put("RicercaModello", new ASCouple(new ApplicationServiceModello(), "read"));
		asMap.put("RimuoviModello", new ASCouple(new ApplicationServiceModello(), "delete"));
		asMap.put("ModificaModello", new ASCouple(new ApplicationServiceModello(), "update"));
		asMap.put("VisualizzaOgniModello", new ASCouple(new ApplicationServiceModello(), "readAll"));
		
		asMap.put("VisualizzaPeriodi", new ASCouple(new ApplicationServiceAssegnaAuto(), "getPeriodi"));
	}

	/**
	 * Ritorna un oggetto di tipo ASCouple partendo dal service inserito come parametro
	 * sotto forma di stringa.
	 * @param service
	 * @return
	 */
	public static ASCouple getServiceMethod(String service) {
		ASCouple couple = asMap.get(service);
		return couple;
	}
}
