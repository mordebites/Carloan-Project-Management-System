package business.applicationservice;

import integration.dao.DAOAutoDisp;
import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.sql.Date;

import org.apache.log4j.Logger;

import utility.TimeUtility;
import business.entity.Agenzia;
import business.entity.Auto;
import business.entity.PeriodoDisp;
import business.transfer.Parameter;

class ApplicationServiceAutoDisp {
	@SuppressWarnings("unchecked")
	private DAOCRUD<PeriodoDisp> daoAutoDisp = (DAOCRUD<PeriodoDisp>) DAOFactory.getDAOEntity("DAOAutoDisp");
	static Logger log = Logger.getLogger(DAOAutoDisp.class.getName());
	
	void aggiungiDisp(Auto auto, Agenzia agenzia, Agenzia luogoRest, Date dataInizio, Date dataFine) {
		Parameter par = new Parameter();
		PeriodoDisp nuovo = new PeriodoDisp();
		nuovo.setAuto(auto);
		nuovo.setAgenzia(luogoRest);
		nuovo.setDataInizio(dataInizio);
		nuovo.setDataFine(dataFine);
		
		par.setValue("tipoRichiesta", DAOAutoDisp.RICHIESTA_PREV);
		par.setValue("auto", auto);
		par.setValue("agenzia", agenzia);
		par.setValue("dataFine", TimeUtility.getDay(dataInizio, -1));
		PeriodoDisp prec = daoAutoDisp.read(par);
		
		par.setValue("tipoRichiesta", DAOAutoDisp.RICHIESTA_NEXT);
		par.setValue("dataInizio", TimeUtility.getDay(dataFine, 1));
		PeriodoDisp nextAgenzia = daoAutoDisp.read(par);
		
		PeriodoDisp nextLuogoRest = null;
		
		if(nextAgenzia == null && agenzia.getId() != luogoRest.getId()) {
			par.setValue("agenzia", luogoRest);
			nextLuogoRest = daoAutoDisp.read(par);
		}

		if(prec != null && nextLuogoRest == null) {
			daoAutoDisp.delete(prec.getId());
			nuovo.setDataInizio(prec.getDataInizio());
		}
		if(nextAgenzia != null) {
			daoAutoDisp.delete(nextAgenzia.getId());
			nuovo.setDataFine(nextAgenzia.getDataFine());
		} else if (nextLuogoRest != null) {
			daoAutoDisp.delete(nextLuogoRest.getId());
			nuovo.setDataFine(nextLuogoRest.getDataFine());
		}
		
		try {
			daoAutoDisp.create(nuovo);
		} catch (Exception e) {
			log.error("Coppia auto -  data inizio duplicata!");
		}
	}
	
	/*
	 * Questo metodo viene chiamato quando si deve aggiungere un contratto o un periodo di manutenzione.
	 * 
	 * diPar e' la data di inizio del periodo desiderato
	 * dfPar e' la data di fine del periodo desiderato
	 * PeriodoDisp e' il periodo di disponibilita' selezionato
	 */
	void riduciDisp(Date diPar, Date dfPar, PeriodoDisp disp, Agenzia luogoRest) {

		Date diDisp, dfDisp;
		diDisp = disp.getDataInizio();
		dfDisp = disp.getDataFine();
		
		if(dfDisp == null){
			if(diPar.compareTo(diDisp) > 0){
				daoAutoDisp.delete(disp.getId());
				disp.setDataFine(TimeUtility.getDay(diPar, TimeUtility.PREV));
				try {
					daoAutoDisp.create(disp);
				} catch (Exception e) {
					log.error("Coppia auto -  data inizio duplicata!");
				}
			} 
			disp.setAgenzia(luogoRest);
			disp.setDataInizio(TimeUtility.getDay(dfPar, TimeUtility.NEXT));
			disp.setDataFine(null);
		} else {
				//se i due periodi non coincidono
			if(diPar.compareTo(diDisp) > 0 || dfPar.compareTo(dfDisp) < 0) {
				daoAutoDisp.delete(disp.getId());
				if(diPar.compareTo(diDisp) > 0 && dfPar.compareTo(dfDisp) == 0){
					disp.setDataFine(TimeUtility.getDay(diPar, TimeUtility.PREV));
				} else if(diPar.compareTo(diDisp) == 0 && dfPar.compareTo(dfDisp) < 0){
					disp.setDataInizio(TimeUtility.getDay(dfPar, TimeUtility.NEXT));
					disp.setAgenzia(luogoRest);
				} else {
					disp.setDataFine(TimeUtility.getDay(diPar, TimeUtility.PREV));
					try {
						daoAutoDisp.create(disp);
					} catch (Exception e) {
						log.error("Coppia auto -  data inizio duplicata!");
					}
					
					disp.setDataFine(dfDisp);
					disp.setAgenzia(luogoRest);
					disp.setDataInizio(TimeUtility.getDay(dfPar, TimeUtility.NEXT));
				}
			}
		}
		try {
			daoAutoDisp.create(disp);
		} catch (Exception e) {
			log.error("Coppia auto -  data inizio duplicata!");
		}
		
	}
	
	void cambiaLuogoRest(Auto auto, Agenzia vecchioLuogoRest, Agenzia nuovoLuogoRest, Date dataFine) {
		Parameter par = new Parameter();
		
		par.setValue("tipoRichiesta", DAOAutoDisp.RICHIESTA_NEXT);
		par.setValue("auto", auto);
		par.setValue("agenzia", vecchioLuogoRest);
		par.setValue("dataInizio", TimeUtility.getDay(dataFine, 1));
		PeriodoDisp next = daoAutoDisp.read(par);
		
		if(next != null) {
			next.setAgenzia(nuovoLuogoRest);
			try {
				daoAutoDisp.update(next);
			} catch (Exception e) {
				log.error("Coppia auto -  data inizio duplicata!");
			}
		}
	}
}
