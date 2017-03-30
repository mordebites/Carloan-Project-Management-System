package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaAgenzia extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaAgenzia";

    public SchermataModificaAgenzia(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica agenzia");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
