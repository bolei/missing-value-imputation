package missingvalueimputation;

import java.io.InputStream;
import java.util.Properties;

import weka.core.Instances;

public class Main {
	public static void main(String[] args) throws Exception {
		InputStream is = Main.class.getClassLoader().getResourceAsStream(
				"config.properties");
		Properties prop = new Properties();
		prop.load(is);

		DataLoader loader = new DataLoader(Main.class.getClassLoader()
				.getResourceAsStream("hw3.csv"));

		int missingValRatio = Integer
				.parseInt(prop.getProperty("missingRatio"));
		int beta = Integer.parseInt(prop.getProperty("querynum"));

		MissingDataGenerator missGen = new MissingDataGenerator(missingValRatio);

		double[][] fullData = loader.loadData();
		double[][] inCompleteData = missGen.generateIncompleteData(fullData);
		Instances formattedData = new DataPreprocessor()
				.getFormattedData(inCompleteData);

		MissingValueScoreCalculator scoreCalc;
		if (prop.getProperty("queryScoreMethod").equals("Random")) {
			scoreCalc = new RandomMissingValueScoreCalculator();
		} else {
			scoreCalc = new SimpleMissingValueScoreCalculator();
		}

		FeatureValueAcquisition fva = new FeatureValueAcquisition(
				formattedData, fullData, inCompleteData, beta, scoreCalc);
		fva.performAcquisition();

	}
}
