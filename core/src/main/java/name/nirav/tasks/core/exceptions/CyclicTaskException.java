package name.nirav.tasks.core.exceptions;

public class CyclicTaskException extends RuntimeException {

	public CyclicTaskException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 7394303592872108968L;
	
}
