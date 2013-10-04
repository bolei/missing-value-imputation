package missingvalueimputation;

import java.awt.Point;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import weka.classifiers.functions.SMO;
import weka.core.Instances;

public class FeatureValueAcquisition {
	private Instances imputedData;
	private double[][] incompleteData;
	private double[][] fullData;
	private SMO classifier = new SMO();
	private final int beta; // num to query
	private MissingValueScoreCalculator scoreCalculator;

	// cost matrix to be all ones

	public FeatureValueAcquisition(Instances imputedData, double[][] fullData,
			double[][] incompleteData, int beta,
			MissingValueScoreCalculator scoreCalc) {
		this.imputedData = imputedData;
		this.fullData = fullData;
		this.incompleteData = incompleteData;
		this.beta = beta;
		scoreCalculator = scoreCalc;
	}

	public void performAcquisition() throws Exception {
		List<Point> points = initQ();
		int numQueried = 0;

		while (points.isEmpty() == false) {
			classifier.buildClassifier(imputedData);
			evaluate(numQueried);// print out evaluation

			// takes 100 points
			Point[] toquery = selectPointsToQuery(points);

			updateData(toquery);
			numQueried += toquery.length;
		}
		classifier.buildClassifier(imputedData);
		evaluate(numQueried);// print out evaluation
	}

	private void evaluate(int numQueried) throws Exception {
		int numAccurate = 0;
		int numInstances = imputedData.numInstances();
		for (int i = 0; i < numInstances; i++) {
			double clsLabel = classifier.classifyInstance(imputedData
					.instance(i));
			if (Math.abs(clsLabel - imputedData.instance(i).classValue()) < 1e-5) {
				numAccurate++;
			}
		}
		double accuracy = numAccurate / (double) numInstances;
		System.out.println(numQueried + ", " + accuracy);
	}

	private void updateData(Point[] toquery) {
		for (Point p : toquery) {
			imputedData.get(p.x).setValue(p.y, fullData[p.x][p.y]);
			incompleteData[p.x][p.y] = fullData[p.x][p.y];
		}

	}

	private Point[] selectPointsToQuery(List<Point> points) {
		TreeSet<ScorePoint> scoreBoard = new TreeSet<>(
				Collections.reverseOrder());
		Point[] selectedPoints = new Point[Math.min(points.size(), beta)];
		for (Point p : points) {
			double score = scoreCalculator.calcualteScore(p, imputedData,
					incompleteData);
			scoreBoard.add(new ScorePoint(score, p));
		}

		Iterator<ScorePoint> scoreIt = scoreBoard.iterator();
		int i = 0;
		while (i < selectedPoints.length && scoreIt.hasNext()) {
			Point p = scoreIt.next().point;
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
			for (int j = 0; j < colNum; j++) {
				if (Double.isNaN(incompleteData[i][j])) {
					points.add(new Point(i, j)); // x is row, j is column
				}
			}
		}
		return points;
	}

	private class ScorePoint implements Comparable<ScorePoint> {
		double score;
		Point point;

		ScorePoint(double s, Point p) {
			score = s;
			point = p;
		}

		@Override
		public int compareTo(ScorePoint o) {
			if (this.score < o.score) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
