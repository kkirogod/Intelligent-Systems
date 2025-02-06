package si2024.miguelquirogaalu.p03b;

public class TransicionComerBolitas extends Transicion {

	public TransicionComerBolitas(Estado destino) {
		this.destino = destino;
		condicion = new CondicionComerBolitas();
	}
}