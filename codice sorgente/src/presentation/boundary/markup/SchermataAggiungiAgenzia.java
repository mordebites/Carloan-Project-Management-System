package presentation.boundary.markup;

public class SchermataAggiungiAgenzia extends SchermataImmissioneAgenzia {
	@Override
	public void initialize() {
		super.useCase = "InserisciAgenzia";
	}
}
