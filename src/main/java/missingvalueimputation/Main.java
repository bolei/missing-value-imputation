package missingvalueimputation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
	public static void main(String[] args) throws IOException {
		InputStream is = Main.class.getClassLoader().getResourceAsStream(
				"config.properties");
		Properties prop = new Properties();
		prop.load(is);
		System.out.println(prop.getProperty("missingRatio"));
	}
}
