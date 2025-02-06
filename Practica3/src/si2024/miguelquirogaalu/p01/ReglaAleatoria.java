package si2024.miguelquirogaalu.p01;

import java.util.List;

public class ReglaAleatoria implements Regla {

	List<Condicion> antecedentes;
	Accion accion;
	
	@Override
	public boolean seCumple(Mundo m) {
		
		return true;
	}
	
	@Override
	public Accion getAccion() {
		
		return new AccionAleatoria();
	}

}
