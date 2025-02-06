package si2024.miguelquirogaalu.p03b;

import java.util.LinkedList;

public abstract class MaquinaFSM {

	public Estado estadoInicial;
	public Estado estadoActual;
	public LinkedList<Transicion> transiciones;
	public Mundo68 mundo;

	public Estado disparo() {
		
		for(Transicion transicion : transiciones) {
			if(transicion.seDispara(mundo)) {
				estadoActual = transicion.siguienteEstado();
				return estadoActual;
			}
		}
		
		return estadoActual;
	}
}
