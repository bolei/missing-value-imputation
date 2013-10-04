package missingvalueimputation;

import java.awt.Point;

import weka.core.Instances;

public class SimpleMissingValueScoreCalculator implements
		MissingValueScoreCalculator {

	/*
	 * Calculate the coefficient of variation of known values of the feature
	 * having same label as the given point
	 */
	@Override
	public double calcualteScore(Point p, Instances imputedData,
			double[][] incompleteData) {
		double label = imputedData.instance(p.x).classValue();

		// calculating coefficient of variation
		int n = 0;
		double mean = 0;
		double M2 = 0;
		double delta;
		int numRow = incompleteData.length;
		for (int i = 0; i < numRow; i++) {
			double curLable = imputedData.instance(i).classValue();
			if (Double.isNaN(incompleteData[i][p.y])
					|| Math.abs(curLable - label) > 1e-5) {
				continue;
			}
			n++;
			delta = incompleteData[i][p.y] - mean;
			mean += delta / n;
			M2 = M2 + delta * (incompleteData[i][p.y] - mean);
		}
		double variance = M2 / (n - 1);
		return variance / mean;
	}

}
