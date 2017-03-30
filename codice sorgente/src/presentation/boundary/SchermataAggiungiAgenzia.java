package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiAgenzia extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiAgenzia";

    public SchermataAggiungiAgenzia(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi agenzia");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
