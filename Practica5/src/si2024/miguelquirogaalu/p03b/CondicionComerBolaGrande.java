package si2024.miguelquirogaalu.p03b;

public class CondicionComerBolaGrande extends Condicion {

	@Override
	public boolean seCumple(Mundo68 mundo) {
		if(mundo.getPosBolasGrandes().size() > 0 &&
				mundo.hayFantasmasColores() &&
				(mundo.distanciaManhattan(mundo.bolaGrandeMasCercana()) > mundo.distanciaManhattan(mundo.fantasmaColorMasCercano())))
			return true;
		else
			return false;
	}
}
