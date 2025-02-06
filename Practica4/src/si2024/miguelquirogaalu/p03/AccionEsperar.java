package si2024.miguelquirogaalu.p03;

import java.util.List;
import java.util.Random;

import ontology.Types.ACTIONS;
import tools.Vector2d;

public class AccionEsperar extends Accion {

	@Override
	public ACTIONS doAction(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		ACTIONS accion;

		List<ACTIONS> acciones = mundo.accionesDisponibles();

		if(mundo.estoyInfectado()) {
			Vector2d posSano = mundo.sanoMasCercano();
			
			if (Mundo53.coordenada(mundo.distanciaManhattan(posSano)) < 3) {

				accion = mundo.alejarse(posSano, acciones);
			}
			else {
				accion = ACTIONS.ACTION_NIL;
			}
		}
		else {
			Vector2d posVirus = mundo.virusMasCercano();
			
			if (Mundo53.coordenada(mundo.distanciaManhattan(posVirus)) < 3) {

				accion = mundo.alejarse(posVirus, acciones);
			}
			else {
				accion = ACTIONS.ACTION_NIL;
			}
		}

		if (mundo.getNumEnfermeras() > 0 && Mundo53
				.coordenada(mundo.distanciaManhattan(mundo.proxPosicion(accion), mundo.enfermeraMasCercana())) < 1) {
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
