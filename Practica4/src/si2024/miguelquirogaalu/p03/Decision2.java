package si2024.miguelquirogaalu.p03;

public class Decision2 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {
		
		Mundo53 mundo = (Mundo53) m;
		
		if(mundo.estoyInfectado())
			return new AccionInfectar();
		else
			return new Decision3();
	}
	
}
