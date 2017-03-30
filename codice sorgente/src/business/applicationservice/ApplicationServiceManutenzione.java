package business.applicationservice;

import integration.dao.DAOAutoDisp;
import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utility.SessionManager;
import business.applicationservice.checker.Checker;
import business.applicationservice.checker.CheckerFactory;
import business.applicationservice.exception.ChiaveNonDisponibileException;
import business.applicationservice.exception.DataNonValidaException;
import business.applicationservice.factory.ApplicationService;
import business.entity.Auto;
import business.entity.PeriodoDisp;
import business.entity.PeriodoManut;
import business.entity.TipoManut;
import business.transfer.Parameter;

@SuppressWarnings("unchecked")
public class ApplicationServiceManutenzione implements ApplicationService, CRUD<PeriodoManut> {
	private DAOCRUD<PeriodoManut> daoManut = (DAOCRUD<PeriodoManut>) DAOFactory.getDAOEntity("DAOAutoManut");
	private ApplicationServiceAutoDisp asad = new ApplicationServiceAutoDisp();
	private Checker checker = CheckerFactory.buildInstance(PeriodoDisp.class);
	
	static Logger log = Logger.getLogger(ApplicationServiceManutenzione.class.getName());
	
	public void create(Parameter par) throws Exception {
		
		PeriodoManut manut = fill(par);
		daoManut.create(manut);
		
		PeriodoDisp disp = (PeriodoDisp) par.getValue("PeriodoDisp");
		asad.riduciDisp(manut.getDataInizio(), manut.getDataFine(), disp, disp.getAgenzia());
		
	}

	public PeriodoManut read(Parameter par) {
		return daoManut.read(par);
	}

	public List<PeriodoManut> readAll() {
		return daoManut.readAll(null);
	}

	public void update(Parameter par) throws Exception{
		PeriodoManut manut = fill(par);
		
		Date vdi = (Date) par.getValue("vecchiaDataInizio");
		Date vdf = (Date) par.getValue("vecchiaDataFine");
		
		asad.aggiungiDisp(manut.getAuto(), SessionManager.getAgenzia(), 
				SessionManager.getAgenzia(), vdi, vdf);
		
		List<Object> values = new ArrayList<Object>();
		values.add(manut.getAuto());
		values.add(manut.getDataInizio());
		values.add(manut.getDataFine());
		values.add(SessionManager.getAgenzia());
		values.add(DAOAutoDisp.RICHIESTA_MANUTENZIONE);
		
		PeriodoDisp disp = null;
		try {
			checker.check(values);
			
			manut.setId((String) par.getValue("id"));
			daoManut.update(manut);

			disp = (PeriodoDisp) values.get(5);
			asad.riduciDisp(manut.getDataInizio(), manut.getDataFine(), disp, disp.getAgenzia());
		} catch (DataNonValidaException e){
			values.set(1, vdi);
			values.set(2, vdf);
			checker.check(values);
			PeriodoDisp vecchioDisp = (PeriodoDisp) values.get(5);
			asad.riduciDisp(vdi, vdf, vecchioDisp, vecchioDisp.getAgenzia());
		}
	}

	public void delete(Parameter par) {
		String id = null;
		
		try {
			id = (String) par.getValue("id");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		PeriodoManut manut = daoManut.read(par);
		daoManut.delete(id);
		
		asad.aggiungiDisp(manut.getAuto(), SessionManager.getAgenzia(), SessionManager.getAgenzia(), 
				manut.getDataInizio(), manut.getDataFine());
	}

	private PeriodoManut fill(Parameter par) {
		//DAOCRUD<Auto> daoAuto = (DAOCRUD<Auto>) DAOFactory.getDAOEntity("DAOAuto");
		PeriodoManut manut = new PeriodoManut();

		TipoManut tipo = null;
		Date dataInizio = null;
		Date dataFine = null;
		Auto auto = null;
		
		try {
			auto = (Auto) par.getValue("Auto");
			tipo = (TipoManut) par.getValue("TipoManut");
			dataInizio = (Date) par.getValue("DataInizio");
			dataFine = (Date) par.getValue("DataFine");
		} catch (ChiaveNonDisponibileException e) {
			log.error("Chiave non disponibile nel transfer object!");
		}
		
		manut.setAuto(auto);
		manut.setDataInizio(dataInizio);
		manut.setDataFine(dataFine);
		manut.setTipo(tipo);
		
		return manut;
	}

	/*
	
	public static void main(String... args) throws Exception{
		ApplicationServiceManutenzione asm = new ApplicationServiceManutenzione();
		Parameter par = new Parameter();
		
		/*
		// create
		Auto auto = new Auto();
		auto.setId("1");
		Date dataInizio = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Date dataFine = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		dataInizio = TimeUtility.getDay(dataInizio, 3);
		dataFine = TimeUtility.getDay(dataInizio, 7);
		
		par.setValue("auto", auto);
		par.setValue("dataInizio", dataInizio);
		par.setValue("dataFine", dataFine);
		par.setValue("tipoManut", TipoManut.ORDINARIA);
		
		asm.create(par);
		
		*/
		
		/*
		// read
		par.setValue("id", "1");
		PeriodoManut tempPer = asm.read(par);
		*/
		
		/*
		// update
		Auto auto = new Auto();
		auto.setId("1");
		Date dataInizio = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		Date dataFine = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		dataInizio = TimeUtility.getDay(dataInizio, 3);
		dataFine = TimeUtility.getDay(dataInizio, 7);
		
		par.setValue("id", "1"); // idauto in manutenzione da aggiornare
		par.setValue("auto", auto);
		par.setValue("dataInizio", dataInizio);
		par.setValue("dataFine", dataFine);
		par.setValue("tipoManut", TipoManut.STRAORDINARIA);
		asm.update(par);
		*/
		
		/*
		// readall
		for(PeriodoManut e : asm.readAll()) {
			System.out.println(e.getId());
		}
		
		// delete
		par.setValue("id", "1");
		asm.delete(par);
	}*/
}
