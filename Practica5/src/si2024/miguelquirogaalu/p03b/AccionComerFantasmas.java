package si2024.miguelquirogaalu.p03b;

import java.util.List;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionComerFantasmas extends Accion {

	@Override
	public ACTIONS doAction(Mundo68 mundo) {

		Vector2d posFantasma = mundo.fantasmaBlancoMasCercano();
		
		ACTIONS accion;

		List<ACTIONS> acciones = mundo.accionesDisponibles();

		Vector2d salto = mundo.saltoAStar(16);

		if (salto != null) {
			//System.out.println("*** A STAR ***");
			accion = mundo.proxAccion(salto);
		}
		else {
			accion = mundo.acercarse(posFantasma, acciones);
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
