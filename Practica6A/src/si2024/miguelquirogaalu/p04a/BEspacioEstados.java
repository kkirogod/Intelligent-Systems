package si2024.miguelquirogaalu.p04a;

import java.util.ArrayList;
import java.util.List;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class BEspacioEstados {

	private Mundo49 mundo;
	private boolean aPorFlecha;
	private boolean aPorFlechaNenufar;
	private Vector2d posFlecha;

	public BEspacioEstados(Mundo49 mundo) {
		this.mundo = mundo;
		aPorFlecha = false;
		aPorFlechaNenufar = false;
		posFlecha = null;
	}

	public ACTIONS pensar() {

		if (mundo.visualizar()) {
			System.out.println("A POR FLECHA: " + aPorFlecha);
			System.out.println("A POR FLECHA NENUFAR: " + aPorFlechaNenufar);
			if (posFlecha != null)
				System.out.println(
						"Flecha: [" + Mundo49.coordenada(posFlecha.y) + ", " + Mundo49.coordenada(posFlecha.x) + "]");
		}

		Vector2d destino = null;
		Vector2d proxDestino = null;

		if (mundo.estaVolando() || mundo.getTick() < 4) {
			aPorFlecha = false;
			aPorFlechaNenufar = false;
			return ACTIONS.ACTION_NIL;
		}

		if (Mundo49.coordenada(mundo.getPosicionAvatar().x) == 11
				&& Mundo49.coordenada(mundo.getPosicionAvatar().y) == 14 && !mundo.estaVolando()) {
			destino = new Vector2d(mundo.proxPosicion(ACTIONS.ACTION_LEFT));
			aPorFlecha = true;
			posFlecha = mundo.flechaMasCercana(ACTIONS.ACTION_UP);
		}

		if (Mundo49.coordenada(mundo.getdPI()) == 2 && mundo.getTick() < 7) {
			return ACTIONS.ACTION_RIGHT;
		}

		List<Nodo> ruta = mundo.saltoAStar(null);

		if (ruta != null) {
			destino = new Vector2d(ruta.get(0).x * mundo.getBloque(), ruta.get(0).y * mundo.getBloque());

			if (ruta.size() > 1)
				proxDestino = new Vector2d(ruta.get(1).x * mundo.getBloque(), ruta.get(1).y * mundo.getBloque());
		}

		if (destino != null) {
			
			if (mundo.getdPI() == 390 && Mundo49.coordenada(mundo.getPosicionAvatar().x) == 12
					&& Mundo49.coordenada(mundo.getPosicionAvatar().y) == 2) {
				aPorFlechaNenufar = true;
				posFlecha = new Vector2d(14*mundo.getBloque(), 1*mundo.getBloque());
				mundo.posicionesProhibidas.add(new Vector2d(3*mundo.getBloque(), 10*mundo.getBloque()));
			}

			if (!aPorFlechaNenufar) {

				if (mundo.visualizar()) {
					System.out.println("\nProximo salto: [" + Mundo49.coordenada(destino.y) + ", "
							+ Mundo49.coordenada(destino.x) + "] -> Etiqueta: " + mundo.etiqueta(destino));
					System.out.println("Proxima accion: " + mundo.proxAccion(destino));
				}

				if (mundo.etiqueta(destino) == 3) {

					if (mundo.hayNenufar((int) Mundo49.coordenada(destino.x),
							(int) Mundo49.coordenada(destino.y)))
						return ACTIONS.ACTION_NIL;
					else if ((!aPorFlecha || (aPorFlecha && posFlecha == null))
							&& mundo.orientacion() == mundo.proxAccion(destino)) {
						aPorFlecha = true;
						posFlecha = mundo.flechaMasCercana(mundo.orientacion());
					}

				} else if (mundo.etiqueta(destino) == 10 || mundo.etiqueta(destino) == 11) {

					if (mundo.orientacion() != mundo.proxAccion(destino))
						return mundo.proxAccion(destino);

					boolean cruzar = false;

					for (Vector2d pos : mundo.getPosicionesNenufarDer()) {
						if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1) {
							cruzar = true;
							break;
						}
					}

					if (!cruzar) {
						for (Vector2d pos : mundo.getPosicionesNenufarIzq()) {
							if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1) {
								cruzar = true;
								break;
							}
						}
					}

					if (!cruzar)
						return ACTIONS.ACTION_NIL;

				} else if (mundo.etiqueta(destino) == 19) {

					if (Mundo49.coordenada(mundo.getdPI()) == 2)
						mundo.posicionesProhibidas.add(destino);

					return ACTIONS.ACTION_NIL;

				} else if (mundo.etiqueta(destino) >= 14 && mundo.etiqueta(destino) <= 17) {

					if (proxDestino != null && mundo.etiqueta(proxDestino) >= 14 && mundo.etiqueta(proxDestino) <= 17
							&& (mundo.proxAccion(destino, proxDestino) != mundo.direccionFlecha(destino))) {
						ArrayList<Vector2d> posProhibidas = mundo.getPosicionesProhibidas();
						posProhibidas.add(destino);
						mundo.setPosicionesProhibidas(posProhibidas);
						return ACTIONS.ACTION_NIL;
					}

					if (mundo.orientacion() != mundo.proxAccion(destino) && !aPorFlecha)
						return mundo.proxAccion(destino);

					Vector2d vecina = null;

					boolean cruzar = false;

					switch (mundo.etiqueta(destino)) {
					case 14:
						vecina = mundo.flechaVecina(destino, ACTIONS.ACTION_DOWN);

						if (vecina != null) {
							aPorFlecha = true;
							posFlecha = vecina;
							destino = vecina;
						}

						for (Vector2d pos : mundo.getPosicionesFlechaAbajo()) {
							if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1
									&& !mundo.nenufarEstorbaFlecha(pos)) {
								cruzar = true;
								break;
							}
						}
						break;
					case 15:
						vecina = mundo.flechaVecina(destino, ACTIONS.ACTION_UP);

						if (vecina != null) {
							aPorFlecha = true;
							posFlecha = vecina;
							destino = vecina;
						}

						for (Vector2d pos : mundo.getPosicionesFlechaArriba()) {
							if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1
									&& !mundo.nenufarEstorbaFlecha(pos)) {
								cruzar = true;
								break;
							}
						}
						break;
					case 16:
						vecina = mundo.flechaVecina(destino, ACTIONS.ACTION_RIGHT);

						if (vecina != null) {
							aPorFlecha = true;
							posFlecha = vecina;
							destino = vecina;
						}

						for (Vector2d pos : mundo.getPosicionesFlechaDer()) {
							if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1) {
								cruzar = true;
								break;
							}
						}
						break;
					case 17:
						vecina = mundo.flechaVecina(destino, ACTIONS.ACTION_LEFT);

						if (vecina != null) {
							aPorFlecha = true;
							posFlecha = vecina;
							destino = vecina;
						}

						for (Vector2d pos : mundo.getPosicionesFlechaIzq()) {
							if (Mundo49.coordenada(mundo.distanciaManhattan(pos)) <= 1) {
								cruzar = true;
								break;
							}
						}
						break;
					}

					if (!cruzar)
						return ACTIONS.ACTION_NIL;

				} else if ((mundo.etiqueta(destino) == 7 || mundo.etiqueta(destino) == 8)
						&& (mundo.hayNenufar((int) Mundo49.coordenada(mundo.getPosicionAvatar().x),
								(int) Mundo49.coordenada(mundo.getPosicionAvatar().y)))) {

					if (mundo.etiqueta(destino) == 7)
						return ACTIONS.ACTION_RIGHT;
					else
						return ACTIONS.ACTION_LEFT;
				}
			}

			if (aPorFlechaNenufar) {

				List<Nodo> rutaFlecha = mundo.saltoAStar(posFlecha);

				Vector2d proxPos = new Vector2d(rutaFlecha.get(0).x * mundo.getBloque(), rutaFlecha.get(0).y * mundo.getBloque());
				
				if (Mundo49.coordenada(mundo.distanciaManhattan(posFlecha)) > 1
						|| mundo.hayNenufarColumnaFlecha(posFlecha, 11))
					return mundo.proxAccion(proxPos);
				else
					return ACTIONS.ACTION_NIL;

			} else if (aPorFlecha && posFlecha != null) {

				ACTIONS a = mundo.acercarse(posFlecha, mundo.accionesDisponibles());

				if (Mundo49.coordenada(mundo.distanciaManhattan(posFlecha)) > 1) {

					if (mundo.etiqueta(mundo.proxPosicion(a)) == 3
							|| (a == ACTIONS.ACTION_RIGHT && mundo.getPosicionesFlechaDer().size() == 7))
						return ACTIONS.ACTION_NIL;
					else {
						return a;
					}
				} else if (mundo.nenufarEstorbaFlecha(posFlecha)) {
					return ACTIONS.ACTION_NIL;
				} else {
					return a;
				}

			} else if (destino != null) {
				ACTIONS a = mundo.proxAccion(destino);

				if (mundo.etiqueta(mundo.proxPosicion(a)) == 3) {
					return ACTIONS.ACTION_NIL;
				} else
					return a;
			}
		}
		return ACTIONS.ACTION_NIL;
	}
}