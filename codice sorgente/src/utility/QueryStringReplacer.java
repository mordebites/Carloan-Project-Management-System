package utility;

/**
 * Classe per la sostuzione delle query all'interno di stringhe.
 * @author Morena De Liddo e Simone Marzulli
 *
 */
public class QueryStringReplacer {
	
	private static final String REGEX = "[?]";

	public static String replaceValues(String query, String[] values) {
		for(String s : values){
			query = query.replaceFirst(REGEX, s);
		}
		return query;
	}
}
