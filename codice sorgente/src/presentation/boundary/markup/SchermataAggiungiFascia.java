package presentation.boundary.markup;

public class SchermataAggiungiFascia extends SchermataImmissioneFascia {
	@Override
	public void initialize() {
		super.useCase = "InserisciFascia";
	}
}
