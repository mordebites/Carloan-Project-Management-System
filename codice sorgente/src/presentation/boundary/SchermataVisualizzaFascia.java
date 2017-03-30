package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataVisualizzaFascia extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataVisualizzaFascia";

    public SchermataVisualizzaFascia(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Visualizza fascia");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
