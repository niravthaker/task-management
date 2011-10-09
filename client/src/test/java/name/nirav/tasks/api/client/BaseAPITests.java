package name.nirav.tasks.api.client;

import static name.nirav.tasks.api.client.APIUtils.getBaseURL;
import static name.nirav.tasks.api.client.APIUtils.getPassword;
import static name.nirav.tasks.api.client.APIUtils.getUser;
import static name.nirav.tasks.api.client.APIUtils.getVersion;

import org.junit.BeforeClass;
public class BaseAPITests {
	protected static TaskAPIClient apiClient;
	
	@BeforeClass
	public static void init(){
		apiClient = new TaskAPIClient(getBaseURL(), getVersion(), getUser(), getPassword());
	}

}
