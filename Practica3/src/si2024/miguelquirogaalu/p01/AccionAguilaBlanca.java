package si2024.miguelquirogaalu.p01;

import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionAguilaBlanca implements Accion {

	@Override
	public ACTIONS doAction(Mundo m) {

		Mundo18 mundo = (Mundo18) m;
		Random rnd = new Random(System.currentTimeMillis());
		Vector2d posAguila = null;

		posAguila = mundo.aguilaBlancaMasCercanaSinRiesgo();

		if (posAguila == null)
			posAguila = mundo.aguilaBlancaMasCercana();

		Vector2d miPosicion = mundo.getPosicionAvatar();

		ACTIONS accion;
		final int aleatoriedad = 0;

		List<ACTIONS> acciones = mundo.accionesDisponibles();
		/*
		 * System.out.println("EN PELIGRO: " + mundo.estoyEnPeligro());
		 * System.out.println("ACCIONES DISPONIBLES: " + acciones);
		 */
		if (rnd.nextInt(100) < aleatoriedad) {
			accion = acciones.get(rnd.nextInt(acciones.size()));
			// System.out.println("ACCION RANDOM: " + accion);
		} else {

			Vector2d salto = mundo.saltoAStar(6, posAguila);

			if (salto != null)
				accion = mundo.proxAccion(salto);
			else {
				if (Mundo18.coordenada(posAguila.y) > Mundo18.coordenada(miPosicion.y)
						&& acciones.contains(ACTIONS.ACTION_DOWN))
					accion = ACTIONS.ACTION_DOWN;

				else if (Mundo18.coordenada(posAguila.y) < Mundo18.coordenada(miPosicion.y)
						&& acciones.contains(ACTIONS.ACTION_UP))
					accion = ACTIONS.ACTION_UP;

				else if (Mundo18.coordenada(posAguila.x) > Mundo18.coordenada(miPosicion.x)
						&& acciones.contains(ACTIONS.ACTION_RIGHT))
					accion = ACTIONS.ACTION_RIGHT;

				else if (Mundo18.coordenada(posAguila.x) < Mundo18.coordenada(miPosicion.x)
						&& acciones.contains(ACTIONS.ACTION_LEFT))
					accion = ACTIONS.ACTION_LEFT;

				else if (acciones.size() > 1)
					accion = acciones.get(rnd.nextInt(acciones.size()));
				else
					accion = acciones.get(0);
			}

			// System.out.println("ACCION ESCOGIDA: " + accion);
		}
		/*
		 * System.out.println("*************************************************");
		 * System.out.println("*************************************************");
		 * System.out.println("*************************************************");
		 */
		return accion;
	}
}