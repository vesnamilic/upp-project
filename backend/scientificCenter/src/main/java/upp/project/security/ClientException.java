package upp.project.security;

public class ClientException extends RuntimeException {

	private static final long serialVersionUID = 4469223079743371160L;

	public ClientException() {
		super();
	}

	public ClientException(String message) {
		super(message);
	}

	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientException(Throwable cause) {
		super(cause);
	}
}