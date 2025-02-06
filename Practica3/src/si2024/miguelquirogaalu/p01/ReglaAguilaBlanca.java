package si2024.miguelquirogaalu.p01;

import java.util.LinkedList;
import java.util.List;

public class ReglaAguilaBlanca implements Regla {

	List<Condicion> antecedentes;
	Accion accion;
	
	public ReglaAguilaBlanca() {
		antecedentes = new LinkedList<Condicion>();
		antecedentes.add(new CondicionAguilaBlanca());
		accion = new AccionAguilaBlanca();
	}

	@Override
	public boolean seCumple(Mundo m) {

		for(Condicion c : antecedentes) {
			if(!c.seCumple(m))
				return false;
		}
		
		return true;
	}

	@Override
	public Accion getAccion() {

		return accion;
	}

}
