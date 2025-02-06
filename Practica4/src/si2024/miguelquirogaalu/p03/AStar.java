package si2024.miguelquirogaalu.p03;

import java.util.*;

import core.game.Observation;
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
	private Mundo53 mundo;

	public AStar(Mundo53 m, ArrayList<Observation>[][] grid, double xi, double yi, double xf, double yf, int tObj) {

		xi = Math.round(xi);
		yi = Math.round(yi);
		xf = Math.round(xf);
		yf = Math.round(yf);

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
					mapaNodos[columna][fila] = new Nodo(Mundo53.coordenada(celda.position.x),
							Mundo53.coordenada(celda.position.y), celda.itype, objetivo);

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
			actual = abierta.get(0);

			// double d = (Math.abs(actual.x - objetivo.x) + Math.abs(actual.y -
			// objetivo.y));

			if (actual.x == objetivo.x && actual.y == objetivo.y) {
				// System.out.println("POSICION [" + objetivo.y + ", " + objetivo.x + "]
				// ENCONTRADA");
				break; // llegamos al objetivo
			} else {
				abierta.remove(0);
				cerrada.add(actual);

				for (int columna = (int) (actual.x - 1); columna <= (int) (actual.x + 1); columna++) {
					for (int fila = (int) (actual.y - 1); fila <= (int) (actual.y + 1); fila++) {

						if ((columna == (int) (actual.x - 1) || columna == (int) (actual.x + 1))
								&& (fila == (int) (actual.y - 1) || fila == (int) (actual.y + 1)))
							continue;

						else if (!abierta.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& !cerrada.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& mapaNodos[tColumna(columna)][tFila(fila)].tipo != 0
								&& ((mundo.getNumPuertas() > 0
											&& lejosSanos(mapaNodos[tColumna(columna)][tFila(fila)])
											&& lejosVirus(mapaNodos[tColumna(columna)][tFila(fila)]))
										|| mundo.getNumPuertas() == 0))
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

	private boolean lejosEnfermera(Nodo nodo) {

		ArrayList<Vector2d> posicionesEnf = mundo.getPosicionesEnfermeras();

		if (posicionesEnf.size() > 0) {
			for (Vector2d pos : posicionesEnf) {
				if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo53.coordenada(pos.x),
						Mundo53.coordenada(pos.y)) < 2) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean lejosSanos(Nodo nodo) {
		
		if(!mundo.estoyInfectado())
			return true;

		ArrayList<Vector2d> posicionesSanos = mundo.getPosicionesSanos();

		for (Vector2d pos : posicionesSanos) {
			if(mundo.posSanosEnPeligro().contains(pos))
				return true;
			
			else if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo53.coordenada(pos.x), Mundo53.coordenada(pos.y)) < 2) {
				return false;
			}
		}

		return true;
	}

	private boolean lejosVirus(Nodo nodo) {
		
		if(mundo.estoyInfectado())
			return true;

		ArrayList<Vector2d> posicionesInf = mundo.getPosicionesInfectados();

		for (Vector2d pos : posicionesInf) {
			if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo53.coordenada(pos.x), Mundo53.coordenada(pos.y)) < 2) {
				return false;
			}
		}
		
		ArrayList<Vector2d> posicionesMocos = mundo.getPosicionesMocos();

		for (Vector2d pos : posicionesMocos) {
			if (mundo.distanciaManhattan(nodo.x, nodo.y, Mundo53.coordenada(pos.x), Mundo53.coordenada(pos.y)) < 1) {
				return false;
			}
		}

		return true;
	}

	public void ruta() {

		Nodo aux = objetivo;

		// System.out.println("RUTA A [" + objetivo.y + ", " + objetivo.x + "] : ");

		while (aux.anterior != null) {

			// System.out.println(" -> F: " + aux.y + " C: " + aux.x);

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
