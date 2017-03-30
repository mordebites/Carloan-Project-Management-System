package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataRegistrazione extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataRegistrazione";

    public SchermataRegistrazione(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Registrazione");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
