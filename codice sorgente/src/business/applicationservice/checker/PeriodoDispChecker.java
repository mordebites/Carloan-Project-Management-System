package business.applicationservice.checker;

import integration.dao.DAOCRUD;
import integration.dao.DAOFactory;

import java.util.List;

import business.applicationservice.exception.DataNonValidaException;
import business.entity.PeriodoDisp;
import business.transfer.Parameter;

/**
 * Classe per il controllo del formato dei valori di un periodo di disponibilita'.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class PeriodoDispChecker implements Checker {
	@SuppressWarnings("unchecked")
	private DAOCRUD<PeriodoDisp> daoAutoDisp = (DAOCRUD<PeriodoDisp>) DAOFactory.getDAOEntity("DAOAutoDisp");
	
	@Override
	public void check(List<Object> values) throws Exception {
		Parameter par = new Parameter();
		par.setValue("auto", values.get(0)); //auto
		par.setValue("dataInizio", values.get(1));
		par.setValue("dataFine", values.get(2));
		par.setValue("agenzia", values.get(3));
		par.setValue("tipoRichiesta", values.get(4));
		PeriodoDisp disp = null;

		disp = daoAutoDisp.read(par);
		if(disp == null){
			throw new DataNonValidaException();
		}
		
		values.add(5, disp);
	}

}
