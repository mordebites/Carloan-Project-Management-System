package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaManutenzione extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaManutenzione";

    public SchermataModificaManutenzione(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica manutenzione");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
