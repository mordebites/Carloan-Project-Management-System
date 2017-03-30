package presentation.boundary.markup;

import javafx.fxml.FXML;

public class SchermataAggiungiCliente extends SchermataImmissioneCliente {

	@Override
	@FXML
	public void initialize() {
		super.useCase = "InserisciCliente";
	}
}
