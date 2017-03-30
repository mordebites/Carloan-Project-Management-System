package presentation.boundary.markup;

import javafx.fxml.FXML;

public class SchermataAggiungiContratto extends SchermataImmissioneContratto {

	@Override
	@FXML
	public void initialize() {
		super.useCase = "InserisciContratto";
		super.initialize();
	}
	
}
