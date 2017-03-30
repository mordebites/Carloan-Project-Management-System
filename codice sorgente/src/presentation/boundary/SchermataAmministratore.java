package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAmministratore extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAmministratore";

    public SchermataAmministratore(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Amministratore");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
