package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataVisualizzaContratto extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataVisualizzaContratto";

    public SchermataVisualizzaContratto(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Visualizza contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
