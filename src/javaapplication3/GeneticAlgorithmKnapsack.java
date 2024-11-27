import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmKnapsack {

    // Các tham số của bài toán
    static final int n = 10;
    static final int W = 50;
    static final int[] weights = {10, 20, 30, 40, 50, 15, 25, 35, 45, 5};
    static final int[] values = {60, 100, 120, 80, 200, 90, 150, 140, 210, 50};

    // Các tham số giải thuật di truyền
    static final int populationSize = 20;
    static final int generations = 100;
    static final double crossoverRate = 0.8;
    static final double mutationRate = 0.1;
    static final Random random = new Random();

    // Khởi tạo quần thể ban đầu
    static List<int[]> initializePopulation() {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            int[] individual = new int[n];
            for (int j = 0; j < n; j++) {
                individual[j] = random.nextInt(2);
            }
            population.add(individual);
        }
        return population;
    }

    // Hàm tính độ thích nghi (fitness)
    static int fitness(int[] individual) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < n; i++) {
            totalWeight += individual[i] * weights[i];
            totalValue += individual[i] * values[i];
        }
        int penalty = Math.max(0, totalWeight - W) * 10;
        return totalValue - penalty;
    }

    // Chọn cha mẹ bằng phương pháp tỉ lệ thích nghi (roulette wheel selection)
    static int[] selectParent(List<int[]> population) {
        List<Integer> fitnessValues = new ArrayList<>();
        int totalFitness = 0;

        for (int[] individual : population) {
            int fit = fitness(individual);
            fitnessValues.add(fit);
            totalFitness += fit;
        }

        if (totalFitness == 0) {
            return population.get(random.nextInt(populationSize));
        }

        double[] probabilities = new double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            probabilities[i] = (double) fitnessValues.get(i) / totalFitness;
        }

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < populationSize; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return population.get(i);
            }
        }
        return population.get(populationSize - 1);
    }

    // Lai ghép (crossover)
    static int[] crossover(int[] parent1, int[] parent2) {
        if (random.nextDouble() < crossoverRate) {
            int point = random.nextInt(n - 1) + 1;
            int[] child = new int[n];
            System.arraycopy(parent1, 0, child, 0, point);
            System.arraycopy(parent2, point, child, point, n - point);
            return child;
        }
        return parent1.clone();
    }

    // Đột biến (mutation)
    static void mutate(int[] individual) {
        for (int i = 0; i < n; i++) {
            if (random.nextDouble() < mutationRate) {
                individual[i] = 1 - individual[i];
            }
        }
    }

    // Tạo quần thể mới
    static List<int[]> createNewPopulation(List<int[]> population) {
        List<int[]> newPopulation = new ArrayList<>();
        while (newPopulation.size() < populationSize) {
            int[] parent1 = selectParent(population);
            int[] parent2 = selectParent(population);
            int[] child = crossover(parent1, parent2);
            mutate(child);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    // Giải thuật di truyền
    public static void geneticAlgorithm() {
        List<int[]> population = initializePopulation();
        int[] bestSolution = null;
        int bestFitness = Integer.MIN_VALUE;

        for (int generation = 0; generation < generations; generation++) {
            Collections.sort(population, (a, b) -> fitness(b) - fitness(a));
            int currentBestFitness = fitness(population.get(0));
            if (currentBestFitness > bestFitness) {
                bestFitness = currentBestFitness;
                bestSolution = population.get(0).clone();
            }
            System.out.println("Generation " + generation + ": Best Fitness = " + bestFitness);
            population = createNewPopulation(population);
        }

        if (bestSolution == null) {
            System.out.println("No valid solution found.");
        } else {
            System.out.println("Best solution: ");
            for (int gene : bestSolution) {
                System.out.print(gene + " ");
            }
            System.out.println("\nBest value: " + bestFitness);
            int totalWeight = 0;
            for (int i = 0; i < n; i++) {
                totalWeight += bestSolution[i] * weights[i];
            }
            System.out.println("Total weight: " + totalWeight);
        }
    }

    public static void main(String[] args) {
        geneticAlgorithm();
    }
}
