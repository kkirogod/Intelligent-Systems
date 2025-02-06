package si2024.miguelquirogaalu.p03b;

public abstract class Transicion {

	public Estado destino;
	public Condicion condicion;
	
	public boolean seDispara(Mundo68 mundo) {
		return condicion.seCumple(mundo);
	}
	
	public Estado siguienteEstado() {
		return destino;
	}
}
