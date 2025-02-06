package si2024.miguelquirogaalu.p03;

import ontology.Types.ACTIONS;

// Extenderán los nodos de acción que impelementa
// doAction() para devolver la acción a realizar

public abstract class Accion extends NodoArbol {
	
	public abstract ACTIONS doAction(Mundo m);

	@Override
	public NodoArbol decide(Mundo m) {
		return this;
	}
}
