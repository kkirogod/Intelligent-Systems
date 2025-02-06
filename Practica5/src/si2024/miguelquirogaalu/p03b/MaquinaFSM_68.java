package si2024.miguelquirogaalu.p03b;

import java.util.LinkedList;

public class MaquinaFSM_68 extends MaquinaFSM {
	
	public EstadoComerBolitas estadoComerBolitas;
	public EstadoComerFantasmas estadoComerFantasmas;
	public EstadoComerBolaGrande estadoComerBolaGrande;

	public MaquinaFSM_68(Mundo68 m) {
		
		estadoComerBolitas = new EstadoComerBolitas();
		estadoComerFantasmas = new EstadoComerFantasmas();
		estadoComerBolaGrande = new EstadoComerBolaGrande();
		
		estadoInicial = estadoComerBolitas;
		estadoActual = estadoInicial;
		
		mundo = m;
		
		transiciones = new LinkedList<Transicion>();
		//transiciones.add(new TransicionComerBolaGrande(estadoComerBolaGrande));
		transiciones.add(new TransicionComerFantasmas(estadoComerFantasmas));
		transiciones.add(new TransicionComerBolitas(estadoComerBolitas));
	}
}