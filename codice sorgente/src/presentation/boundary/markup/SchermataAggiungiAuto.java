package presentation.boundary.markup;

public class SchermataAggiungiAuto extends SchermataImmissioneAuto{

	@Override
	public void initialize() {
		super.useCase = "InserisciAuto";
		super.initialize();
	}
}
