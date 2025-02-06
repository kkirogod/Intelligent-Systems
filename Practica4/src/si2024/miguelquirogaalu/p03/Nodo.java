package si2024.miguelquirogaalu.p03;

public class Nodo implements Comparable<Nodo>{
	double x, y, heuristica, puntaje, distancia;
	int tipo;
	Nodo anterior;

	public Nodo(double x, double y, int tipo, Nodo objetivo) {
		this.x = x;
		this.y = y;
		this.tipo = tipo;
		
		if(objetivo != null)
			heuristica = Math.abs(this.x - objetivo.x) + Math.abs(this.y - objetivo.y);
	}
	
	public void calculaCoste() {
		
		if(anterior == null)
			distancia = 0;
		else
			distancia = anterior.distancia + 1;
		
		puntaje = distancia + heuristica;
	}

	@Override
	public int compareTo(Nodo otro) {
		
		return Double.compare(this.puntaje, otro.puntaje);
	}
}
