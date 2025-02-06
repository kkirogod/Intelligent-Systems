package si2024.miguelquirogaalu.p03b;

public class EstadoComerBolaGrande extends Estado {

	@Override
	public Accion getAccion() {
		return new AccionComerBolaGrande();
	}
}