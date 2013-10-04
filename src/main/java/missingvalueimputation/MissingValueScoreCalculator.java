package missingvalueimputation;

import java.awt.Point;

import weka.core.Instances;

public interface MissingValueScoreCalculator {

	double calcualteScore(Point p, Instances imputedData,
			double[][] incompleteData);

}
