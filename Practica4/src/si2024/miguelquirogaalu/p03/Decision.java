package si2024.miguelquirogaalu.p03;

// Extenderán los nodos que implementaran la decisión y devolverán el
// nodo elegido según se cumpla o no la pregunta

public abstract class Decision extends NodoArbol {
	
	public abstract NodoArbol getBranch(Mundo m);

	@Override
	public NodoArbol decide(Mundo m) {
		return getBranch(m).decide(m);
	}
}
