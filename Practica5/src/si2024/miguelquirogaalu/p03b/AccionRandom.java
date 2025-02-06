package si2024.miguelquirogaalu.p03b;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;

public class AccionRandom extends Accion {

	List<ACTIONS> acciones;
	Random rnd;
	
	@Override
	public ACTIONS doAction(Mundo68 m) {
		
		acciones = new LinkedList<ACTIONS>();
		acciones.add(ACTIONS.ACTION_LEFT);
		acciones.add(ACTIONS.ACTION_UP);
		acciones.add(ACTIONS.ACTION_DOWN);
		acciones.add(ACTIONS.ACTION_RIGHT);
		
		rnd = new Random(System.currentTimeMillis());
		
		return acciones.get(rnd.nextInt(4));
	}

}
