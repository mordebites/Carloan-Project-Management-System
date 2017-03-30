package presentation.boundary;

import javafx.stage.Modality;
import business.transfer.Parameter;

public class SchermataAggiungiFascia extends ReturnableStage {
    private static final String SCHEME_RESOURCE = "SchermataAggiungiFascia";

    public SchermataAggiungiFascia(Parameter parameter) {
        super(parameter, SCHEME_RESOURCE);

        setTitle("Aggiungi fascia");
        setResizable(false);

        initModality(Modality.APPLICATION_MODAL);
    }
}
