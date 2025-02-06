package si2024.miguelquirogaalu.p03;

public class Decision3_4 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		if(mundo.getNumPuertas() > 0) {
			return new Decision4_4();
		}
		else {
			return new AccionInfectarme();
		}
	}

}
