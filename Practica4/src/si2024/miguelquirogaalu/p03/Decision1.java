package si2024.miguelquirogaalu.p03;

public class Decision1 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		// veo si tengo mas cerca una Enfermera o un Sano
		if (mundo.getNumEnfermeras() > 0 && mundo.getNumSanos() > 0 && (mundo.distanciaManhattan(mundo.enfermeraMasCercana()) < mundo.distanciaManhattan(mundo.sanoMasCercano())))
			return new AccionMatarEnfermera();
		else
			return new Decision2();
	}
}