package si2024.miguelquirogaalu.p01;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;

public class AccionAleatoria implements Accion {

	List<ACTIONS> acciones;
	Random rnd;
	
	public AccionAleatoria() {
		
		acciones = new LinkedList<ACTIONS>();
		acciones.add(ACTIONS.ACTION_LEFT);
		acciones.add(ACTIONS.ACTION_UP);
		acciones.add(ACTIONS.ACTION_DOWN);
		acciones.add(ACTIONS.ACTION_RIGHT);
		
		rnd = new Random(System.currentTimeMillis());
	}

	@Override
	public ACTIONS doAction(Mundo m) {
		
		return acciones.get(rnd.nextInt(4));
	}

}
