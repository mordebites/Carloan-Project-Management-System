package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaContratto extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaContratto";

    public SchermataModificaContratto(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
