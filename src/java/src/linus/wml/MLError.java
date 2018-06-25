package linus.wml;

public class MLError extends Error {
	private static final long serialVersionUID = 1L;

	public MLError() {
		super();
	}

	public MLError(String message) {
		super(message);
	}

	public MLError(Throwable cause) {
		super(cause);
	}

	public MLError(String message, Throwable cause) {
		super(message, cause);
	}

	public MLError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
