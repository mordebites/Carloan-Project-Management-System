package presentation.boundary.markup;

import javafx.fxml.FXML;

public class SchermataAggiungiManutenzione extends SchermataImmissioneManutenzione {

	@Override
	@FXML
	public void initialize() {
		super.useCase = "InserisciAutoManutenzione";
		super.initialize();
	}
}
