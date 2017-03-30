package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiContratto extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiContratto";

    public SchermataAggiungiContratto(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi contratto");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
