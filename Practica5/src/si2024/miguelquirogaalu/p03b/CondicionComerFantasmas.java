package si2024.miguelquirogaalu.p03b;

public class CondicionComerFantasmas extends Condicion {

	@Override
	public boolean seCumple(Mundo68 mundo) {
		if(mundo.hayFantasmasHuyendo())
			return true;
		else
			return false;
	}
}
