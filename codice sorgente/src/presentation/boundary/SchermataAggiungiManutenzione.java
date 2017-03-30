package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiManutenzione extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiManutenzione";

    public SchermataAggiungiManutenzione(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi manutenzione");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
