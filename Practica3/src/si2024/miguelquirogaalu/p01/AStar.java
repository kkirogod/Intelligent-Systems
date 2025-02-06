package si2024.miguelquirogaalu.p01;

import java.util.*;

import core.game.Observation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AStar {
	private ArrayList<Observation>[][] mapa;
	private Nodo[][] mapaNodos;
	private int ancho, alto;
	private Nodo objetivo;
	private List<Nodo> abierta;
	private List<Nodo> cerrada;
	public List<Nodo> ruta;
	private double inicioX, inicioY;
	private int tipoObj;
	private Mundo18 mundo;

	public AStar(Mundo18 m, ArrayList<Observation>[][] grid, double xi, double yi, double xf, double yf, int tObj) {

		mapa = grid;
		
		mundo = m;

		tipoObj = tObj;

		abierta = new ArrayList<Nodo>();
		cerrada = new ArrayList<Nodo>();
		ruta = new ArrayList<Nodo>();

		inicioX = xi;
		inicioY = yi;

		alto = mapa[0].length;
		ancho = mapa.length;

		mapaNodos = new Nodo[ancho][alto];

		int fila = 0;
		int columna = 0;

		objetivo = new Nodo(xf, yf, tipoObj, null);

		for (fila = 0; fila < alto; fila++) {
			for (columna = 0; columna < ancho; columna++) {

				ArrayList<Observation> contenido = mapa[columna][fila];

				if (contenido.size() > 0) {
					Observation celda = contenido.get(0);
					mapaNodos[columna][fila] = new Nodo(Mundo18.coordenada(celda.position.x),
							Mundo18.coordenada(celda.position.y), celda.itype, objetivo);
				} else
					mapaNodos[columna][fila] = new Nodo(columna, fila, -1, objetivo); // celda vacía
			}
		}
		mapaNodos[(int) xf][(int) yf] = objetivo;
	}

	public void encontrarRuta() {

		Nodo actual = null;

		abierta.add(mapaNodos[(int) inicioX][(int) inicioY]);

		while (!abierta.isEmpty()) {

			Collections.sort(abierta);
			actual = abierta.remove(0);

			double d = (Math.abs(actual.x - objetivo.x) + Math.abs(actual.y - objetivo.y));

			if (d==0)
				break; // llegamos al objetivo
			else {
				cerrada.add(actual);

				for (int columna = (int) (actual.x - 1); columna <= (int) (actual.x + 1); columna++) {
					for (int fila = (int) (actual.y - 1); fila <= (int) (actual.y + 1); fila++) {

						if ((columna == (int) (actual.x - 1) || columna == (int) (actual.x + 1))
								&& (fila == (int) (actual.y - 1) || fila == (int) (actual.y + 1)))
							continue;

						else if (!abierta.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& !cerrada.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& mapaNodos[tColumna(columna)][tFila(fila)].tipo != 0
								&& mapaNodos[tColumna(columna)][tFila(fila)].tipo != 5
								&& lejosAguilaN(mapaNodos[tColumna(columna)][tFila(fila)]))
						{	
							mapaNodos[tColumna(columna)][tFila(fila)].anterior = actual;
							mapaNodos[tColumna(columna)][tFila(fila)].calculaCoste();

							abierta.add(mapaNodos[tColumna(columna)][tFila(fila)]);
						}
					}
				}
			}
		}
	}

	private boolean lejosAguilaN(Nodo nodo) {
		
		for (Vector2d posAguilaNegra : mundo.getPosicionesAguilasNegras()) {
			if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo18.coordenada(posAguilaNegra.x), Mundo18.coordenada(posAguilaNegra.y)) <= 2) {
				return false;
			}
		}
		return true;
	}

	public void ruta() {

		Nodo aux = objetivo;

		// System.out.println("RUTA: ");

		while (aux.anterior != null) {

			// System.out.println("x: " + aux.x + " y: " + aux.y);

			ruta.add(aux);
			mapaNodos[(int) aux.x][(int) aux.y].tipo = -2; // tipo camino
			aux = aux.anterior;
		}

		Collections.reverse(ruta);
	}

	public int tColumna(int columna) { // para que no se salga de los limites del mapa
		if (columna < 0)
			return 0;
		if (columna >= ancho)
			return ancho - 1;

		return columna;
	}

	public int tFila(int fila) { // para que no se salga de los limites del mapa
		if (fila < 0)
			return 0;
		if (fila >= alto)
			return alto - 1;

		return fila;
	}
}
