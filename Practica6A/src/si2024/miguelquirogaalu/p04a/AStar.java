package si2024.miguelquirogaalu.p04a;

import java.util.*;
import core.game.Observation;
import ontology.Types.ACTIONS;
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
	private Mundo49 mundo;

	public AStar(Mundo49 m, ArrayList<Observation>[][] grid, double xi, double yi, double xf, double yf, int tObj) {

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
					mapaNodos[columna][fila] = new Nodo(Mundo49.coordenada(celda.position.x),
							Mundo49.coordenada(celda.position.y), mundo.etiqueta(celda.position), objetivo);
					
				} else
					mapaNodos[columna][fila] = new Nodo(columna, fila, 1, objetivo);
				
				// System.out.print(" " + mapaNodos[columna][fila].tipo);
			}
			// System.out.println();
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
			
			// System.out.println("actual: " + actual.y + ", " + actual.x);

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
						
						if ((columna == (int) (actual.x - 1) || columna == (int) (actual.x + 1))
								&& (fila == (int) (actual.y - 1) || fila == (int) (actual.y + 1)))
							continue;
						
						else if (!abierta.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& !cerrada.contains(mapaNodos[tColumna(columna)][tFila(fila)])
								&& mapaNodos[tColumna(columna)][tFila(fila)].tipo != 0
								&& (mapaNodos[tColumna(columna)][tFila(fila)].tipo != 3
										|| (mapaNodos[tColumna(columna)][tFila(fila)].tipo == 3
											&& (mundo.hayNenufar(columna, fila) || mundo.hayFlecha(columna, fila))))
								&& (!esFlecha(mapaNodos[tColumna(columna)][tFila(fila)].tipo) 
										|| (esFlecha(mapaNodos[tColumna(columna)][tFila(fila)].tipo) 
												&& mundo.flechaValida(new Vector2d(columna*mundo.getBloque(), fila*mundo.getBloque()), dirFlecha(mapaNodos[tColumna(columna)][tFila(fila)].tipo))))
								&& !esProhibida(columna, fila))
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
	
	private boolean esProhibida(int x, int y) {
		
		for(Vector2d pos : mundo.getPosicionesProhibidas()) {
			if(Mundo49.coordenada(pos.x) == x && Mundo49.coordenada(pos.y) == y)
				return true;
		}
		
		return false;
	}
	
	private ACTIONS dirFlecha (int tipo) {
		switch (tipo) {
		case 14:
			return ACTIONS.ACTION_DOWN;
		case 15:
			return ACTIONS.ACTION_UP;
		case 16:
			return ACTIONS.ACTION_RIGHT;
		case 17:
			return ACTIONS.ACTION_LEFT;
		default:
			return null;
		}
	}
	
	private boolean esFlecha (int tipo) {
		
		if(tipo >= 14 && tipo <= 17)
			return true;
		else
			return false;
	}
	
	// PASAR POR AGUA SI:
	// - hay alguna flecha izq o der en la misma fila, o alguna flecha arriba o abajo en la mimsa columna
	// - hay nenufar o spawn en esa fila

	public void ruta() {

		Nodo aux = objetivo;

		// System.out.println("RUTA A [" + objetivo.y + ", " + objetivo.x + "] : ");

		while (aux.anterior != null) {

			// System.out.println("  -> F: " + aux.y + " C: " + aux.x);

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
