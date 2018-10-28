package main;

import java.util.Random;

public class DNA {

	// The genetic sequence
	private char[] genes;

	private double fitness;

	private Random r = new Random();

	// Constructor (makes a random DNA)
	public DNA(int num) {
		genes = new char[num];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = (char) (r.nextInt(101) + 32); // Pick from range of chars
		}
	}

	// Converts character array to a String
	public String getPhrase() {
		return new String(genes);
	}

	// Fitness function (returns doubling point % of "correct" characters)
	public void fitness(String target) {
		int score = 0;
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] == target.charAt(i)) {
				score++;
			}
		}
		fitness = (double) score / (double) target.length();
	}

	// Crossover
	public DNA crossover(DNA partner) {
		// A new child
		DNA child = new DNA(genes.length);

		int midpoint = r.nextInt(genes.length); // Pick a midpoint
		// Half from one, half from the other
		for (int i = 0; i < genes.length; i++) {
			if (i > midpoint) {
				child.genes[i] = genes[i];
			} else {
				child.genes[i] = partner.genes[i];
			}
		}
		return child;
	}

	// Based on a mutation probability, picks a new random character
	void mutate(double mutationRate) {
		for (int i = 0; i < genes.length; i++) {
			if (r.nextDouble() < mutationRate) {
				genes[i] = (char) (r.nextInt(101) + 32);
			}
		}
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
}