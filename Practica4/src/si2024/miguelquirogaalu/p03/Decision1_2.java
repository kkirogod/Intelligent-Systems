package si2024.miguelquirogaalu.p03;

public class Decision1_2 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		if (mundo.getNumEnfermeras() > 0) {
			//System.out.println("***MATAR ENFERMERA***");
			return new AccionMatarEnfermera();
		}
		else
			return new Decision2_3();
			//return new Decision2_2();
	}

}
