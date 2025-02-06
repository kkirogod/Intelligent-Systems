package si2024.miguelquirogaalu.p03;

public abstract class ArbolDecision extends Motor{

	public NodoArbol raiz;

	// Será el encargado de empezar la búsqueda a partir de nodo raíz buscando la
	// acción a realizar a través del mismo.
	@Override
	public Accion piensa(Mundo m) { 
		return (Accion) raiz.decide(m);
	}
}
