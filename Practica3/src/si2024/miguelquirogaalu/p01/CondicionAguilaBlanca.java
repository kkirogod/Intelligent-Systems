package si2024.miguelquirogaalu.p01;

public class CondicionAguilaBlanca implements Condicion {

	@Override
	public boolean seCumple(Mundo m) {

		if(((Mundo18) m).getNumAguilasBlancas() > 0)
			return true;
		else
			return false;
	}
}
