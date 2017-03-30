package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataOperatore extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataOperatore";

    public SchermataOperatore(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Operatore");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
