package de.omikron.helper.domain;

import java.io.Serializable;

public class JSONErrorMessage implements Serializable {

	private static final long	serialVersionUID	= 6873456499580127008L;
	private final String		error;
	private final String		stacktrace;

	/**
	 * 
	 * @param error
	 *            NOT NULL
	 * @param stacktrace
	 *            can be null if no stacktrace is available
	 */
	public JSONErrorMessage(final String error, final String stacktrace) {
		if (error == null)
			throw new RuntimeException("Parameter error is null.");
		this.error = error;
		this.stacktrace = stacktrace;
	}

	/**
	 * 
	 * @return the error message. is never NULL
	 */
	public String getError() {
		return error;
	}

	/**
	 * 
	 * @return the stacktrace if available or NULL if not available
	 */
	public String getStacktrace() {
		return stacktrace;
	}

	@Override
	public String toString() {
		return error + "\n\tat" + stacktrace;
	}

}
