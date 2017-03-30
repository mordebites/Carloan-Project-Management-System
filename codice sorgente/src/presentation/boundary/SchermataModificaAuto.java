package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaAuto extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaAuto";

    public SchermataModificaAuto(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
