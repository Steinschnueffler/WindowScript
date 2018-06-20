package linus.windowscript;

public class WindowScriptException extends Exception {
	private static final long serialVersionUID = 1L;

	public WindowScriptException() {
		super();
	}

	public WindowScriptException(String message) {
		super(message);
	}

	public WindowScriptException(Throwable cause) {
		super(cause);
	}

	public WindowScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public WindowScriptException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
