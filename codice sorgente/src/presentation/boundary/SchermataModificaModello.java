package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaModello extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaModello";

    public SchermataModificaModello(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica modello");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
