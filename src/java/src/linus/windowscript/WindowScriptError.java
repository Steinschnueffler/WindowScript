package linus.windowscript;

public class WindowScriptError extends Error {
	private static final long serialVersionUID = 1L;

	public WindowScriptError() {
		super();
	}

	public WindowScriptError(String message) {
		super(message);
	}

	public WindowScriptError(Throwable cause) {
		super(cause);
	}

	public WindowScriptError(String message, Throwable cause) {
		super(message, cause);
	}

	public WindowScriptError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
