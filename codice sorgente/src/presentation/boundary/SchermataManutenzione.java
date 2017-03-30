package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataManutenzione extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataManutenzione";

    public SchermataManutenzione(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Manutenzione");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}