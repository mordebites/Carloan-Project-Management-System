package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAssegnaAuto extends SchermataModifica {
    private static final String SCHEME_RESOURCE = "SchermataAssegnaAuto";

    public SchermataAssegnaAuto(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Assegna auto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}