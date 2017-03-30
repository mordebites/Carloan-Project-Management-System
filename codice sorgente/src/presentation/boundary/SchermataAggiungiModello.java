package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiModello extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiModello";

    public SchermataAggiungiModello(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi modello");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
