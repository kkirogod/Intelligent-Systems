package si2024.miguelquirogaalu.p03;

public class Decision3 extends Decision {

	@Override
	public NodoArbol getBranch(Mundo m) {

		Mundo53 mundo = (Mundo53) m;

		// veo si es mas rapido matar o infectar (aunque yo no esté infectado)
		if (mundo.getNumEnfermeras() > 0 && mundo.getNumSanos() > 0 && (mundo.distanciaManhattan(mundo.enfermeraMasCercana()) < (mundo.distanciaManhattan(mundo.virusMasCercano())
				+ mundo.distanciaManhattan(mundo.virusMasCercano(), mundo.sanoMasCercano(mundo.virusMasCercano())))))
			return new AccionMatarEnfermera();
		else
			return new AccionInfectarme();
	}

}
