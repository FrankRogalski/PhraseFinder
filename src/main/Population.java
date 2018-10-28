package main;

import java.util.ArrayList;
import java.util.Random;

public class Population {
	private final DNA[] population; // Array to hold the current population
	private final ArrayList<DNA> matingPool; // ArrayList which we will use for our "mating pool"
	private final Random r = new Random();
	
	private final String target; // Target phrase
	private final double mutationRate; // Mutation rate
	private final int perfectScore;
	
	private int generations; // Number of generations
	private boolean finished; // Are we finished evolving?

	public Population(final String p, final double m, final int num) {
		target = p;
		mutationRate = m;
		population = new DNA[num];
		for (int i = 0; i < population.length; i++) {
			population[i] = new DNA(target.length());
		}
		calcFitness();
		matingPool = new ArrayList<DNA>();
		finished = false;
		generations = 0;

		perfectScore = 1;
	}

	// Fill our fitness array with a value for every member of the population
	public void calcFitness() {
		for (int i = 0; i < population.length; i++) {
			population[i].fitness(target);
		}
	}

	// Generate a mating pool
	public void naturalSelection() {
		// Clear the ArrayList
		matingPool.clear();

		double maxFitness = 0;
		for (int i = 0; i < population.length; i++) {
			if (population[i].getFitness() > maxFitness) {
				maxFitness = population[i].getFitness();
			}
		}

		// Based on fitness, each member will get added to the mating pool a
		// certain number of times
		// a higher fitness = more entries to mating pool = more likely to be
		// picked as a parent
		// a lower fitness = fewer entries to mating pool = less likely to be
		// picked as a parent
		for (int i = 0; i < population.length; i++) {

			double fitness = map(population[i].getFitness(), 0, maxFitness, 0, 1);
			int n = (int) (fitness * 100); // Arbitrary multiplier, we can also
											// use monte carlo method
			for (int j = 0; j < n; j++) { // and pick two random numbers
				matingPool.add(population[i]);
			}
		}
	}

	// Create a new generation
	public void generate() {
		// Refill the population with children from the mating pool
		for (int i = 0; i < population.length; i++) {
			int a = r.nextInt(matingPool.size());
			int b = r.nextInt(matingPool.size());
			DNA partnerA = matingPool.get(a);
			DNA partnerB = matingPool.get(b);
			DNA child = partnerA.crossover(partnerB);
			child.mutate(mutationRate);
			population[i] = child;
		}
		generations++;
	}

	// Compute the current "most fit" member of the population
	public String getBest() {
		double worldrecord = 0.0;
		int index = 0;
		for (int i = 0; i < population.length; i++) {
			if (population[i].getFitness() > worldrecord) {
				index = i;
				worldrecord = population[i].getFitness();
			}
		}

		if (worldrecord == perfectScore) {
			finished = true;
		}
		return population[index].getPhrase();
	}

	public boolean finished() {
		return finished;
	}

	public int getGenerations() {
		return generations;
	}

	// Compute average fitness for the population
	public double getAverageFitness() {
		double total = 0;
		for (int i = 0; i < population.length; i++) {
			total += population[i].getFitness();
		}
		return total / (population.length);
	}

	public String allPhrases() {
		String everything = "";

		int displayLimit = population.length > 50 ? 50 : population.length;

		for (int i = 0; i < displayLimit; i++) {
			everything += population[i].getPhrase() + "\n";
		}
		return everything;
	}

	public static double map(double value, double min, double max, double nMin, double nMax) {
		return ((value - min) / (max - min)) * (nMax - nMin) + nMin;
	}
}