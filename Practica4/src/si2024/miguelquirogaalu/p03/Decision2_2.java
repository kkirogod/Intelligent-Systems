package si2024.miguelquirogaalu.p03;

public class Decision2_2 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {
		
		Mundo53 mundo = (Mundo53) m;
		
		if(mundo.estoyInfectado()) {
			//System.out.println("***INFECTAR***");
			return new AccionInfectar();
		}
		else {
			//System.out.println("***INFECTARME***");
			return new AccionInfectarme();
		}
	}
}
