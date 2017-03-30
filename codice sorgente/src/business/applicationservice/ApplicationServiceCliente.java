package business.applicationservice;

import integration.dao.DAOCRU;
import integration.dao.DAOFactory;

import java.util.List;

import org.apache.log4j.Logger;

import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Cliente;
import business.transfer.Parameter;

/**
 * Application service per la gestione dei clienti.
 * Implementa metodi di creazione, lettura, aggiornamento ed
 * eliminazione di clienti nel sistema di persistenza dei dati.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class ApplicationServiceCliente implements ApplicationService, CRU<Cliente>{
	@SuppressWarnings("unchecked")
	private DAOCRU<Cliente> daoCliente = (DAOCRU<Cliente>) DAOFactory.getDAOEntity("DAOCliente");
 
	static Logger log = Logger.getLogger(ApplicationServiceCliente.class.getName());

	/**
	 * Crea un nuovo cliente prendendo come input un parameter, transfer object
	 * che contiene tutte le informazioni riguardanti il cliente.
	 * 
	 * @param par l'oggetto che contiene informazioni sul cliente da creare
	 * @throws Exception
	 */
	public void create(Parameter par) throws Exception {
		Cliente cliente = fill(par);
		daoCliente.create(cliente);
	}

	/**
	 * Restituisce un cliente presente nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto che contiene informazioni sul cliente da cercare
	 * @return l'oggetto cliente trovato nel sistema di persistenza dei dati 
	 * Ritorna null se non viene trovata il cliente richiesto.
	 */
	public Cliente read(Parameter par) {
		return daoCliente.read(par);
	}

	/**
	 * Restituisce una lista di i clienti presenti nel sistema di persistenza dei dati.
	 * 
	 * @return la lista di clienti presenti nel sistema di persistenza dei dati.
	 * Ritorna una lista vuota se non sono presenti clienti.
	 */
	public List<Cliente> readAll() {
		return daoCliente.readAll(null);
	}

	/**
	 * Aggiorna le informazioni su un cliente specificato come parametro andando
	 * a scrivere i dati aggiornati nel sistema di persistenza dei dati.
	 * 
	 * @param par l'oggetto contente le informazioni sul cliente da aggiornare
	 * @throws Exception
	 */
	public void update(Parameter par) throws Exception {
		try {
			Cliente cliente = fill(par);
			String id = (String) par.getValue("id");
			
			cliente.setId(id);
			daoCliente.update(cliente);
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		
	}
	
	/*
	 * Costruisce un oggetto di tipo cliente, in base ai dati presenti
	 * nel transfer object parameter passato come parametro alla funzione.
	 */
	private Cliente fill(Parameter parameter) throws Exception {
        String nome = (String) parameter.getValue("Nome");
        String cognome = (String) parameter.getValue("Cognome");
        String numTel = (String) parameter.getValue("NumTel");

        /*List<Object> clienteValues = new ArrayList<>();
        clienteValues.add(nome);
        clienteValues.add(cognome);
        clienteValues.add(numTel);

        checker.check(clienteValues);*/

        Cliente cliente = new Cliente();

        cliente.setNome(nome);
        cliente.setCognome(cognome);
        cliente.setNumTel(numTel);
        
        return cliente;
    }
	
	/*	
	public static void main(String... args) throws Exception {
		ApplicationServiceCliente appServ = new ApplicationServiceCliente();
		Parameter par = new Parameter();
		
		
		//scrittura
		par.setValue("Nome", new String("Simone"));
		par.setValue("Cognome", new String("Malli"));
		par.setValue("NumTel", new String("0805034549"));
		appServ.create(par);
		
		
		
	    // lettura
		par.setValue("id", new String("2"));
		Cliente cliente = appServ.read(par);
		
		
		
		// readall
		for(Cliente e : appServ.readAll()) {
			System.out.println(e.getId());
		}
		
		
		
		// update
		par.setValue("id", new String("2"));		
		par.setValue("Nome", new String("Dorena"));
		par.setValue("Cognome", new String("Meldildo"));
		par.setValue("NumTel", new String("338436990"));
		appServ.update(par);
		
	}*/
}
