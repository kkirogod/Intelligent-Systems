package si2024.miguelquirogaalu.p03b;

import java.util.List;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionComerBolaGrande extends Accion {

	@Override
	public ACTIONS doAction(Mundo68 mundo) {

		Vector2d posBolaGrande = mundo.bolaGrandeMasCercana();
		
		ACTIONS accion;

		List<ACTIONS> acciones = mundo.accionesDisponibles();

		Vector2d salto = mundo.saltoAStar(6);

		if (salto != null) {
			//System.out.println("*** A STAR ***");
			accion = mundo.proxAccion(salto);
		}
		else {
			accion = mundo.acercarse(posBolaGrande, acciones);
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
