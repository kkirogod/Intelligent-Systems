package si2024.miguelquirogaalu.p04;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class BEspacioEstados {

	private Mundo45 mundo;

	public BEspacioEstados(Mundo45 mundo) {
		this.mundo = mundo;
	}
	
	public ACTIONS pensar() {
		
		return mundo.proxAccion(mundo.saltoAStar());
	}
}
