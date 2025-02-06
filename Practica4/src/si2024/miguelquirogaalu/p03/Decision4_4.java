package si2024.miguelquirogaalu.p03;

public class Decision4_4 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		if (mundo.getNumEnfermeras() > 0) {
			return new AccionMatarEnfermera();
		}
		else
			return new AccionEsperar();
	}

}
