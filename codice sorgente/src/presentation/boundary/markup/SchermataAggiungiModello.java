package presentation.boundary.markup;

import javafx.fxml.FXML;

public class SchermataAggiungiModello extends SchermataImmissioneModello {
	@Override
	@FXML
	public void initialize() {
		super.useCase = "InserisciModello";
		super.initialize();
	}
}
