
package si2024.miguelquirogaalu.p03;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica_4_Agente extends AbstractPlayer {

	Motor motor;
	Mundo mundo;
	
	public Practica_4_Agente(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo = new Mundo53(stateObs);
		motor = new ArbolDecision53();
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo.analizarMundo(stateObs);
	
		return motor.piensa(mundo).doAction(mundo);
	}

}
