package si2024.miguelquirogaalu.p03b;

public class EstadoComerFantasmas extends Estado {

	@Override
	public Accion getAccion() {
		return new AccionComerFantasmas();
	}
}