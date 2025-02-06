package si2024.miguelquirogaalu.p04;

import java.util.*;

import tools.Vector2d;

public class TSPBruteForce {

	private double minDistancia;
	private List<Vector2d> mejorRuta;
	private Mundo45 mundo;

	public TSPBruteForce(Mundo45 m) {
		minDistancia = Double.MAX_VALUE;
		mejorRuta = null;
		mundo = m;
	}

	public List<Vector2d> encontrarRutaMinima(List<Vector2d> ciudades, int n) {

		if (n == 1) {
			double totalDistancia = calcularDistanciaTotal(ciudades);
			if (totalDistancia < minDistancia) {
				minDistancia = totalDistancia;
				mejorRuta = new ArrayList<>(ciudades);
			}
		}

		for (int i = 0; i < n; i++) {
			Collections.swap(ciudades, i, n - 1);
			encontrarRutaMinima(ciudades, n - 1);
			Collections.swap(ciudades, i, n - 1);
		}

		return mejorRuta;
	}
	
	private double calcularDistanciaTotal(List<Vector2d> ciudades) {
		
		double total = 0;
		
		for (int i = 0; i < ciudades.size() - 1; i++) {
			
			if(total >= minDistancia)
				break;
			
			total += Mundo45.distanciaManhattan(ciudades.get(i), ciudades.get(i + 1));
		}
		
		return total;
	}
}
