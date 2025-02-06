package si2024.miguelquirogaalu.p03b;

public class TransicionComerBolaGrande extends Transicion {

	public TransicionComerBolaGrande(Estado destino) {
		this.destino = destino;
		condicion = new CondicionComerBolaGrande();
	}
}