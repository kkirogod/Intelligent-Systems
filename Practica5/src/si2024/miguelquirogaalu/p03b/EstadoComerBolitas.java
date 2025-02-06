package si2024.miguelquirogaalu.p03b;

public class EstadoComerBolitas extends Estado {

	@Override
	public Accion getAccion() {
		return new AccionComerBolitas();
	}
}