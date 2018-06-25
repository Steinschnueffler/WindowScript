package linus.wml;

public class MLException extends Exception {
	private static final long serialVersionUID = 1L;

	public MLException() {
		super();
	}

	public MLException(String message) {
		super(message);
	}

	public MLException(Throwable cause) {
		super(cause);
	}

	public MLException(String message, Throwable cause) {
		super(message, cause);
	}

	public MLException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
