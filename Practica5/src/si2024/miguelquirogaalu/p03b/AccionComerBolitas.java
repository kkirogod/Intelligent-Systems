package si2024.miguelquirogaalu.p03b;

import java.util.List;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionComerBolitas extends Accion {

	@Override
	public ACTIONS doAction(Mundo68 mundo) {

		ACTIONS accion;
		List<ACTIONS> acciones = mundo.accionesDisponibles();
		
		Vector2d posFantasma = mundo.fantasmaColorMasCercano();

		if (posFantasma != null && Mundo68.coordenada(mundo.distanciaManhattan(posFantasma)) < 3) {

			accion = mundo.alejarse(posFantasma, acciones);
		}
		else {
			
			Vector2d posBolita = mundo.bolitaMasCercana();

			Vector2d salto = mundo.saltoAStar(5);

			if (salto != null) {
				accion = mundo.proxAccion(salto);
			} else {
				accion = mundo.acercarse(posBolita, acciones);
			}
		}

		/*
		 * System.out.println("ACCION ESCOGIDA: " + accion);
		 * System.out.println("*************************************************");
		 * System.out.println("*************************************************");
		 * System.out.println("*************************************************");
		 */

		return accion;
	}

}
