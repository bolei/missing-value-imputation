package missingvalueimputation;

import java.awt.Point;
import java.util.Random;

import weka.core.Instances;

public class RandomMissingValueScoreCalculator implements
		MissingValueScoreCalculator {
	private Random rand = new Random();

	@Override
	public double calcualteScore(Point p, Instances imputedData,
			double[][] incompleteData) {
		return rand.nextDouble();
	}

}
