package si2024.miguelquirogaalu.p03;

import java.util.Random;

import tools.Utils;
import tracks.ArcadeMachine;

public class Practica_4_exe {
	
	public static int[] test_level(int lvl) {

		String p0 = "si2024.miguelquirogaalu.p03.Practica_4_Agente";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		boolean visuals = false;
		int gameIdx = 53;

		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1 = game.replace(gameName, gameName + "_lvl" + lvl);
		Random rnd = new Random();
		// ArcadeMachine.playOneGame(game, level1, null, 33);

		int ganadas = 0;
		int score = 0;
		int max_score = 0;
		int tiradas = 1;

		for (int i = 0; i < tiradas; i++) {
			// var seed = -1168181290;
			int seed = rnd.nextInt();

			double[] cosos = ArcadeMachine.runOneGame(game, level1, visuals, p0, null, seed, 0);

			if (cosos[0] != 0.0) {
				ganadas++;
			}
			score += (int) cosos[1];
			
			if(cosos[1] > max_score)
				max_score = (int) cosos[1];
		}

		score = score / tiradas;
		int wr = ganadas * 100 / tiradas;

		System.out.println("WR en el nivel " + lvl + ": " + wr + "%");
		System.out.println("Sc en el nivel " + lvl + ": " + score);

		int data[] = { score, wr, max_score};

		return data;
	}

	
	public static void main(String[] args) {

		int puntos = 0;
		int wr = 0;
		int data[];
		int max_score = 0;
		int partidas = 0;

		while(puntos < 183) {
			
			puntos = 0;
			max_score = 0;
			wr = 0;
			
			for (int i = 0; i < 5; i++) {
				data = test_level(i);
				puntos += data[0];
				wr += data[1];
				max_score += data[2];
				System.out.println("\n");
			}
			
			partidas++;
		}

		System.out.println("Puntos: " + puntos);
		System.out.println("Puntos maximos: " + max_score);
		System.out.println("Win Rate: " + wr / 5 + "%");
		System.out.println("Partidas: " + partidas);

		System.exit(0);
	}
	
	/*
	public static void main(String[] args) {

		String p0 = "si2024.miguelquirogaalu.p02.Practica_4_Agente";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		// Game settings
		boolean visuals = true;
		int seed = new Random().nextInt();

		// Game and level to play
		int gameIdx = 53;
		int levelIdx = 2; // level names from 0 to 4 (game_lvlN.txt).

		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);

		double[] array;
		int wins = 0;
		int score = 0;
		int tiradas = 1;

		for (int i = 0; i < tiradas; i++) {
			array = ArcadeMachine.runOneGame(game, level1, visuals, p0, null, seed, 0);
		 	//array = ArcadeMachine.playOneGame(game, level1, null, seed);

			if (array[0] == 1)
				wins++;

			score += (int) array[1];
		}

		System.out.println("Puntos: " + score / tiradas);
		System.out.println("WIN RATE: " + wins * 100 / tiradas + "%");

		System.exit(0);
	}
	*/
}
