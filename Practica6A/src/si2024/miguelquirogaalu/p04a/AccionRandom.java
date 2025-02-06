package si2024.miguelquirogaalu.p04a;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;

public class AccionRandom {

	List<ACTIONS> acciones;
	Random rnd;
	
	public ACTIONS doAction() {
		
		acciones = new LinkedList<ACTIONS>();
		acciones.add(ACTIONS.ACTION_LEFT);
		acciones.add(ACTIONS.ACTION_UP);
		acciones.add(ACTIONS.ACTION_DOWN);
		acciones.add(ACTIONS.ACTION_RIGHT);
		acciones.add(ACTIONS.ACTION_NIL);
		
		rnd = new Random(System.currentTimeMillis());
		
		return acciones.get(rnd.nextInt(5));
	}
}
