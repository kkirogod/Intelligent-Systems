
package si2024.miguelquirogaalu.p04;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica_6_Agente extends AbstractPlayer {

	BEspacioEstados cerebro;
	Mundo45 mundo;
	
	public Practica_6_Agente(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo = new Mundo45(stateObs);
		cerebro = new BEspacioEstados(mundo);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo.actualizar(stateObs);
		
		return cerebro.pensar();
		
		//return mundo.accionAleatoria();
	}

}