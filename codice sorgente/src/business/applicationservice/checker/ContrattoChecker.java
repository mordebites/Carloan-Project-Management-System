package business.applicationservice.checker;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import utility.TimeUtility;
import business.applicationservice.exception.DataNonValidaException;
import business.applicationservice.exception.NoleggioIniziatoException;
import business.entity.Base;


/**
 * Classe per il controllo del formato dei valori di un contratto.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class ContrattoChecker implements Checker {
	/**
	 * Costante che indica la necessita' di controllare un contratto aperto
	 */
	public static final int CONTRATTO_APERTO = 1;
	/**
	 * Costante che indica la necessita' di controllare un contratto da chiudere
	 */
	public static final int CONTRATTO_CHIUSO = 2;
	/**
	 * Costante che indica la necessita' di controllare un contratto da eliminare
	 */
	public static final int CONTRATTO_ELIMINANDO = 3;
	
	
	public void check(List<Object> values) throws Exception{
		//controlli per un contratto aperto
		if((int) values.get(0) == CONTRATTO_APERTO) {
			checkDataInizio((Date)values.get(1));
			checkDataLimite((Date)values.get(2), (Date)values.get(1), (Base) values.get(4));
			// controllo per acconto
			CheckerUtility.checkQuantitaPositiva((float) values.get(3));
		} else if ((int) values.get(0) == CONTRATTO_CHIUSO) { //controlli per un contratto da chiudere
			checkDataRientro((Date)values.get(1), (Date)values.get(2), (Date)values.get(4));
			CheckerUtility.checkQuantitaPositiva((Integer) values.get(3));
		} else { //controlli per un contratto da eliminare
			checkNoleggioIniziato((Date)values.get(1));
		}
	}
	
	private void checkNoleggioIniziato(Date dataInizio) throws NoleggioIniziatoException{
		try {
			this.checkDataInizio(dataInizio);
		} catch (DataNonValidaException e) {
			throw new NoleggioIniziatoException();
		}
	}
	
	
	private void checkDataLimite(Date dataLimite, Date dataInizio, Base base) throws DataNonValidaException{
		long distanza = TimeUtility.getDistance(dataInizio, dataLimite);

		if (distanza < 0) {
			throw new DataNonValidaException();
		}
			
		if(Base.SETTIMANALE == base){
			if(distanza % 7 != 0 || distanza == 0){
				throw new DataNonValidaException(); //meglio eccezione più specifica?
			}
		}
	}
	
	private void checkDataInizio(Date dataInizio) throws DataNonValidaException {
		Date dataAttuale = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		long distanza = TimeUtility.getDistance(dataInizio, dataAttuale);
		
		if (distanza > 0) {
			throw new DataNonValidaException();
		}
		
	}
	
	private void checkDataRientro(Date dataRientro, Date dataInizio, Date dataLimite) throws DataNonValidaException {

		if ((dataRientro.compareTo(dataInizio) < 0) || dataLimite.compareTo(dataRientro) < 0) {
			throw new DataNonValidaException();
		}
		
	}
}
