
package si2024.miguelquirogaalu.p04a;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica_6A_Agente extends AbstractPlayer {

	BEspacioEstados cerebro;
	Mundo49 mundo;
	
	public Practica_6A_Agente(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo = new Mundo49(stateObs);
		cerebro = new BEspacioEstados(mundo);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo.actualizar(stateObs);
		
		return cerebro.pensar();
		
		//return ACTIONS.ACTION_NIL;
	}

}