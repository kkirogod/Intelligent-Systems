package si2024.miguelquirogaalu.p03;

import ontology.Types.ACTIONS;


public class AccionNull extends Accion {

	@Override
	public ACTIONS doAction(Mundo m) {

		return ACTIONS.ACTION_NIL;
	}

}
