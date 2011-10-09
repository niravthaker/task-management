package name.nirav.tasks.api.client.exceptions;

public class ResourceNotFound extends RuntimeException {
	private static final long serialVersionUID = -3981722357230085668L;
	public ResourceNotFound(String msg) {
		super(msg);
	}
}
