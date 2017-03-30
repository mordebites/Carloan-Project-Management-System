package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaFascia extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaFascia";

    public SchermataModificaFascia(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
