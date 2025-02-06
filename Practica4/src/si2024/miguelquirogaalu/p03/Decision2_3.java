package si2024.miguelquirogaalu.p03;

public class Decision2_3 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {
		
		Mundo53 mundo = (Mundo53) m;
		
		if(mundo.getNumPuertas() > 0) {
			return new AccionEsperar();
		}
		else {
			return new Decision2_2();
		}
	}
}
