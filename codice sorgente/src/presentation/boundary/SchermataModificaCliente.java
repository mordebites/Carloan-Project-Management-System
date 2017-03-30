package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataModificaCliente extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataModificaCliente";

    public SchermataModificaCliente(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Modifica cliente");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}