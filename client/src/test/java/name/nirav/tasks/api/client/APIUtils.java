package name.nirav.tasks.api.client;

import java.io.IOException;
import java.util.Properties;

public class APIUtils {
	private static Properties props = new Properties();
	public static final String URI_USERS = String.format("%s/users", getBaseURL());

	public static Properties loadProps() throws IOException {
		if (props.isEmpty())
			props.load(APIUtils.class.getResourceAsStream("settings.properties"));
		return props;
	}

	public static Properties getProperties() {
		try {
			return loadProps();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getBaseURL() {
		return getProperties().getProperty("baseURL");
	}
}
