package si2024.miguelquirogaalu.p03;

public class Decision1_3 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;
		
		int numEnf = mundo.getNumEnfermeras();

		if ((mundo.getNumSanos() > (numEnf*2)) || (numEnf == 0))
			return new Decision2_2();
		else
			return new AccionMatarEnfermera();
	}
}