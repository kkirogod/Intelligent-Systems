
package si2024.miguelquirogaalu.p03b;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class Practica_5_Agente extends AbstractPlayer {

	MaquinaFSM_68 cerebro;
	Mundo68 mundo;
	
	public Practica_5_Agente(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo = new Mundo68(stateObs);
		cerebro = new MaquinaFSM_68(mundo);
	}

	@Override
	public ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elpasedTimer) {
		
		mundo.actualizar(stateObs);
		/*
		Accion a = new AccionRandom();
		
		return a.doAction(mundo);
		*/
		
		Estado estado = cerebro.disparo();
	
		return estado.getAccion().doAction(mundo);
	}

}

// BOLAS -> +1
// CEREZAS -> +5 | DURACIÓN -> 200 ticks
// MATAR FANTASMA -> +40 y SPAWNEA 1 FANTASMA (no asustado)
// BUG: puntos infinitos al matar en el spawn

// MEJORAS:
// - Si solo queda una bolita y una bola grande, dejar la bolita para lo ultimo