package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiCliente extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiCliente";

    public SchermataAggiungiCliente(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi cliente");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
