package missingvalueimputation;

import java.util.Random;

public class MissingDataGenerator {

	private int missingValueRatio;
	private Random rand = new Random();

	public MissingDataGenerator(int ratio) {
		missingValueRatio = ratio;
	}

	public double[][] generateIncompleteData(double[][] fullData) {
		int colNum = fullData[0].length;
		int rowNum = fullData.length;
		double[][] incompleteData = new double[rowNum][colNum];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				incompleteData[i][j] = rand.nextInt(100) >= missingValueRatio ? fullData[i][j]
						: Double.NaN;
			}
		}
		return incompleteData;
	}
}
