package missingvalueimputation;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DataPreprocessor {
	private static final int FEATURE_LENTH = 50;

	public Instances getFormattedData(double[][] inCompleteData) {
		Instances instances = createEmptyInstances();
		int rowNum = inCompleteData.length;
		int colNum = inCompleteData[0].length;
		for (int i = 0; i < rowNum; i++) {
			double[] attValues = new double[colNum + 1];
			for (int j = 0; j < colNum; j++) {
				attValues[j] = inCompleteData[i][j];
			}
			attValues[colNum] = i < rowNum / 2 ? 1d : 0d; // assign labels
			instances.add(new DenseInstance(1.0, attValues));
		}

		imputeMissingValue(instances);

		return instances;
	}

	private void imputeMissingValue(Instances instances) {
		// replace missing values with 0
		int instCount = instances.numInstances();
		int featCount = instances.get(0).numAttributes() - 1;
		for (int i = 0; i < instCount; i++) {
			for (int j = 0; j < featCount; j++) {
				if (Double.isNaN(instances.get(i).value(j))) {
					instances.get(i).setValue(j, 0);
				}
			}
		}
	}

	private Instances createEmptyInstances() {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		for (int i = 0; i < FEATURE_LENTH; i++) {
			atts.add(new Attribute("att" + i));
		}
		// Declare the class attribute along with its values
		ArrayList<String> fvClassVal = new ArrayList<String>(2);
		fvClassVal.add("0");
		fvClassVal.add("1");
		atts.add(new Attribute("label", fvClassVal));
		Instances data = new Instances("MyRelation", atts, 0);
		data.setClassIndex(data.numAttributes() - 1);
		return data;
	}
}
