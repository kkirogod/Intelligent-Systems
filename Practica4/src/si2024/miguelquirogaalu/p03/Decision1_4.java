package si2024.miguelquirogaalu.p03;

public class Decision1_4 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		if(mundo.estoyInfectado()) {
			return new Decision2_4();
		}
		else {
			return new Decision3_4();
		}
	}

}
