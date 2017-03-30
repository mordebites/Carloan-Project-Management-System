package utility;

import java.sql.Date;
import java.util.Calendar;

/**
 * Classe per la gestione delle date.
 * @author Morena De Liddo e Simone Marzulli
 *
 */

public class TimeUtility {
	/**
	 * Indica che si vuole ottenere un oggetto Date corrispondente
	 * al giorno seguente a quello dato in input a getDay.
	 */
	public final static int NEXT = 1;
	/**
	 * Indica che si vuole ottenere un oggetto Date corrispondente
	 * al giorno precedente a quello dato in input a getDay.
	 */
	public final static int PREV = -1;
	/**
	 * Indica che si vuole ottenere un oggetto Date corrispondente
	 * esattamente a quello dato in input a getDay.
	 */
	public final static int CURRENT = 0;
	
	private static final int MILLISECONDS_IN_DAY = 86400000;
	
	/**
	 * Passiamo una data ed una distanza temporale in giorni,
	 * restituisce una data azzerando tutti i valori tranne anno mese e giorno.
	 */
	public static Date getDay(Date data, int shift){
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_YEAR, shift);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}
	
	/**
	 * Calcola la distanza in giorni tra due date inserite come parametro
	 * restituisce un valore negativo se la prima data è successiva alla seconda
	 */
	public static Long getDistance(Date firstDate, Date secondDate) {
		Calendar cal = Calendar.getInstance();
		long firstMillisec = 0;
		long secondMillisec = 0;
		
		firstDate = getDay(firstDate, CURRENT);
		cal.setTime(firstDate);
		firstMillisec = cal.getTimeInMillis();
		
		secondDate = getDay(secondDate, CURRENT);
		cal.setTime(secondDate);
		secondMillisec = cal.getTimeInMillis();
		
		long distanza = (secondMillisec - firstMillisec) / MILLISECONDS_IN_DAY;
		
		return distanza;
	}
}
