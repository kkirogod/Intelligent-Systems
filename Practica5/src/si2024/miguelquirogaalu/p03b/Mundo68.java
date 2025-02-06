package si2024.miguelquirogaalu.p03b;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.game.Observation;
import core.game.StateObservation;
import ontology.Types.ACTIONS;
import tools.Vector2d;

public class Mundo68 implements Mundo {

	private boolean visualizar = false;
	
	private ArrayList<Observation>[][] entorno;
	private Vector2d posicionAvatar;
	public static int bloque;
	private int numFilas;
	private int numColumnas;
	private ArrayList<Vector2d> posicionesAvatar;
	private ArrayList<Vector2d> posicionesBolitas;
	private ArrayList<Vector2d> posicionesBolasGrandes;
	private ArrayList<Vector2d> posicionesFantasmasColores;
	private ArrayList<Vector2d> posicionesFantasmasBlancos;
	private StateObservation stateObs;
	private boolean hayFantasmas;
	private boolean fantasmasHuyendo;
	private Random rnd;

	public static double coordenada(double xy) {
		return xy / bloque;
	}

	public Vector2d getPosicionAvatar() {
		return posicionAvatar;
	}

	public ArrayList<Vector2d> getPosBolitas() {
		return posicionesBolitas;
	}

	public ArrayList<Vector2d> getPosBolasGrandes() {
		return posicionesBolasGrandes;
	}
	
	public ArrayList<Vector2d> getPosicionesFantasmasColores() {
		return posicionesFantasmasColores;
	}

	public boolean hayFantasmasColores() {
		return hayFantasmas;
	}

	public boolean hayFantasmasHuyendo() {
		return fantasmasHuyendo;
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

	public Mundo68(StateObservation stateObs) {

		this.stateObs = stateObs;

		bloque = stateObs.getBlockSize();
		entorno = stateObs.getObservationGrid();

		numFilas = entorno[0].length;
		numColumnas = entorno.length;

		posicionAvatar = stateObs.getAvatarPosition();

		rnd = new Random(System.currentTimeMillis());
	}

	@Override
	public void actualizar(StateObservation stateObs) {

		this.stateObs = stateObs;

		rnd = new Random(System.currentTimeMillis());

		entorno = stateObs.getObservationGrid();
		posicionAvatar = stateObs.getAvatarPosition();
		
		posicionesAvatar = new ArrayList<Vector2d>();
		posicionesBolitas = new ArrayList<Vector2d>();
		posicionesFantasmasColores = new ArrayList<Vector2d>();
		posicionesFantasmasBlancos = new ArrayList<Vector2d>();
		posicionesBolasGrandes = new ArrayList<Vector2d>();
		
		hayFantasmas = false;
		fantasmasHuyendo = false;

		if (visualizar) {
			System.out.println("\n\n*********************************************\n");
			System.out.println("Tamaño bloque: " + getBloque());
			System.out.println("Numero filas: " + numFilas);
			System.out.println("Numero de columnas: " + numColumnas);
			System.out.println("Tamaño del entorno: " + (getBloque() * numColumnas) + "x" + (getBloque() * numFilas));
			System.out.println("Posicion Avatar [F, C]: [" + coordenada(posicionAvatar.y) + ", "
					+ coordenada(posicionAvatar.x) + "]");
			System.out.println("");
		}

		for (int y = 0; y < numFilas; y++) {
			for (int x = 0; x < numColumnas; x++) {

				ArrayList<Observation> contenido = entorno[x][y];
				String celda = null;

				if (x == 0)
					System.out.print(" ");

				if (contenido.size() > 0) {
					
					int i = 0;
					
					for (Observation propiedades : contenido) {

						if (i == 0) {
							if (propiedades.itype == 0) {
								// MUROS
								celda = "#";
								break;
							}
							else
								celda = " ";
						}
						else {
							if (propiedades.position.x == posicionAvatar.x && propiedades.position.y == posicionAvatar.y) {
								posicionesAvatar.add(new Vector2d(x*bloque, y*bloque));
								celda = "X";
							}
							else {
								switch (propiedades.itype) {
								// CEREZAS
								case 4:
									posicionesBolitas.add(propiedades.position);
									celda = "C";
									break;
								// BOLITAS
								case 5:
									posicionesBolitas.add(propiedades.position);
									celda = "·";
									break;
								// BOLAS GRANDES
								case 6:
									if(fantasmasLejosSpawn() || posicionesBolitas.size() == 0)
										posicionesBolitas.add(propiedades.position);
									
									posicionesBolasGrandes.add(propiedades.position);
									celda = "O";
									break;
								// SPAWNS
								case 8:
								case 9:
								case 10:
								case 11:
									celda = "S";
									break;
								// FANTASMA ROJO
								case 15:
									hayFantasmas = true;
									posicionesFantasmasColores.add(propiedades.position);
									celda = "R";
									break;
								case 16:
									fantasmasHuyendo = true;
									posicionesFantasmasBlancos.add(propiedades.position);
									celda = "R";
									break;
								// FANTASMA AZUL
								case 18:
									hayFantasmas = true;
									posicionesFantasmasColores.add(propiedades.position);
									celda = "B";
									break;
								case 19:
									fantasmasHuyendo = true;
									posicionesFantasmasBlancos.add(propiedades.position);
									celda = "B";
									break;
								// FANTASMA ROSA
								case 21:
									hayFantasmas = true;
									posicionesFantasmasColores.add(propiedades.position);
									celda = "P";
									break;
								case 22:
									fantasmasHuyendo = true;
									posicionesFantasmasBlancos.add(propiedades.position);
									celda = "P";
									break;
								// FANTASMA AMARILLO
								case 24:
									hayFantasmas = true;
									posicionesFantasmasColores.add(propiedades.position);
									celda = "Y";
									break;
								case 25:
									fantasmasHuyendo = true;
									posicionesFantasmasBlancos.add(propiedades.position);
									celda = "Y";
									break;
								default:
									celda = String.valueOf(propiedades.itype);
								}
							}
						}
						i++;
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
			Vector2d posBolitaMasCercana = bolitaMasCercana();
			System.out.println("Bolita mas cercana [F, C]: [" + coordenada(posBolitaMasCercana.y) + ", "
					+ coordenada(posBolitaMasCercana.x) + "]");
			System.out.println("");
		}
	}

	public Vector2d saltoAStar(int tipoObj) {

		Vector2d obj = null;
		List <Nodo> ruta = null;
		
		if (tipoObj == 5)
			obj = bolitaMasCercana();
		
		if (tipoObj == 6)
			obj = bolaGrandeMasCercana();
		
		if (tipoObj == 16)
			obj = fantasmaBlancoMasCercano();
		
		
		if (posicionesAvatar.size() > 1) { // cogemos la posicion mas alejada
			
			AStar aStar = new AStar(this, entorno, coordenada(posicionesAvatar.get(0).x), coordenada(posicionesAvatar.get(0).y), coordenada(obj.x), coordenada(obj.y), tipoObj);
			List <Nodo> ruta1 = aStar.ruta;
			
			aStar = new AStar(this, entorno, coordenada(posicionesAvatar.get(1).x), coordenada(posicionesAvatar.get(1).y), coordenada(obj.x), coordenada(obj.y), tipoObj);
			List <Nodo> ruta2 = aStar.ruta;
			
			if(ruta1.size() > ruta2.size())
				ruta = ruta1;
			else
				ruta = ruta2;
		}
		else {
			AStar aStar = new AStar(this, entorno, coordenada(posicionAvatar.x), coordenada(posicionAvatar.y), coordenada(obj.x), coordenada(obj.y), tipoObj);
			ruta = aStar.ruta;
		}

		
		if (ruta != null && !ruta.isEmpty())
			return new Vector2d(ruta.get(0).x * bloque, ruta.get(0).y * bloque);
		else {
			return null;
		}
	}
	
	public boolean fantasmasLejosSpawn() {
		
		Vector2d centro = new Vector2d((numColumnas/2)*bloque, (numFilas/2)*bloque);

		if (posicionesFantasmasColores.size() > 0) {
			for (Vector2d pos : posicionesFantasmasColores) {
				if (coordenada(distanciaManhattan(pos, centro)) < 6) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Vector2d fantasmaColorMasCercano() {

		Vector2d posFantasma = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesFantasmasColores) {

			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posFantasma = pos;
				distanciaMasCercana = d;
			}
		}
		return posFantasma;
	}
	
	public Vector2d fantasmaColorMasCercano(Vector2d posicion) {

		Vector2d posFantasma = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d posF : posicionesFantasmasColores) {

			double d = distanciaManhattan(posF, posicion);

			if (d < distanciaMasCercana) {
				posFantasma = posF;
				distanciaMasCercana = d;
			}
		}
		return posFantasma;
	}
	
	public Vector2d fantasmaBlancoMasCercano() {

		Vector2d posFantasma = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesFantasmasBlancos) {

			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posFantasma = pos;
				distanciaMasCercana = d;
			}
		}
		return posFantasma;
	}

	public Vector2d bolitaMasCercana() {

		Vector2d posBolita = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesBolitas) {
			
			if(posBolita != null && hayFantasmas && (distanciaManhattan(pos, fantasmaColorMasCercano(pos)) < distanciaManhattan(pos)))
				continue;
			
			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posBolita = pos;
				distanciaMasCercana = d;
			}
		}
		return posBolita;
	}
	
	public Vector2d bolaGrandeMasCercana() {

		Vector2d posBolaGrande = null;
		double distanciaMasCercana = Double.MAX_VALUE;

		for (Vector2d pos : posicionesBolasGrandes) {

			double d = distanciaManhattan(pos);

			if (d < distanciaMasCercana) {
				posBolaGrande = pos;
				distanciaMasCercana = d;
			}
		}
		return posBolaGrande;
	}
	
	public int etiqueta(Vector2d pos) {
		
		ArrayList<Observation> contenido = entorno[(int) coordenada(pos.x)][(int) coordenada(pos.y)];

		int tipo = 0;

		if (contenido.get(0).itype == 0)
			tipo = 1;
		
		for (int i = 1; i < contenido.size(); i++) {
			switch (contenido.get(i).itype) {
			case 6:
				tipo = 6;
				break;
			case 15:
			case 18:
			case 21:
			case 24:
				if(tipo != 6)
					tipo = 2;
				break;
			default:
			}
		}
		
		return tipo;
	}

	public List<ACTIONS> accionesDisponibles() {
		
		Vector2d posFantasma = fantasmaColorMasCercano();

		List<ACTIONS> acciones = new ArrayList<ACTIONS>();

		ArrayList<Observation> contenido;
		
		double d = 0.5;
		
		if(posicionesAvatar.size() == 1) {
			d = 1;
		}
		
		
		contenido = entorno[(int) (coordenada(posicionAvatar.x) + d)][(int) coordenada(posicionAvatar.y)];
		
		acciones.add(ACTIONS.ACTION_RIGHT);
		
		if (contenido.size() > 0) {
	
			if (contenido.get(0).itype == 0)
				acciones.remove(ACTIONS.ACTION_RIGHT);
			
			for (int i = 1; i < contenido.size(); i++) {
				switch (contenido.get(i).itype) {
				case 15:
				case 18:
				case 21:
				case 24:
					acciones.remove(ACTIONS.ACTION_RIGHT);
					break;
				default:
				}
			}
		}
		
		if(posFantasma != null && coordenada(distanciaManhattan(proxPosicion(ACTIONS.ACTION_RIGHT), posFantasma)) < 2)
			acciones.remove(ACTIONS.ACTION_RIGHT);
		
		
		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) (coordenada(posicionAvatar.y) + d)];
		
		acciones.add(ACTIONS.ACTION_DOWN);
		
		if (contenido.size() > 0) {
	
			if (contenido.get(0).itype == 0)
				acciones.remove(ACTIONS.ACTION_DOWN);
			
			for (int i = 1; i < contenido.size(); i++) {
				switch (contenido.get(i).itype) {
				case 15:
				case 18:
				case 21:
				case 24:
					acciones.remove(ACTIONS.ACTION_DOWN);
					break;
				default:
				}
			}
		}
		
		if(posFantasma != null && coordenada(distanciaManhattan(proxPosicion(ACTIONS.ACTION_DOWN), posFantasma)) < 2)
			acciones.remove(ACTIONS.ACTION_DOWN);

		
		contenido = entorno[(int) (coordenada(posicionAvatar.x) - d)][(int) coordenada(posicionAvatar.y)];

		acciones.add(ACTIONS.ACTION_LEFT);
		
		if (contenido.size() > 0) {

			if (contenido.get(0).itype == 0)
				acciones.remove(ACTIONS.ACTION_LEFT);
			
			for (int i = 1; i < contenido.size(); i++) {
				switch (contenido.get(i).itype) {
				case 15:
				case 18:
				case 21:
				case 24:
					acciones.remove(ACTIONS.ACTION_LEFT);
					break;
				default:
				}
			}
		}
		
		if(posFantasma != null && coordenada(distanciaManhattan(proxPosicion(ACTIONS.ACTION_LEFT), posFantasma)) < 2)
			acciones.remove(ACTIONS.ACTION_LEFT);

		
		contenido = entorno[(int) coordenada(posicionAvatar.x)][(int) (coordenada(posicionAvatar.y) - d)];

		acciones.add(ACTIONS.ACTION_UP);
		
		if (contenido.size() > 0) {

			if (contenido.get(0).itype == 0)
				acciones.remove(ACTIONS.ACTION_UP);
			
			for (int i = 1; i < contenido.size(); i++) {
				switch (contenido.get(i).itype) {
				case 15:
				case 18:
				case 21:
				case 24:
					acciones.remove(ACTIONS.ACTION_UP);
					break;
				default:
				}
			}
		}
		
		if(posFantasma != null && coordenada(distanciaManhattan(proxPosicion(ACTIONS.ACTION_UP), posFantasma)) < 2)
			acciones.remove(ACTIONS.ACTION_UP);
		
		
		if (posicionesAvatar.size() > 1) {
			if (posicionesAvatar.get(0).x == posicionesAvatar.get(1).x) { // misma columna
				acciones.remove(ACTIONS.ACTION_RIGHT);
				acciones.remove(ACTIONS.ACTION_LEFT);
			}
			else { // misma fila
				acciones.remove(ACTIONS.ACTION_UP);
				acciones.remove(ACTIONS.ACTION_DOWN);
			}
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
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y + (bloque/2));
			break;
		case ACTION_UP:
				proxPosicion = new Vector2d(posicionAvatar.x, posicionAvatar.y - (bloque/2));
			break;
		case ACTION_RIGHT:
				proxPosicion = new Vector2d(posicionAvatar.x + (bloque/2), posicionAvatar.y);
			break;
		case ACTION_LEFT:
				proxPosicion = new Vector2d(posicionAvatar.x - (bloque/2), posicionAvatar.y);
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

	public double distanciaManhattan(Vector2d destino) {

		return Math.abs(posicionAvatar.x - destino.x) + Math.abs(posicionAvatar.y - destino.y);
	}

	public double distanciaManhattan(Vector2d origen, Vector2d destino) {

		return (Math.abs(origen.x - destino.x) + Math.abs(origen.y - destino.y));
	}

	public double distanciaManhattan(double xi, double yi, double xf, double yf) {
		return Math.abs(xi - xf) + Math.abs(yi - yf);
	}
}
