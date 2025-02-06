package si2024.miguelquirogaalu.p03b;

public class TransicionComerFantasmas extends Transicion {

	public TransicionComerFantasmas(Estado destino) {
		this.destino = destino;
		condicion = new CondicionComerFantasmas();
	}
}