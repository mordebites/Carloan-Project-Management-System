package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataConcordaRestituzione extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataConcordaRestituzione";

    public SchermataConcordaRestituzione(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Concorda restituzione");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}