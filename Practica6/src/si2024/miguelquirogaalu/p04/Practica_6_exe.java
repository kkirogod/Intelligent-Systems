package si2024.miguelquirogaalu.p04;

import java.util.Random;

import tools.Utils;
import tracks.ArcadeMachine;

public class Practica_6_exe {
	/*
	public static int[] test_level(int lvl) {

		String p0 = "si2024.miguelquirogaalu.p04.Practica_6_Agente";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		boolean visuals = true;
		int gameIdx = 45;

		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1 = game.replace(gameName, gameName + "_lvl" + lvl);
		Random rnd = new Random();
		// ArcadeMachine.playOneGame(game, level1, null, 33);

		int ganadas = 0;
		int ticks = 0;
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
			ticks += (int) cosos[2];
			
			if(cosos[1] > max_score)
				max_score = (int) cosos[1];
		}

		score = score / tiradas;
		ticks = ticks / tiradas;
		int wr = ganadas * 100 / tiradas;

		System.out.println("Ticks en el nivel " + lvl + ": " + ticks);
		System.out.println("Sc en el nivel " + lvl + ": " + score);
		System.out.println("WR en el nivel " + lvl + ": " + wr + "%");

		int data[] = { score, wr, max_score, ticks };

		return data;
	}

	
	public static void main(String[] args) {

		int puntos = 0;
		int wr = 0;
		int data[];
		int max_score = 0;
		int ticks = 0;

		for (int i = 0; i < 5; i++) {
			data = test_level(i);
			puntos += data[0];
			wr += data[1];
			max_score += data[2];
			ticks += data[3];
			System.out.println("\n");
		}

		System.out.println("Ticks: " + ticks);
		System.out.println("Puntos: " + puntos);
		System.out.println("Puntos maximos: " + max_score);
		System.out.println("Win Rate: " + wr / 5 + "%");

		System.exit(0);
	}
	*/
	
	public static void main(String[] args) {

		String p0 = "si2024.miguelquirogaalu.p04.Practica_6_Agente";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		// Game settings
		boolean visuals = true;
		int seed = new Random().nextInt();

		// Game and level to play
		int gameIdx = 45;
		int levelIdx = 0; // level names from 0 to 4 (game_lvlN.txt).

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
	
}
