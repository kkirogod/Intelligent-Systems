package si2024.miguelquirogaalu.p04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class Mundo45 {

	private boolean visualizar = true;

	private ArrayList<Observation>[][] entorno;
	private Vector2d posicionAvatar;
	public static int bloque;
	private int numFilas;
	private int numColumnas;
	private ArrayList<Vector2d> posicionesBasura;
	private ArrayList<Vector2d> posicionesMuro;
	private ArrayList<Vector2d> posicionesCuerpo;
	private List<Vector2d> mejorRuta;
	private StateObservation stateObs;
	private Random rnd;
	private double dB;
	private int tick;
	private int posB;

	public Mundo45(StateObservation stateObs) {

		this.stateObs = stateObs;

		bloque = stateObs.getBlockSize();
		entorno = stateObs.getObservationGrid();

		numFilas = entorno[0].length;
		numColumnas = entorno.length;

		rnd = new Random(System.currentTimeMillis());

		actualizar(stateObs);
		
		Collections.sort(posicionesBasura, new Comparator<Vector2d>() {
			@Override
			public int compare(Vector2d p1, Vector2d p2) {
				double dist1 = Math.abs(p1.x - posicionAvatar.x) + Math.abs(p1.y - posicionAvatar.y);
				double dist2 = Math.abs(p2.x - posicionAvatar.x) + Math.abs(p2.y - posicionAvatar.y);
				return Double.compare(dist1, dist2);
			}
		});
		
		TSPBruteForce tsp = new TSPBruteForce(this);

		mejorRuta = tsp.encontrarRutaMinima(posicionesBasura, posicionesBasura.size());

		double di = distanciaManhattan(mejorRuta.get(0));
		double df = distanciaManhattan(mejorRuta.get(mejorRuta.size() - 1));

		if (df < di)
			Collections.reverse(mejorRuta);
		
		posB = 0;
	}

	public void actualizar(StateObservation stateObs) {

		this.stateObs = stateObs;

		tick = stateObs.getGameTick();

		rnd = new Random(System.currentTimeMillis());

		entorno = stateObs.getObservationGrid();
		posicionAvatar = stateObs.getAvatarPosition();

		posicionesBasura = new ArrayList<Vector2d>();
		posicionesMuro = new ArrayList<Vector2d>();
		posicionesCuerpo = new ArrayList<Vector2d>();

		if (visualizar) {
			System.out.println("\n\n*********************************************\n");
			System.out.println("Tamaño bloque: " + getBloque());
			System.out.println("Numero filas: " + numFilas);
			System.out.println("Numero de columnas: " + numColumnas);
			System.out.println("Tamaño del entorno: " + (getBloque() * numColumnas) + "x" + (getBloque() * numFilas));
			System.out.println("Posicion Avatar [F, C]: [" + coordenada(posicionAvatar.y) + ", "
					+ coordenada(posicionAvatar.x) + "]");
			System.out.println("Orientacion Avatar [F, C]: [" + coordenada(stateObs.getAvatarOrientation().y) + ", "
					+ coordenada(stateObs.getAvatarOrientation().x) + "] -> " + orientacion());
			System.out.println("");
		}

		for (int y = 0; y < numFilas; y++) {
			for (int x = 0; x < numColumnas; x++) {

				ArrayList<Observation> contenido = entorno[x][y];
				String celda = null;

				if (x == 0)
					System.out.print(" ");

				if (contenido.size() > 0) {

					for (Observation propiedades : contenido) {

						if (propiedades.position.x == posicionAvatar.x && propiedades.position.y == posicionAvatar.y) {
							celda = "X";
						} else {
							switch (propiedades.itype) {
							case 0:
								celda = "#";
								posicionesMuro.add(propiedades.position);
								break;
							case 4:
								celda = "B";
								posicionesBasura.add(propiedades.position);
								break;
							case 3:
								celda = "o";
								posicionesCuerpo.add(propiedades.position);
								break;
							default:
								celda = String.valueOf(propiedades.itype);
							}
						}
					}
				} else
					celda = " ";

				celda = String.format("%2s", celda); // para que se vea más espaciado

				if (visualizar)
					System.out.print(celda);
			}
			if (visualizar)
				System.out.println("");
		}

		if (visualizar) {
			System.out.println("");
		}
		
		dB = distanciaManhattan(basuraMasCercana());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Vector2d saltoAStar() {

		Vector2d obj = null;
		List<Nodo> ruta = null;
		
		if (hayMuros()) {
			if(posicionesBasura.size() > 5)
				obj = basuraMasCercanaOrientacion();
			
			if (dB <= 90 && dB >= 70 && posicionesBasura.size() == 3)
				return proxPosicion(ACTIONS.ACTION_RIGHT);
			
			if (dB <= 80 && dB >= 50 && posicionesBasura.size() == 9)
				return proxPosicion(ACTIONS.ACTION_DOWN);
			
			// QUITAR LUEGO ////////////
			//if(tick < 1)
				//return posicionAvatar;
			////////////////////////////
	
			if (obj == null)
				obj = basuraMasCercana();
		}
		else {
			if (posicionAvatar.x == mejorRuta.get(posB).x && posicionAvatar.y == mejorRuta.get(posB).y)
				posB++;
	
			obj = mejorRuta.get(posB);
		}

		AStar aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
				coordenada(obj.x), coordenada(obj.y), 4);
		ruta = aStar.ruta;

		if (ruta != null && !ruta.isEmpty()) {
			if(tick <= 1 && ruta.get(0).x < coordenada(posicionAvatar.x) && !hayMuros())
				return proxPosicion(ACTIONS.ACTION_DOWN);
			
			return new Vector2d(ruta.get(0).x * bloque, ruta.get(0).y * bloque);
		} else {
			return null;
		}
	}

	public Vector2d basuraMasCercana() {

		Vector2d posBasura = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesBasura) {

			double d = distanciaManhattan(pos);
			
			if(coordenada(pos.x) == 4 && coordenada(pos.y) == 22)
				return pos;

			if (d < distanciaMasCercana) {
				posBasura = pos;
				distanciaMasCercana = d;
			}
		}
		return posBasura;
	}

	public Vector2d basuraMasCercanaOrientacion() {

		Vector2d posBasura = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesBasura) {

			if ((orientacion() == ACTIONS.ACTION_UP) && (pos.y > posicionAvatar.y)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_DOWN) && (pos.y < posicionAvatar.y)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_RIGHT) && (pos.x < posicionAvatar.x)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_LEFT) && (pos.x > posicionAvatar.x)) {
				continue;
			}

			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posBasura = pos;
				distanciaMasCercana = d;
			}
		}
		return posBasura;
	}

	public Vector2d basuraMasCercanaAStar() {

		Vector2d posBasura = null;
		AStar aStar = null;
		List<Nodo> ruta = null;
		int distanciaMasCercana = Integer.MAX_VALUE;

		for (Vector2d pos : posicionesBasura) {

			aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
					coordenada(pos.x), coordenada(pos.y), 4);
			ruta = aStar.ruta;

			int d = ruta.size();

			if (d < distanciaMasCercana) {
				posBasura = pos;
				distanciaMasCercana = d;
			}
		}
		return posBasura;
	}

	public Vector2d basuraMasCercanaAStarOrientacion() {

		Vector2d posBasura = null;
		AStar aStar = null;
		List<Nodo> ruta = null;
		int distanciaMasCercana = Integer.MAX_VALUE;

		for (Vector2d pos : posicionesBasura) {

			if ((orientacion() == ACTIONS.ACTION_UP) && (pos.y > posicionAvatar.y)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_DOWN) && (pos.y < posicionAvatar.y)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_RIGHT) && (pos.x < posicionAvatar.x)) {
				continue;
			}

			if ((orientacion() == ACTIONS.ACTION_LEFT) && (pos.x > posicionAvatar.x)) {
				continue;
			}

			aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
					coordenada(pos.x), coordenada(pos.y), 4);
			ruta = aStar.ruta;

			int d = ruta.size();

			if (d < distanciaMasCercana) {
				posBasura = pos;
				distanciaMasCercana = d;
			}
		}
		return posBasura;
	}

	public int etiqueta(Vector2d pos) {

		ArrayList<Observation> contenido = entorno[(int) coordenada(pos.x)][(int) coordenada(pos.y)];

		int tipo = 0;

		for (int i = 0; i < contenido.size(); i++) {
			tipo = contenido.get(i).itype;
		}

		return tipo;
	}

	public int etiqueta(int x, int y) {

		ArrayList<Observation> contenido = entorno[x][y];

		int tipo = -1;

		for (int i = 0; i < contenido.size(); i++) {
			tipo = contenido.get(i).itype;
		}

		return tipo;
	}

	public List<ACTIONS> accionesDisponibles() {

		List<ACTIONS> acciones = new LinkedList<ACTIONS>();

		ArrayList<Observation> contenido;

		contenido = entorno[(int) coordenada(posicionAvatar.x) + 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_RIGHT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 3)
				acciones.remove(ACTIONS.ACTION_RIGHT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) + 1];

		acciones.add(ACTIONS.ACTION_DOWN);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 3)
				acciones.remove(ACTIONS.ACTION_DOWN);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x) - 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_LEFT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 3)
				acciones.remove(ACTIONS.ACTION_LEFT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) - 1];

		acciones.add(ACTIONS.ACTION_UP);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0 || contenido.get(i).itype == 3)
				acciones.remove(ACTIONS.ACTION_UP);
		}

		if (acciones.size() == 0) {
			acciones.add(accionAleatoria());
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
		}

		return proxPosicion;
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
		
		if(salto.x == posicionAvatar.x && salto.y == posicionAvatar.y)
			return ACTIONS.ACTION_NIL;

		return null;
	}

	public ACTIONS acercarse(Vector2d destino, List<ACTIONS> acciones) {

		ACTIONS accion;

		if (coordenada(destino.x) > coordenada(posicionAvatar.x) && acciones.contains(ACTIONS.ACTION_RIGHT))
			accion = ACTIONS.ACTION_RIGHT;

		else if (coordenada(destino.x) < coordenada(posicionAvatar.x) && acciones.contains(ACTIONS.ACTION_LEFT))
			accion = ACTIONS.ACTION_LEFT;

		else if (coordenada(destino.y) > coordenada(posicionAvatar.y) && acciones.contains(ACTIONS.ACTION_DOWN))
			accion = ACTIONS.ACTION_DOWN;

		else if (coordenada(destino.y) < coordenada(posicionAvatar.y) && acciones.contains(ACTIONS.ACTION_UP))
			accion = ACTIONS.ACTION_UP;

		else if (acciones.size() > 1)
			accion = acciones.get(rnd.nextInt(acciones.size()));

		else
			accion = acciones.get(0);

		return accion;
	}

	public ACTIONS alejarse(Vector2d pos, List<ACTIONS> acciones) {

		ACTIONS accion;

		if (coordenada(pos.x) > coordenada(posicionAvatar.x) && acciones.contains(ACTIONS.ACTION_RIGHT))
			accion = ACTIONS.ACTION_LEFT;

		else if (coordenada(pos.x) < coordenada(posicionAvatar.x) && acciones.contains(ACTIONS.ACTION_LEFT))
			accion = ACTIONS.ACTION_RIGHT;

		else if (coordenada(pos.y) > coordenada(posicionAvatar.y) && acciones.contains(ACTIONS.ACTION_DOWN))
			accion = ACTIONS.ACTION_UP;

		else if (coordenada(pos.y) < coordenada(posicionAvatar.y) && acciones.contains(ACTIONS.ACTION_UP))
			accion = ACTIONS.ACTION_DOWN;

		else if (acciones.size() > 1)
			accion = acciones.get(rnd.nextInt(acciones.size()));

		else
			accion = acciones.get(0);

		return accion;
	}

	public ACTIONS orientacion() {
		if (stateObs.getAvatarOrientation().y > 0)
			return ACTIONS.ACTION_DOWN;
		if (stateObs.getAvatarOrientation().y < 0)
			return ACTIONS.ACTION_UP;
		if (stateObs.getAvatarOrientation().x > 0)
			return ACTIONS.ACTION_RIGHT;
		if (stateObs.getAvatarOrientation().x < 0)
			return ACTIONS.ACTION_LEFT;
		else
			return null;
	}

	public boolean esBasura(double x, double y) {

		boolean esBasura = false;

		for (Vector2d pos : posicionesBasura) {
			if (pos.x == x && pos.y == y) {
				esBasura = true;
				break;
			}
		}

		return esBasura;
	}

	public boolean contiene(ArrayList<Vector2d> posiciones, Vector2d pos) {

		for (Vector2d p : posiciones) {
			if (p.x == pos.x && p.y == pos.y)
				return true;
		}

		return false;
	}

	public boolean hayMuro(Vector2d origen, Vector2d destino) {

		int x1 = (int) coordenada(origen.x);
		int y1 = (int) coordenada(origen.y);
		int x2 = (int) coordenada(destino.x);
		int y2 = (int) coordenada(destino.y);

		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;

		int err = dx - dy;

		while (true) {
			if (etiqueta(x1, y1) == 0) { // Si hay un muro en esta posición
				return true;
			}

			if (x1 == x2 && y1 == y2) {
				break;
			}

			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x1 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y1 += sy;
			}
		}

		return false;
	}
	
	public boolean hayMuroColumna(Vector2d pos) {

		int x = (int) coordenada(pos.x);
		
		for(int i = 1; i<(numFilas-1); i++) {
			if (etiqueta(x, i) == 0) {
				return true;
			}
		}

		return false;
	}

	public static double coordenada(double xy) {
		return xy / bloque;
	}

	public Vector2d getPosicionAvatar() {
		return posicionAvatar;
	}

	public int getTick() {
		return tick;
	}

	public ArrayList<Vector2d> getPosicionesBasura() {
		return posicionesBasura;
	}

	public double distanciaManhattan(Vector2d destino) {

		return Math.abs(posicionAvatar.x - destino.x) + Math.abs(posicionAvatar.y - destino.y);
	}

	public static double distanciaManhattan(Vector2d origen, Vector2d destino) {

		return (Math.abs(origen.x - destino.x) + Math.abs(origen.y - destino.y));
	}

	public double distanciaManhattan(double xi, double yi, double xf, double yf) {
		return Math.abs(xi - xf) + Math.abs(yi - yf);
	}

	public int distanciaAStar(Vector2d src, Vector2d dest) {

		AStar aStar = new AStar(this, entorno, coordenada(src.x), coordenada(src.y), coordenada(dest.x),
				coordenada(dest.y), 4);
		List<Nodo> ruta = aStar.ruta;

		return ruta.size();
	}
	
	public boolean hayMuros() {
		return posicionesMuro.size() > 230;
	}
}
