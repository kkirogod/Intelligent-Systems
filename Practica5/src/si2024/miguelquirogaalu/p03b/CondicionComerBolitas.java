package si2024.miguelquirogaalu.p03b;

public class CondicionComerBolitas extends Condicion {

	@Override
	public boolean seCumple(Mundo68 mundo) {
		if(mundo.getPosBolitas().size() > 0) {
			return true;
		}
		else
			return false;
	}
}
