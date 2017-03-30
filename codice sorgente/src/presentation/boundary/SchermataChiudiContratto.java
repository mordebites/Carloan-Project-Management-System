package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataChiudiContratto extends SchermataModifica {

	private static final String SCHEME_RESOURCE = "SchermataChiudiContratto";

    public SchermataChiudiContratto(Parameter parameter) {
    	
        super(parameter, SCHEME_RESOURCE);
        
        setTitle("Chiudi contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }

}
