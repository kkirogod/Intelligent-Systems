package si2024.miguelquirogaalu.p03;

import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionInfectar extends Accion {

	@Override
	public ACTIONS doAction(Mundo m) {

		Mundo53 mundo = (Mundo53) m;
		Random rnd = new Random(System.currentTimeMillis());

		Vector2d posSano = mundo.sanoMasCercano();
		
		/*
		if(mundo.posSanosEnPeligro().size() > 0)
			return ACTIONS.ACTION_NIL;
		*/
		
		ACTIONS accion;

		List<ACTIONS> acciones = mundo.accionesDisponibles();

		Vector2d salto = mundo.saltoAStar(10);

		if (salto != null)
			accion = mundo.proxAccion(salto);
		else
			accion = mundo.acercarse(posSano, acciones);
		
		if(mundo.getNumEnfermeras() > 0 && Mundo53.coordenada(mundo.distanciaManhattan(mundo.proxPosicion(accion), mundo.enfermeraMasCercana())) < 1) {
			return ACTIONS.ACTION_USE;
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
