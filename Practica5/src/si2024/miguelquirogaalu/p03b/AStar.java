package si2024.miguelquirogaalu.p03b;

import java.util.*;
import core.game.Observation;
import tools.Vector2d;

public class AStar {
	
	private ArrayList<Observation>[][] mapa;
	private Nodo[][] mapaNodos;
	private int ancho, alto, tipoObj;
	private Nodo objetivo;
	private List<Nodo> abierta;
	private List<Nodo> cerrada;
	public List<Nodo> ruta;
	private double inicioX, inicioY;
	private Mundo68 mundo;

	public AStar(Mundo68 m, ArrayList<Observation>[][] grid, double xi, double yi, double xf, double yf, int tObj) {

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

		objetivo = new Nodo(xf, yf, 0, null);

		for (fila = 0; fila < alto; fila++) {
			for (columna = 0; columna < ancho; columna++) {

				ArrayList<Observation> contenido = mapa[columna][fila];

				if (contenido.size() > 0) {
					Observation celda = contenido.get(0);
					mapaNodos[columna][fila] = new Nodo(Mundo68.coordenada(celda.position.x),
							Mundo68.coordenada(celda.position.y), mundo.etiqueta(celda.position), objetivo);
					
				} else
					mapaNodos[columna][fila] = new Nodo(columna, fila, 1, objetivo);
			}
		}
		mapaNodos[(int) xf][(int) yf] = objetivo;
		
		encontrarRuta();
		ruta();
	}

	public void encontrarRuta() {

		Nodo actual = null;

		abierta.add(mapaNodos[(int) inicioX][(int) inicioY]);

		while (!abierta.isEmpty()) {

			Collections.sort(abierta);
			actual = abierta.get(0);
			
			//System.out.println("actual: " + actual.y + ", " + actual.x);

			//double d = (Math.abs(actual.x - objetivo.x) + Math.abs(actual.y - objetivo.y));

			if (actual.x == objetivo.x && actual.y == objetivo.y) {
				//System.out.println("POSICION [" + objetivo.y + ", " + objetivo.x + "] ENCONTRADA");
				break;
			}
			else {
				abierta.remove(0);
				cerrada.add(actual);

				for (int columna = (int) (actual.x - 1); columna <= (int) (actual.x + 1); columna++) {
					for (int fila = (int) (actual.y - 1); fila <= (int) (actual.y + 1); fila++) {
						
						//System.out.println("nodo: " + fila + ", " + columna + " (" + mapaNodos[tColumna(columna)][tFila(fila)].tipo + ")");

						if ((columna == (int) (actual.x - 1) || columna == (int) (actual.x + 1))
								&& (fila == (int) (actual.y - 1) || fila == (int) (actual.y + 1)))
							continue;
						
						else if (!abierta.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& !cerrada.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& (mapaNodos[tColumna(columna)][tFila(fila)].tipo == 0
									|| (mapaNodos[tColumna(columna)][tFila(fila)].tipo == 6 
										&& tipoObj != 16 
										&& mundo.fantasmasLejosSpawn()))
								&& lejosFantasma(mapaNodos[tColumna(columna)][tFila(fila)]))
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

	
	private boolean lejosFantasma(Nodo nodo) {

		ArrayList<Vector2d> posicionesFantasmas = mundo.getPosicionesFantasmasColores();

		if (!posicionesFantasmas.isEmpty()) {
			for (Vector2d pos : posicionesFantasmas) {
				if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo68.coordenada(pos.x), Mundo68.coordenada(pos.y)) < 3) {
					return false;
				}
			}
		}
		return true;
	}

	public void ruta() {

		Nodo aux = objetivo;

		//System.out.println("RUTA A [" + objetivo.y + ", " + objetivo.x + "] : ");

		while (aux.anterior != null) {

			//System.out.println("  -> F: " + aux.y + " C: " + aux.x);

			ruta.add(aux);
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
