package presentation.boundary.markup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import business.transfer.Parameter;

public abstract class Schermata{
	
	@FXML
	protected Button annulla;
	protected Parameter parameter;
	
	
    public abstract void initialize();

    public void initData(Parameter par) {
        this.parameter = par;
    }

	public void onAnnulla(){
		// chiudo la schermata attuale
		Stage stage = (Stage) annulla.getScene().getWindow();
		stage.close();
	}
}