package missingvalueimputation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataLoader {

	private InputStream dataFileIn;

	public DataLoader(InputStream inputStream) {
		dataFileIn = inputStream;
	}

	public double[][] loadData() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(dataFileIn));
		String line = null;
		double[][] data = new double[400][50];
		int rowNum = 0;
		while ((line = br.readLine()) != null) {
			String[] strArr = line.split(",");
			for (int j = 0; j < strArr.length; j++) {
				data[rowNum][j] = Double.parseDouble(strArr[j]);
			}
			rowNum++;
		}
		return data;
	}
}
