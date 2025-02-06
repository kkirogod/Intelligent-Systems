package si2024.miguelquirogaalu.p01;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class Mundo18 implements Mundo {

	private ArrayList<Observation>[][] entorno;
	private Vector2d posicionAvatar;
	private ArrayList<Vector2d> posicionesGusanos;
	private ArrayList<Vector2d> posicionesAguilasBlancas;
	private ArrayList<Vector2d> posicionesAguilasNegras;
	private ArrayList<Vector2d> posicionesAguilasNCercanas;
	private static int bloque;
	private int numFilas;
	private int numColumnas;
	private StateObservation stateObs;
	private int numAguilasBlancas;
	private boolean enPeligro;

	public static double coordenada(double xy) {
		return xy / bloque;
	}

	public boolean estoyEnPeligro() {
		return enPeligro;
	}

	public int getGameTick() {
		return stateObs.getGameTick();
	}

	public int getNumAguilasBlancas() {
		return numAguilasBlancas;
	}

	public Vector2d getPosicionAvatar() {
		return posicionAvatar;
	}

	public ArrayList<Vector2d> getPosicionesAguilasNegras() {
		return posicionesAguilasNegras;
	}

	public ArrayList<Vector2d> getPosicionesGusanos() {
		return posicionesGusanos;
	}

	public Mundo18(StateObservation stateObs) {

		this.stateObs = stateObs;

		bloque = stateObs.getBlockSize();
		entorno = stateObs.getObservationGrid();

		numFilas = entorno[0].length;
		numColumnas = entorno.length;

		posicionAvatar = stateObs.getAvatarPosition();

		posicionesGusanos = new ArrayList<Vector2d>();
		posicionesAguilasBlancas = new ArrayList<Vector2d>();
		posicionesAguilasNegras = new ArrayList<Vector2d>();
		posicionesAguilasNCercanas = new ArrayList<Vector2d>();
		numAguilasBlancas = 0;

		enPeligro = false;
	}

	@Override
	public void analizarMundo(StateObservation stateObs) {

		this.stateObs = stateObs;

		enPeligro = false;

		entorno = stateObs.getObservationGrid();
		posicionAvatar = stateObs.getAvatarPosition();
		/*
		 * System.out.println("Tamaño bloque: " + getBloque());
		 * System.out.println("Numero filas: " + numFilas);
		 * System.out.println("Numero de columnas: " + numColumnas);
		 * System.out.println("Tamaño del entorno: " + (getBloque() * numColumnas) + "x"
		 * + (getBloque() * numFilas)); System.out.println( "Posicion Avatar [F, C]: ["
		 * + coordenada(posicionAvatar.y) + ", " + coordenada(posicionAvatar.x) + "]");
		 * System.out.println("");
		 */
		numAguilasBlancas = 0;
		posicionesGusanos = new ArrayList<Vector2d>();
		posicionesAguilasNegras = new ArrayList<Vector2d>();
		posicionesAguilasBlancas = new ArrayList<Vector2d>();
		posicionesAguilasNCercanas = new ArrayList<Vector2d>();

		for (int y = 0; y < numFilas; y++) {
			for (int x = 0; x < numColumnas; x++) {

				ArrayList<Observation> contenido = entorno[x][y];
				String celda = null;
				/*
				 * if (x == 0) System.out.print(" ");
				 */
				if (contenido.size() > 0) {

					for (Observation propiedades : contenido) {

						switch (propiedades.itype) {
						case 0:
							celda = "#";
							break;
						case 3:
							celda = "G";
							posicionesGusanos.add(propiedades.position);
							break;
						case 5:
							if (coordenada(distanciaManhattan(posicionAvatar, propiedades.position)) <= 2) {
								enPeligro = true;
								posicionesAguilasNCercanas.add(propiedades.position);
							}
							posicionesAguilasNegras.add(propiedades.position);
							celda = "@";
							break;
						case 6:
							numAguilasBlancas++;
							posicionesAguilasBlancas.add(propiedades.position);
							celda = "A";
							break;
						default:
							if (propiedades.position.x == posicionAvatar.x
									&& propiedades.position.y == posicionAvatar.y)
								celda = "X";
							else
								celda = String.valueOf(propiedades.itype);
						}
					}
					// System.out.print(celda);

				} // else
					// System.out.print(" ");
			}
			// System.out.println("");
		}
		/*
		 * System.out.println("Posicion aguila mas cercana [F, C]: [" +
		 * coordenada(aguilaBlancaMasCercana().y) + ", " +
		 * coordenada(aguilaBlancaMasCercana().x) + "]");
		 * System.out.println("\n\n*********************************************\n");
		 */
	}

	public Vector2d aguilaBlancaMasCercanaSinRiesgo() {

		Vector2d posAguila = null;
		double distanciaMasCercana = Double.MAX_VALUE;
		boolean hayRiesgo;

		for (Vector2d posA : posicionesAguilasBlancas) {

			hayRiesgo = false;

			for (Vector2d posB : posicionesAguilasBlancas) {

				double dAB = distanciaManhattan(posB, posA);

				if (dAB <= 2) {
					hayRiesgo = true;
					break;
				}
			}

			double d = distanciaManhattan(posicionAvatar, posA);

			if (d < distanciaMasCercana && !hayRiesgo) {
				posAguila = posA;
				distanciaMasCercana = d;
			}
		}

		return posAguila;
	}

	public Vector2d aguilaBlancaMasCercana() {

		Vector2d posAguila = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d posA : posicionesAguilasBlancas) {
			double d = distanciaManhattan(posicionAvatar, posA);

			if (d < distanciaMasCercana) {
				posAguila = posA;
				distanciaMasCercana = d;
			}
		}

		return posAguila;
	}

	public Vector2d aguilaBlancaMasCercanaAGusano() {

		Vector2d posAguila = aguilaBlancaMasCercana();
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d posA : posicionesAguilasBlancas) {

			for (Vector2d posG : posicionesGusanos) {

				double d = distanciaManhattan(posA, posG);
				// double d2 = distanciaManhattan(posA, posicionAvatar);

				if (d < distanciaMasCercana) {
					posAguila = posA;
					distanciaMasCercana = d;
				}
			}
		}
		return posAguila;
	}

	public List<ACTIONS> accionesDisponibles() {

		List<ACTIONS> acciones = new LinkedList<ACTIONS>();

		ArrayList<Observation> contenido;

		contenido = entorno[(int) coordenada(posicionAvatar.x) + 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_RIGHT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_RIGHT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) + 1];

		acciones.add(ACTIONS.ACTION_DOWN);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_DOWN);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x) - 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_LEFT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_LEFT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) - 1];

		acciones.add(ACTIONS.ACTION_UP);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_UP);
		}

		if (enPeligro) {
			for (Vector2d posAguilaNegra : posicionesAguilasNCercanas) {
				if (coordenada(distanciaManhattan(posicionAvatar, posAguilaNegra)) == 2) {
					List<ACTIONS> copiaAcciones = new LinkedList<ACTIONS>(acciones);

					for (ACTIONS accion : copiaAcciones) {
						if (coordenada(distanciaManhattan(proxPosicion(accion), posAguilaNegra)) < 2
								&& acciones.size() > 1)
							acciones.remove(accion);
					}
				}
			}
		}

		if (acciones.size() == 0) {
			acciones.add(accionAleatoria());
		}

		return acciones;
	}

	public List<ACTIONS> accionesDisponibles(Vector2d pos) {

		List<ACTIONS> acciones = new LinkedList<ACTIONS>();

		ArrayList<Observation> contenido;

		contenido = entorno[(int) coordenada(pos.x) + 1][(int) coordenada(pos.y)];

		acciones.add(ACTIONS.ACTION_RIGHT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_RIGHT);
		}

		contenido = entorno[(int) coordenada(pos.x)][(int) coordenada(pos.y) + 1];

		acciones.add(ACTIONS.ACTION_DOWN);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_DOWN);
		}

		contenido = entorno[(int) coordenada(pos.x) - 1][(int) coordenada(pos.y)];

		acciones.add(ACTIONS.ACTION_LEFT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_LEFT);
		}

		contenido = entorno[(int) coordenada(pos.x)][(int) coordenada(pos.y) - 1];

		acciones.add(ACTIONS.ACTION_UP);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 5)
				acciones.remove(ACTIONS.ACTION_UP);
		}

		return acciones;
	}

	public ACTIONS accionAleatoria() {

		List<ACTIONS> acciones = new LinkedList<ACTIONS>();
		acciones.add(ACTIONS.ACTION_LEFT);
		acciones.add(ACTIONS.ACTION_UP);
		acciones.add(ACTIONS.ACTION_DOWN);
		acciones.add(ACTIONS.ACTION_RIGHT);

		Random rnd = new Random(System.currentTimeMillis());

		return acciones.get(rnd.nextInt(4));
	}

	public int getBloque() {
		return bloque;
	}

	public Vector2d proxPosicion(ACTIONS accion) {

		Vector2d proxPosicion = null;

		switch (accion) {
		case ACTION_DOWN:
			proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y + bloque);
			break;
		case ACTION_UP:
			proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y - bloque);
			break;
		case ACTION_RIGHT:
			proxPosicion = new Vector2d(posicionAvatar.x + bloque, posicionAvatar.y);
			break;
		case ACTION_LEFT:
			proxPosicion = new Vector2d(posicionAvatar.x - bloque, posicionAvatar.y);
			break;
		case ACTION_NIL:
			proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		default:
			break;
		}

		return proxPosicion;
	}

	private double distanciaManhattan(Vector2d a, Vector2d b) {
		return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
	}

	public double distanciaManhattan(double xi, double yi, double xf, double yf) {
		return Math.abs(xi - xf) + Math.abs(yi - yf);
	}

	public Vector2d saltoAStar(int tipoObj, Vector2d obj) {

		AStar aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
				coordenada(obj.x), coordenada(obj.y), tipoObj);

		aStar.encontrarRuta();
		aStar.ruta();

		if (aStar.ruta.size() > 0)
			return new Vector2d(aStar.ruta.get(0).x * bloque, aStar.ruta.get(0).y * bloque);
		else
			return null;
	}

	public ACTIONS proxAccion(Vector2d salto) {

		if (salto.x < posicionAvatar.x)
			return ACTIONS.ACTION_LEFT;

		if (salto.x > posicionAvatar.x)
			return ACTIONS.ACTION_RIGHT;

		if (salto.y < posicionAvatar.y)
			return ACTIONS.ACTION_UP;

		if (salto.y > posicionAvatar.y)
			return ACTIONS.ACTION_DOWN;

		return null;
	}
}
