package si2024.miguelquirogaalu.p01;

import java.util.List;

public class Motor18 implements Motor {

	List<Regla> reglas;
	
	public Motor18(List<Regla> r) {
		reglas = r;
	}

	@Override
	public Regla disparo(Mundo m) {
		
		for(Regla r : reglas) {
			if(r.seCumple(m))
				return r;
		}
		
		return new ReglaAleatoria();
	}

	@Override
	public Regla resolucionConflicto() {
		
		return null;
	}
}
