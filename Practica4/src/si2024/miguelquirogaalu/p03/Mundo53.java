package si2024.miguelquirogaalu.p03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class Mundo53 implements Mundo {

	private ArrayList<Observation>[][] entorno;
	private Vector2d posicionAvatar;
	private ArrayList<Vector2d> posicionesSanos;
	private ArrayList<Vector2d> posicionesVirus;
	private ArrayList<Vector2d> posicionesMocos;
	private ArrayList<Vector2d> posicionesEnfermeras;
	private ArrayList<Vector2d> posicionesInfectados;
	public static int bloque;
	private int numFilas;
	private int numColumnas;
	private int numEnfermeras;
	private int numSanos;
	private int numPuertas;
	private StateObservation stateObs;
	private boolean visualizar = false;
	private Random rnd;

	public static double coordenada(double xy) {
		return xy / bloque;
	}

	public Vector2d getPosicionAvatar() {
		return posicionAvatar;
	}

	public int getNumEnfermeras() {
		return numEnfermeras;
	}

	public int getNumSanos() {
		return numSanos;
	}

	public int getNumPuertas() {
		return numPuertas;
	}

	public ArrayList<Vector2d> getPosicionesEnfermeras() {
		return posicionesEnfermeras;
	}

	public ArrayList<Vector2d> getPosicionesVirus() {
		return posicionesVirus;
	}

	public ArrayList<Vector2d> getPosicionesSanos() {
		return posicionesSanos;
	}

	public ArrayList<Vector2d> getPosicionesInfectados() {
		return posicionesInfectados;
	}
	
	public ArrayList<Vector2d> getPosicionesMocos() {
		return posicionesMocos;
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

	public boolean estoyInfectado() {
		if (stateObs.getAvatarType() == 7)
			return false;
		else
			return true;
	}

	public Mundo53(StateObservation stateObs) {

		this.stateObs = stateObs;

		bloque = stateObs.getBlockSize();
		entorno = stateObs.getObservationGrid();

		numFilas = entorno[0].length;
		numColumnas = entorno.length;

		numEnfermeras = 0;
		numSanos = 0;
		numPuertas = 0;

		posicionAvatar = stateObs.getAvatarPosition();

		posicionesSanos = new ArrayList<Vector2d>();
		posicionesVirus = new ArrayList<Vector2d>();
		posicionesEnfermeras = new ArrayList<Vector2d>();
		posicionesMocos = new ArrayList<Vector2d>();
		posicionesInfectados = new ArrayList<Vector2d>();

		rnd = new Random(System.currentTimeMillis());
	}

	@Override
	public void analizarMundo(StateObservation stateObs) {

		this.stateObs = stateObs;

		rnd = new Random(System.currentTimeMillis());

		numEnfermeras = 0;
		numSanos = 0;
		numPuertas = 0;

		entorno = stateObs.getObservationGrid();
		posicionAvatar = stateObs.getAvatarPosition();

		if (visualizar) {
			System.out.println("\n\n*********************************************\n");
			System.out.println("Tamaño bloque: " + getBloque());
			System.out.println("Numero filas: " + numFilas);
			System.out.println("Numero de columnas: " + numColumnas);
			System.out.println("Tamaño del entorno: " + (getBloque() * numColumnas) + "x" + (getBloque() * numFilas));
			System.out.println("Posicion Avatar [F, C]: [" + coordenada(posicionAvatar.y) + ", "
					+ coordenada(posicionAvatar.x) + "]");
			System.out.println("Orientación Avatar [F, C]: [" + coordenada(stateObs.getAvatarOrientation().y) + ", "
					+ coordenada(stateObs.getAvatarOrientation().x) + "]");
			System.out.println("¿Avatar infectado? " + estoyInfectado());
			System.out.println("");
		}

		posicionesSanos = new ArrayList<Vector2d>();
		posicionesVirus = new ArrayList<Vector2d>();
		posicionesEnfermeras = new ArrayList<Vector2d>();
		posicionesMocos = new ArrayList<Vector2d>();
		posicionesInfectados = new ArrayList<Vector2d>();

		for (int y = 0; y < numFilas; y++) {
			for (int x = 0; x < numColumnas; x++) {

				ArrayList<Observation> contenido = entorno[x][y];
				String celda = null;

				if (x == 0)
					System.out.print(" ");

				if (contenido.size() > 0) {

					for (Observation propiedades : contenido) {

						if (propiedades.position.x == posicionAvatar.x && propiedades.position.y == posicionAvatar.y)
							celda = "X";
						else {
							switch (propiedades.itype) {
							// MURO
							case 0:
								celda = "#";
								break;
							// PUERTA
							case 4:
								celda = "P";
								numPuertas++;
								break;
							// MOCOS
							case 5:
								posicionesVirus.add(propiedades.position);
								posicionesMocos.add(propiedades.position);
								celda = "V";
								break;
							// SANO
							case 10:
								posicionesSanos.add(propiedades.position);
								numSanos++;
								celda = "S";
								break;
							// INFECTADO
							case 11:
								posicionesVirus.add(propiedades.position);
								posicionesInfectados.add(propiedades.position);
								celda = "I";
								break;
							// ENFERMERA
							case 12:
								posicionesEnfermeras.add(propiedades.position);
								numEnfermeras++;
								celda = "E";
								break;
							default:
								celda = String.valueOf(propiedades.itype);
							}
						}
					}
				} else
					celda = " ";

				// celda = String.format("%3s", celda); // para que se vea más espaciado

				if (visualizar)
					System.out.print(celda);
			}
			if (visualizar)
				System.out.println("");
		}

		if (visualizar) {
			System.out.println("");

			if (numEnfermeras > 0)
				System.out.println("Posicion enfermera mas cercana [F, C]: [" + coordenada(enfermeraMasCercana().y)
						+ ", " + coordenada(enfermeraMasCercana().x) + "]");
		}
	}

	public Vector2d enfermeraMasCercana() {

		Vector2d posEnfermera = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		ArrayList<Vector2d> posEnfermerasEnPeligro = posEnfermerasEnPeligro();

		if (posEnfermerasEnPeligro.size() > 0) {

			for (Vector2d pos : posEnfermerasEnPeligro) {

				double d = distanciaManhattan(pos);

				if (d < distanciaMasCercana) {
					posEnfermera = pos;
					distanciaMasCercana = d;
				}
			}
		} 
		else {
			
			for (Vector2d pos : posicionesEnfermeras) {

				double d = distanciaManhattan(pos);

				if (d < distanciaMasCercana) {
					posEnfermera = pos;
					distanciaMasCercana = d;
				}
			}
		}
		
		return posEnfermera;
	}

	public Vector2d saltoAStar(int tipoObj) {

		Vector2d obj = null;

		if (tipoObj == 12)
			obj = enfermeraMasCercana();

		if (tipoObj == 5 || tipoObj == 11)
			obj = virusMasCercano();

		if (tipoObj == 10)
			obj = sanoMasCercano();

		AStar aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y),
				coordenada(obj.x), coordenada(obj.y), tipoObj);

		aStar.encontrarRuta();
		aStar.ruta();

		if (!aStar.ruta.isEmpty())
			return new Vector2d(aStar.ruta.get(0).x * bloque, aStar.ruta.get(0).y * bloque);
		else {
			return null;
		}
	}

	public Vector2d sanoMasCercano() {

		Vector2d posSano = null;
		double distanciaMasCercana = Double.MAX_VALUE;
		/*
		ArrayList<Vector2d> posSanosEnPeligro = posSanosEnPeligro();

		if (posSanosEnPeligro.size() > 0) {

			for (Vector2d pos : posSanosEnPeligro) {

				double d = distanciaManhattan(pos);

				if (d < distanciaMasCercana) {
					posSano = pos;
					distanciaMasCercana = d;
				}
			}
		} 
		else {
		 	*/
			for (Vector2d pos : posicionesSanos) {
	
				double d = distanciaManhattan(pos);
	
				if (d < distanciaMasCercana) {
					posSano = pos;
					distanciaMasCercana = d;
				}
			//}
		}
		
		return posSano;
	}

	public Vector2d sanoMasCercano(Vector2d origen) {

		Vector2d posSano = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesSanos) {

			double d = distanciaManhattan(origen, pos);

			if (d < distanciaMasCercana) {
				posSano = pos;
				distanciaMasCercana = d;
			}
		}
		return posSano;
	}

	public Vector2d virusMasCercano() {

		Vector2d posVirus = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesVirus) {

			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posVirus = pos;
				distanciaMasCercana = d;
			}
		}
		return posVirus;
	}

	public ArrayList<Vector2d> posEnfermerasEnPeligro() {

		ArrayList<Vector2d> posEnfermerasEnPeligro = new ArrayList<Vector2d>();

		for (Vector2d posEnf : posicionesEnfermeras) {

			for (Vector2d posMoco : posicionesMocos) {

				if (distanciaManhattan(posEnf, posMoco) < 3)
					posEnfermerasEnPeligro.add(posEnf);
			}
		}

		return posEnfermerasEnPeligro;
	}
	
	public ArrayList<Vector2d> posSanosEnPeligro() {

		ArrayList<Vector2d> posSanosEnPeligro = new ArrayList<Vector2d>();

		for (Vector2d posSano : posicionesSanos) {

			for (Vector2d posVirus : posicionesVirus) {

				if (distanciaManhattan(posSano, posVirus) < 3)
					posSanosEnPeligro.add(posSano);
			}
		}

		return posSanosEnPeligro;
	}

	public List<ACTIONS> accionesDisponibles() {

		List<ACTIONS> acciones = new LinkedList<ACTIONS>();

		ArrayList<Observation> contenido;

		contenido = entorno[(int) coordenada(posicionAvatar.x) + 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_RIGHT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0)
				acciones.remove(ACTIONS.ACTION_RIGHT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) + 1];

		acciones.add(ACTIONS.ACTION_DOWN);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0)
				acciones.remove(ACTIONS.ACTION_DOWN);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x) - 1][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_LEFT);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0)
				acciones.remove(ACTIONS.ACTION_LEFT);
		}

		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) coordenada(posicionAvatar.y) - 1];

		acciones.add(ACTIONS.ACTION_UP);

		for (int i = 0; i < contenido.size(); i++) {
			if (contenido.get(i).itype == 0)
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
	
	public ACTIONS alejarse(Vector2d posVirus, List<ACTIONS> acciones) {
		
		ACTIONS accion = null;
		
		double distActual = distanciaManhattan(posVirus);
		
		for (ACTIONS a : acciones) {
			
			if(distanciaManhattan(proxPosicion(a), posVirus) > distActual) {
				accion = a;
				break;
			}
		}
		
		if(accion == null) {
			
			for (ACTIONS a : acciones) {
				
				if(distanciaManhattan(proxPosicionSinOrientacion(a), posVirus) > distActual) {
					accion = a;
					break;
				}
			}
		}
		
		if(accion == null)
			accion = ACTIONS.ACTION_NIL;

		return accion;
	}

	public double distanciaManhattan(Vector2d destino) { // agrega un bloque de distancia si la orientación no apunta al
															// destino

		double d = (Math.abs(posicionAvatar.x - destino.x) + Math.abs(posicionAvatar.y - destino.y));

		if (destino.y > posicionAvatar.y && !orientacion().equals(ACTIONS.ACTION_DOWN))
			d = d + bloque;

		if (destino.y < posicionAvatar.y && !orientacion().equals(ACTIONS.ACTION_UP))
			d = d + bloque;

		if (destino.x > posicionAvatar.x && !orientacion().equals(ACTIONS.ACTION_RIGHT))
			d = d + bloque;

		if (destino.x < posicionAvatar.x && !orientacion().equals(ACTIONS.ACTION_LEFT))
			d = d + bloque;

		return d;
	}

	public double distanciaManhattan(Vector2d origen, Vector2d destino) {

		return (Math.abs(origen.x - destino.x) + Math.abs(origen.y - destino.y));
	}

	public double distanciaManhattan(double xi, double yi, double xf, double yf) {
		return Math.abs(xi - xf) + Math.abs(yi - yf);
	}
}
