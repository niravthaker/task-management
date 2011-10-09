package name.nirav.tasks.api.client;

import org.junit.BeforeClass;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class BaseAPITests {
	protected static ClientConfig cc;
	@BeforeClass
	public static void init(){
		cc = new DefaultClientConfig();
		cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
	}

}
