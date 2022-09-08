package diceRoller;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DiceRoller {

	private int m_numRolls;
	private Random m_random;

	public DiceRoller(int numRolls) {
		m_numRolls = numRolls;
		m_random = new Random();
	}

	private IntFunction<List<Integer>> nDiceRolls(int n) {
		return (x) -> m_random.ints(n, 1, 11).boxed().collect(Collectors.toList());
	}

	private Map<List<Integer>, Double> diceRolls(int nDice) {
		double fraction = 1.0 / m_numRolls;
		IntStream parallelStream = IntStream.range(0, m_numRolls).parallel();
		Stream<List<Integer>> rolls = parallelStream.mapToObj(nDiceRolls(nDice));
		return rolls.collect(Collectors.groupingBy(roll -> roll, Collectors.summingDouble(n -> fraction)));
	}

	private static Predicate<Map.Entry<List<Integer>, Double>> testRollCondition(int nHighestGone) {
		return (entry) -> entry.getKey().stream().filter(x -> x > 5).count() > nHighestGone;
	}

	public Double getProbability(int nDice, int nHighestGone) {
		Map<List<Integer>, Double> rolls = diceRolls(nDice);
		return rolls.entrySet().stream().filter(testRollCondition(nHighestGone)).collect(Collectors.summingDouble(entry -> entry.getValue()));
	}
	
	public static void main(String[] args) {
		DiceRoller d = new DiceRoller(1000);
		System.out.println("Prob (1, Standard) = " + d.getProbability(1, 0));
		System.out.println("Prob (2, Standard) = " + d.getProbability(2, 0));
		System.out.println("Prob (3, Standard) = " + d.getProbability(3, 0));
		System.out.println("Prob (4, Standard) = " + d.getProbability(4, 0));
		System.out.println("Prob (5, Standard) = " + d.getProbability(5, 0));

		System.out.println("Prob (1, Risky) = " + d.getProbability(1, 1));
		System.out.println("Prob (2, Risky) = " + d.getProbability(2, 1));
		System.out.println("Prob (3, Risky) = " + d.getProbability(3, 1));
		System.out.println("Prob (4, Risky) = " + d.getProbability(4, 1));
		System.out.println("Prob (5, Risky) = " + d.getProbability(5, 1));

		System.out.println("Prob (1, Dangerous) = " + d.getProbability(1, 2));
		System.out.println("Prob (2, Dangerous) = " + d.getProbability(2, 2));
		System.out.println("Prob (3, Dangerous) = " + d.getProbability(3, 2));
		System.out.println("Prob (4, Dangerous) = " + d.getProbability(4, 2));
		System.out.println("Prob (5, Dangerous) = " + d.getProbability(5, 2));
		
		System.out.println("");
		
		DiceRoller betterRoller = new DiceRoller(1000000);
		System.out.println("Better Prob (1, Standard) = " + betterRoller.getProbability(1, 0));
		System.out.println("Better Prob (2, Standard) = " + betterRoller.getProbability(2, 0));
		System.out.println("Better Prob (3, Standard) = " + betterRoller.getProbability(3, 0));
		System.out.println("Better Prob (4, Standard) = " + betterRoller.getProbability(4, 0));
		System.out.println("Better Prob (5, Standard) = " + betterRoller.getProbability(5, 0));

		System.out.println("Better Prob (1, Risky) = " + betterRoller.getProbability(1, 1));
		System.out.println("Better Prob (2, Risky) = " + betterRoller.getProbability(2, 1));
		System.out.println("Better Prob (3, Risky) = " + betterRoller.getProbability(3, 1));
		System.out.println("Better Prob (4, Risky) = " + betterRoller.getProbability(4, 1));
		System.out.println("Better Prob (5, Risky) = " + betterRoller.getProbability(5, 1));

		System.out.println("Better Prob (1, Dangerous) = " + betterRoller.getProbability(1, 2));
		System.out.println("Better Prob (2, Dangerous) = " + betterRoller.getProbability(2, 2));
		System.out.println("Better Prob (3, Dangerous) = " + betterRoller.getProbability(3, 2));
		System.out.println("Better Prob (4, Dangerous) = " + betterRoller.getProbability(4, 2));
		System.out.println("Better Prob (5, Dangerous) = " + betterRoller.getProbability(5, 2));
	}
}