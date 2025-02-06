
package si2024.miguelquirogaalu.p01;

import java.util.LinkedList;
import java.util.List;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica_3_Agente extends AbstractPlayer {

	Motor motor;
	List<Regla> reglas;
	Mundo mundo;

	public Practica_3_Agente(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		reglas = new LinkedList<Regla>();
		reglas.add(new ReglaAguilaBlanca());
		
		motor = new Motor18(reglas);
		mundo = new Mundo18(stateObs);
		
		//mundo.analizarMundo(stateObs);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo.analizarMundo(stateObs);
		
		return motor.disparo(mundo).getAccion().doAction(mundo);
	}

}
