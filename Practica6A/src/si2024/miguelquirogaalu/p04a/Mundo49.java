package si2024.miguelquirogaalu.p04a;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class Mundo49 {

	private boolean visualizar = false;

	private ArrayList<Observation>[][] entorno;
	private Vector2d posicionAvatar;
	private Vector2d posicionPuerta;
	private ArrayList<Vector2d> posicionesNenufarIzq, posicionesNenufarDer;
	private ArrayList<Vector2d> posicionesFlechaArriba, posicionesFlechaAbajo, posicionesFlechaDer, posicionesFlechaIzq;
	private ArrayList<Vector2d> posicionesSpawnNenufarIzq, posicionesSpawnNenufarDer;
	private ArrayList<Vector2d> posicionesAvatar;
	public ArrayList<Vector2d> posicionesProhibidas;
	public static int bloque;
	private int numFilas;
	private int numColumnas;
	private StateObservation stateObs;
	private Random rnd;
	private boolean volando;
	private int tick;
	private double dPI;

	public Mundo49(StateObservation stateObs) {

		this.stateObs = stateObs;

		bloque = stateObs.getBlockSize();
		entorno = stateObs.getObservationGrid();

		numFilas = entorno[0].length;
		numColumnas = entorno.length;

		posicionAvatar = stateObs.getAvatarPosition();

		rnd = new Random(System.currentTimeMillis());
		
		posicionesProhibidas = new ArrayList<Vector2d>();
	}

	public void actualizar(StateObservation stateObs) {

		this.stateObs = stateObs;

		tick = stateObs.getGameTick();

		rnd = new Random(System.currentTimeMillis());

		entorno = stateObs.getObservationGrid();
		posicionAvatar = stateObs.getAvatarPosition();

		posicionesNenufarIzq = new ArrayList<Vector2d>();
		posicionesNenufarDer = new ArrayList<Vector2d>();
		posicionesFlechaArriba = new ArrayList<Vector2d>();
		posicionesFlechaAbajo = new ArrayList<Vector2d>();
		posicionesFlechaDer = new ArrayList<Vector2d>();
		posicionesFlechaIzq = new ArrayList<Vector2d>();
		posicionesSpawnNenufarIzq = new ArrayList<Vector2d>();
		posicionesSpawnNenufarDer = new ArrayList<Vector2d>();
		posicionesAvatar = new ArrayList<Vector2d>();

		volando = false;

		if (visualizar) {
			System.out.println("\n\n*********************************************\n");
			System.out.println("Tick: " + tick);
			System.out.println("Tamaño bloque: " + getBloque());
			System.out.println("Numero filas: " + numFilas);
			System.out.println("Numero de columnas: " + numColumnas);
			System.out.println("Tamaño del entorno: " + (getBloque() * numColumnas) + "x" + (getBloque() * numFilas));
			System.out.println("Posicion Avatar [F, C]: [" + coordenada(posicionAvatar.y) + ", "
					+ coordenada(posicionAvatar.x) + "]");
			System.out.println("Orientacion Avatar: " + orientacion());
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

							if (propiedades.itype >= 22 && propiedades.itype <= 25)
								volando = true;

							posicionesAvatar.add(propiedades.position);

							celda = "X";
						} else {
							switch (propiedades.itype) {
							// MUROS
							case 0:
								celda = "#";
								break;
							// AGUA
							case 3:
								celda = "@";
								break;
							// PUERTA
							case 5:
								posicionPuerta = propiedades.position;
								celda = "P";
								break;
							// SPAWN NENUFAR DER
							case 7:
								posicionesSpawnNenufarDer.add(propiedades.position);
								celda = "SD";
								break;
							// SPAWNS NENUFAR IZQ
							case 8:
								posicionesSpawnNenufarIzq.add(propiedades.position);
								celda = "SI";
								break;
							// NENUFAR DER
							case 10:
								posicionesNenufarDer.add(propiedades.position);
								celda = "D";
								break;
							// NENUFAR IZQ
							case 11:
								posicionesNenufarIzq.add(propiedades.position);
								celda = "I";
								break;
							// TIERRA
							case 12:
								celda = " ";
								break;
							// FLECHA ABAJO
							case 14:
								posicionesFlechaAbajo.add(propiedades.position);
								celda = "v";
								break;
							// FLECHA ARRIBA
							case 15:
								posicionesFlechaArriba.add(propiedades.position);
								celda = "^";
								break;
							// FLECHA DER
							case 16:
								posicionesFlechaDer.add(propiedades.position);
								celda = ">";
								break;
							// FLECHA IZQ
							case 17:
								posicionesFlechaIzq.add(propiedades.position);
								celda = "<";
								break;
							default:
								celda = String.valueOf(propiedades.itype);
							}
						}
					}
				} else
					celda = " ";

				celda = String.format("%3s", celda); // para que se vea más espaciado

				if (visualizar)
					System.out.print(celda);
			}
			if (visualizar)
				System.out.println("");
		}

		if (visualizar) {
			System.out.println("Volando: " + estaVolando());
			System.out.println("");
		}
		
		if(tick < 1)
			dPI = distanciaManhattan(posicionPuerta);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<Nodo> saltoAStar(Vector2d objetivo) {

		Vector2d obj = objetivo;
		
		if(obj == null) {
			if(coordenada(dPI) != 2)
				obj = posicionPuerta;
			else
				obj = new Vector2d(12*bloque, 10*bloque);
		}
			
		List<Nodo> ruta = null;

		AStar aStar = new AStar(this, entorno, Math.round(coordenada(posicionAvatar.x)), Math.round(coordenada(posicionAvatar.y)),
				coordenada(obj.x), coordenada(obj.y), 5);
		ruta = aStar.ruta;

		if (ruta != null && !ruta.isEmpty()) {

			if(visualizar) {
				for (Nodo n : ruta) {
					System.out.println("Salto: " + n.y + ", " + n.x);
				}
			}

			return ruta;
		} else {
			return null;
		}
	}

	public boolean alcanzable(Vector2d obj, int tipo) {

		List<Nodo> ruta = null;

		AStar aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
				coordenada(obj.x), coordenada(obj.y), tipo);
		ruta = aStar.ruta;

		if (ruta != null && !ruta.isEmpty())
			return true;
		else
			return false;
	}

	public Vector2d flechaMasCercana(ACTIONS dir) {

		Vector2d posFlecha = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		ArrayList<Vector2d> posicionesFlecha = null;

		switch (dir) {
		case ACTION_UP:
			posicionesFlecha = posicionesFlechaArriba;
			break;
		case ACTION_DOWN:
			posicionesFlecha = posicionesFlechaAbajo;
			break;
		case ACTION_RIGHT:
			posicionesFlecha = posicionesFlechaDer;
			break;
		case ACTION_LEFT:
			posicionesFlecha = posicionesFlechaIzq;
			break;
		default:
			break;
		}

		for (Vector2d pos : posicionesFlecha) {

			double d = distanciaManhattan(pos);

			if (coordenada(d) == 4 && coordenada(pos.x) == 6 && coordenada(pos.y) == 1)
				return pos;

			if (d < distanciaMasCercana && alcanzable(pos, etiqueta(pos))) {
				posFlecha = pos;
				distanciaMasCercana = d;
			}
		}

		// si hay alguna flecha igual al lado -> cogemos la de al lado
		Vector2d vecina = flechaVecina(posFlecha, dir);

		if (vecina != null)
			posFlecha = vecina;

		return posFlecha;
	}

	public Vector2d flechaVecina(Vector2d posFlecha, ACTIONS dir) {

		switch (dir) {
		case ACTION_UP:
			Vector2d abajo = new Vector2d(posFlecha.x, posFlecha.y + bloque);

			if (etiqueta(abajo) == 15)
				return abajo;

			break;
		case ACTION_DOWN:
			Vector2d arriba = new Vector2d(posFlecha.x, posFlecha.y - bloque);

			if (etiqueta(arriba) == 14)
				return arriba;

			break;
		case ACTION_RIGHT:
			Vector2d izq = new Vector2d(posFlecha.x - bloque, posFlecha.y);

			if (etiqueta(izq) == 16)
				return izq;

			break;
		case ACTION_LEFT:
			Vector2d der = new Vector2d(posFlecha.x + bloque, posFlecha.y);

			if (etiqueta(der) == 17)
				return der;

			break;
		default:
			break;
		}

		return null;
	}

	public int etiqueta(Vector2d pos) {

		ArrayList<Observation> contenido = entorno[(int) coordenada(pos.x)][(int) coordenada(pos.y)];

		int tipo = 1;

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
			if (orientacion().equals(accion))
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y + bloque);
			else
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		case ACTION_UP:
			if (orientacion().equals(accion))
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y - bloque);
			else
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		case ACTION_RIGHT:
			if (orientacion().equals(accion))
				proxPosicion = new Vector2d(posicionAvatar.x + bloque, posicionAvatar.y);
			else
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		case ACTION_LEFT:
			if (orientacion().equals(accion))
				proxPosicion = new Vector2d(posicionAvatar.x - bloque, posicionAvatar.y);
			else
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		case ACTION_NIL:
			proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y);
			break;
		default:
			break;
		}

		return proxPosicion;
	}

	public Vector2d proxPosicionSinOrientacion(ACTIONS accion) {

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

	public ACTIONS proxAccion(Vector2d salto) {

		if (Math.abs(salto.x - posicionAvatar.x) >= bloque && salto.x < posicionAvatar.x)
			return ACTIONS.ACTION_LEFT;

		if (Math.abs(salto.x - posicionAvatar.x) >= bloque && salto.x > posicionAvatar.x)
			return ACTIONS.ACTION_RIGHT;

		if (Math.abs(salto.y - posicionAvatar.y) >= bloque && salto.y < posicionAvatar.y)
			return ACTIONS.ACTION_UP;

		if (Math.abs(salto.y - posicionAvatar.y) >= bloque && salto.y > posicionAvatar.y)
			return ACTIONS.ACTION_DOWN;

		return null;
	}
	
	public ACTIONS proxAccion(Vector2d origen, Vector2d destino) {

		if (Math.abs(destino.x - origen.x) >= bloque && destino.x < origen.x)
			return ACTIONS.ACTION_LEFT;

		if (Math.abs(destino.x - origen.x) >= bloque && destino.x > origen.x)
			return ACTIONS.ACTION_RIGHT;

		if (Math.abs(destino.y - origen.y) >= bloque && destino.y < origen.y)
			return ACTIONS.ACTION_UP;

		if (Math.abs(destino.y - origen.y) >= bloque && destino.y > origen.y)
			return ACTIONS.ACTION_DOWN;

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

	public boolean hayNenufar(int x, int y) {

		boolean hayNenufar = false;

		ArrayList<Vector2d> posiciones = posicionesSpawnNenufarDer;

		for (Vector2d pos : posiciones) {

			if (coordenada(pos.y) == y) {

				hayNenufar = true;

				for (int i = 1; i < Math.abs(coordenada(pos.x) - x); i++) {
					
					double newX = pos.x + (i * bloque);
					
					if (coordenada(newX) < numColumnas && etiqueta(new Vector2d(newX, pos.y)) == 0) {
						hayNenufar = false;
						break;
					}
				}
			}

			if (hayNenufar)
				return true;
		}

		posiciones = posicionesSpawnNenufarIzq;

		for (Vector2d pos : posiciones) {

			if (coordenada(pos.y) == y) {

				hayNenufar = true;

				for (int i = 1; i < Math.abs(coordenada(pos.x) - x); i++) {
					
					double newX = pos.x - (i * bloque);
					
					if (coordenada(newX) >= 0 && etiqueta(new Vector2d(newX, pos.y)) == 0) {
						hayNenufar = false;
						break;
					}
				}
			}

			if (hayNenufar)
				return true;
		}

		/*
		 * posiciones = posicionesNenufarDer;
		 * 
		 * for (Vector2d pos : posiciones) { if (Mundo49.coordenada(pos.y) == y) return
		 * true; }
		 * 
		 * posiciones = posicionesNenufarIzq;
		 * 
		 * for (Vector2d pos : posiciones) { if (Mundo49.coordenada(pos.y) == y) return
		 * true; }
		 */

		return false;
	}

	public boolean hayFlecha(int x, int y) {

		boolean hayFlecha = false;

		ArrayList<Vector2d> posicionesFlecha = posicionesFlechaAbajo;

		for (Vector2d pos : posicionesFlecha) {
			if (Mundo49.coordenada(pos.x) == x && Mundo49.coordenada(pos.y) <= y
					&& flechaValida(pos, ACTIONS.ACTION_DOWN)) {

				hayFlecha = true;

				for (int i = 1; i < Math.abs(coordenada(pos.y) - y); i++) {
					if (etiqueta(new Vector2d(pos.x, pos.y + (i * bloque))) == 0) {
						hayFlecha = false;
						break;
					}
				}
			}

			if (hayFlecha)
				return true;
		}

		posicionesFlecha = posicionesFlechaArriba;

		for (Vector2d pos : posicionesFlecha) {
			if (Mundo49.coordenada(pos.x) == x && Mundo49.coordenada(pos.y) >= y
					&& flechaValida(pos, ACTIONS.ACTION_UP)) {

				hayFlecha = true;

				for (int i = 1; i < Math.abs(coordenada(pos.y) - y); i++) {
					if (etiqueta(new Vector2d(pos.x, pos.y - (i * bloque))) == 0) {
						hayFlecha = false;
						break;
					}
				}
			}

			if (hayFlecha)
				return true;
		}

		posicionesFlecha = posicionesFlechaDer;

		for (Vector2d pos : posicionesFlecha) {
			if (Mundo49.coordenada(pos.y) == y && Mundo49.coordenada(pos.x) <= x
					&& flechaValida(pos, ACTIONS.ACTION_RIGHT)) {

				hayFlecha = true;

				for (int i = 1; i < Math.abs(coordenada(pos.x) - x); i++) {
					if (etiqueta(new Vector2d(pos.x + (i * bloque), pos.y)) == 0) {
						hayFlecha = false;
						break;
					}
				}
			}

			if (hayFlecha)
				return true;
		}

		posicionesFlecha = posicionesFlechaIzq;

		for (Vector2d pos : posicionesFlecha) {
			if (Mundo49.coordenada(pos.y) == y && Mundo49.coordenada(pos.x) >= x
					&& flechaValida(pos, ACTIONS.ACTION_LEFT)) {

				hayFlecha = true;

				for (int i = 1; i < Math.abs(coordenada(pos.x) - x); i++) {
					if (etiqueta(new Vector2d(pos.x - (i * bloque), pos.y)) == 0) {
						hayFlecha = false;
						break;
					}
				}
			}

			if (hayFlecha)
				return true;
		}

		return false;
	}

	public boolean flechaValida(Vector2d posFlecha, ACTIONS dir) { // si la flecha acaba en agua no es valida

		switch (dir) {
		case ACTION_DOWN:
			for (int i = 1; i <= (numFilas - coordenada(posFlecha.y)); i++) {

				double newY = posFlecha.y + (i * bloque);

				if (coordenada(newY) < numFilas && etiqueta(new Vector2d(posFlecha.x, newY)) == 0) {
					if (etiqueta(new Vector2d(posFlecha.x, posFlecha.y + ((i - 1) * bloque))) == 3) {
						return false;
					} else
						return true;
				} else if (coordenada(newY) < numFilas && etiqueta(new Vector2d(posFlecha.x, newY)) >= 14
						&& etiqueta(new Vector2d(posFlecha.x, newY)) <= 17) {
					return true;
				}
			}
			break;
		case ACTION_UP:
			for (int i = 1; i <= coordenada(posFlecha.y); i++) {

				double newY = posFlecha.y - (i * bloque);

				if (coordenada(newY) >= 0 && etiqueta(new Vector2d(posFlecha.x, newY)) == 0) {
					if (etiqueta(new Vector2d(posFlecha.x, posFlecha.y - ((i - 1) * bloque))) == 3) {
						return false;
					} else
						return true;
				} else if (coordenada(newY) >= 0 && etiqueta(new Vector2d(posFlecha.x, newY)) >= 14
						&& etiqueta(new Vector2d(posFlecha.x, newY)) <= 17) {
					return true;
				}
			}
			break;
		case ACTION_RIGHT:
			for (int i = 1; i <= (numColumnas - coordenada(posFlecha.x)); i++) {

				double newX = posFlecha.x + (i * bloque);

				if (coordenada(newX) < numColumnas && etiqueta(new Vector2d(newX, posFlecha.y)) == 0) {
					if (etiqueta(new Vector2d(posFlecha.x + ((i - 1) * bloque), posFlecha.y)) == 3) {
						return false;
					} else
						return true;
				} else if (coordenada(newX) < numColumnas && etiqueta(new Vector2d(newX, posFlecha.y)) >= 14
						&& etiqueta(new Vector2d(newX, posFlecha.y)) <= 17) {
					return true;
				}
			}
			break;
		case ACTION_LEFT:
			for (int i = 1; i <= coordenada(posFlecha.x); i++) {

				double newX = posFlecha.x - (i * bloque);

				if (coordenada(newX) >= 0 && etiqueta(new Vector2d(newX, posFlecha.y)) == 0) {
					if (etiqueta(new Vector2d(posFlecha.x - ((i - 1) * bloque), posFlecha.y)) == 3) {
						return false;
					} else
						return true;
				} else if (coordenada(newX) >= 0 && etiqueta(new Vector2d(newX, posFlecha.y)) >= 14
						&& etiqueta(new Vector2d(newX, posFlecha.y)) <= 17) {
					return true;
				}
			}
			break;
		default:
			break;
		}

		return true;
	}

	public ACTIONS direccionFlecha(Vector2d flecha) {
		
		switch (etiqueta(flecha)) {
		case 14:
			return ACTIONS.ACTION_DOWN;
		case 15:
			return ACTIONS.ACTION_UP;
		case 16:
			return ACTIONS.ACTION_RIGHT;
		case 17:
			return ACTIONS.ACTION_LEFT;
		}

		return null;
	}
	
	public boolean nenufarEstorbaFlecha (Vector2d posFlecha) {
		
		ACTIONS dir = direccionFlecha(posFlecha);
		
		int i = 1;
		double newY = 0;
		//double newX = 0;
		int etiquetaPos = 0;
		
		switch(dir) {
		case ACTION_UP:
			newY = posFlecha.y - (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			
			while (coordenada(newY) >= 0 && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newY = posFlecha.y - (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			}
			break;
		case ACTION_DOWN:
			newY = posFlecha.y + (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			
			while (coordenada(newY) < numFilas && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newY = posFlecha.y + (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			}
			break;
			/*
		case ACTION_LEFT:
			newX = posFlecha.x - (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			
			while (coordenada(newX) >= 0 && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newX = posFlecha.x - (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			}
			break;
		case ACTION_RIGHT:
			newX = posFlecha.x + (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			
			while (coordenada(newX) < numColumnas && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newX = posFlecha.x + (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			}
			break;
			*/
		default:
			break;
		}
		
		return false;
	}
	
public boolean hayNenufarColumnaFlecha (Vector2d posFlecha, int etiquetaNenufar) {
		
		ACTIONS dir = direccionFlecha(posFlecha);
		
		int i = 1;
		double newY = 0;
		//double newX = 0;
		int etiquetaPos = 0;
		
		switch(dir) {
		case ACTION_UP:
			newY = posFlecha.y - (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			
			while (coordenada(newY) >= 0 && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == etiquetaNenufar) {
					return true;
				}
				
				i++;
				newY = posFlecha.y - (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			}
			break;
		case ACTION_DOWN:
			newY = posFlecha.y + (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			
			while (coordenada(newY) < numFilas && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == etiquetaNenufar) {
					return true;
				}
				
				i++;
				newY = posFlecha.y + (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(posFlecha.x, newY));
			}
			break;
			/*
		case ACTION_LEFT:
			newX = posFlecha.x - (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			
			while (coordenada(newX) >= 0 && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newX = posFlecha.x - (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			}
			break;
		case ACTION_RIGHT:
			newX = posFlecha.x + (i * bloque);
			etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			
			while (coordenada(newX) < numColumnas && etiquetaPos != 0 && etiquetaPos != 12) {

				if (etiquetaPos == 10 || etiquetaPos == 11) {
					return true;
				}
				
				i++;
				newX = posFlecha.x + (i * bloque);
				etiquetaPos = etiqueta(new Vector2d(newX, posFlecha.y));
			}
			break;
			*/
		default:
			break;
		}
		
		return false;
	}

	public boolean enNenufarIzq() {

		ArrayList<Observation> contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(
				posicionAvatar.y)];

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 11)
				return true;
		}

		return false;
	}

	public boolean enNenufarDer() {

		ArrayList<Observation> contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(
				posicionAvatar.y)];

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 10)
				return true;
		}

		return false;
	}

	public static double coordenada(double xy) {
		return xy / bloque;
	}

	public Vector2d getPosicionAvatar() {
		return posicionAvatar;
	}

	public double distanciaManhattan(Vector2d destino) {

		return Math.abs(posicionAvatar.x - destino.x) + Math.abs(posicionAvatar.y - destino.y);
	}

	public double distanciaManhattan(Vector2d origen, Vector2d destino) {

		return (Math.abs(origen.x - destino.x) + Math.abs(origen.y - destino.y));
	}

	public double distanciaManhattan(double xi, double yi, double xf, double yf) {
		return Math.abs(xi - xf) + Math.abs(yi - yf);
	}

	public Vector2d getPosicionPuerta() {
		return posicionPuerta;
	}

	public ArrayList<Vector2d> getPosicionesNenufarIzq() {
		return posicionesNenufarIzq;
	}

	public ArrayList<Vector2d> getPosicionesNenufarDer() {
		return posicionesNenufarDer;
	}

	public ArrayList<Vector2d> getPosicionesFlechaArriba() {
		return posicionesFlechaArriba;
	}

	public ArrayList<Vector2d> getPosicionesFlechaAbajo() {
		return posicionesFlechaAbajo;
	}

	public ArrayList<Vector2d> getPosicionesFlechaDer() {
		return posicionesFlechaDer;
	}

	public ArrayList<Vector2d> getPosicionesFlechaIzq() {
		return posicionesFlechaIzq;
	}

	public ArrayList<Vector2d> getPosicionesSpawnNenufarIzq() {
		return posicionesSpawnNenufarIzq;
	}

	public ArrayList<Vector2d> getPosicionesSpawnNenufarDer() {
		return posicionesSpawnNenufarDer;
	}

	public ArrayList<Vector2d> getPosicionesAvatar() {
		return posicionesAvatar;
	}

	public ArrayList<Vector2d> getPosicionesProhibidas() {
		return posicionesProhibidas;
	}

	public void setPosicionesProhibidas(ArrayList<Vector2d> posicionesProhibidas) {
		this.posicionesProhibidas = posicionesProhibidas;
	}

	public int getTick() {
		return tick;
	}

	public double getdPI() {
		return dPI;
	}

	public boolean visualizar() {
		return visualizar;
	}

	public boolean estaVolando() {
		return volando;
	}
}
