package missingvalueimputation;

import java.awt.Point;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class FeatureValueAcquisition {
	private Instances imputedData;
	private double[][] incompleteData;
	private double[][] fullData;
	private RandomForest randomForest = new RandomForest();
	private final int beta; // num to query
	private MissingValueScoreCalculator scoreCalculator;

	public FeatureValueAcquisition(Instances imputedData, double[][] fullData,
			double[][] incompleteData, int beta,
			MissingValueScoreCalculator scoreCalc) {
		this.imputedData = imputedData;
		this.fullData = fullData;
		this.incompleteData = incompleteData;
		this.beta = beta;
		scoreCalculator = scoreCalc;
	}

	// cost matrix to be all ones

	public void performAcquisition() throws Exception {
		List<Point> points = initQ();
		int numQueried = 0;
		while (points.isEmpty() == false) {
			randomForest.buildClassifier(imputedData);
			evaluate(numQueried);// print out evaluation

			// takes 100 points
			Point[] toquery = selectPointsToQuery(points);

			updateData(toquery);
			numQueried += toquery.length;
		}
	}

	private void evaluate(int numQueried) {
		// TODO Auto-generated method stub

	}

	private void updateData(Point[] toquery) {
		for (Point p : toquery) {
			imputedData.get(p.x).setValue(p.y, fullData[p.x][p.y]);
		}

	}

	private Point[] selectPointsToQuery(List<Point> points) {
		TreeMap<Double, Point> scoreBoard = new TreeMap<>(
				Collections.reverseOrder());
		Point[] selectedPoints = new Point[Math.min(points.size(), beta)];
		for (Point p : points) {
			double score = scoreCalculator.calcualteScore(p, imputedData);
			scoreBoard.put(score, p);
		}

		Iterator<Double> scoreIt = scoreBoard.keySet().iterator();
		int i = 0;
		while (i < selectedPoints.length && scoreIt.hasNext()) {
			Point p = scoreBoard.get(scoreIt.next());
			selectedPoints[i] = p;
			points.remove(p);
			i++;
		}
		return selectedPoints;
	}

	private List<Point> initQ() {
		List<Point> points = new LinkedList<>();
		int colNum = incompleteData[0].length;
		int rowNum = incompleteData.length;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; i++) {
				if (Double.isNaN(incompleteData[i][j])) {
					points.add(new Point(i, j)); // x is row, j is column
				}
			}
		}
		return points;
	}
}
