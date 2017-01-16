package de.omikron.helper.client;

public class JavascriptParameterValidator {

	/**
	 * The RegEx for detecting java script tags in any particular way a browser
	 * accept html javascript elements
	 */
	private static final String	htmlJavaScriptPattern	= "(<.*[sS][cC][rR][iI][pP][tT].*>)(.*)(<.*/.*[sS][cC][rR][iI][pP][tT].*>)";

	/**
	 * To increase performance just check strings which are at least 17
	 * characters long (<script></script>)
	 */
	private static final int	minLengthToBeJavascript	= 16;																			// <script></script>

	/**
	 * This method checks a string if he's containing java script. In detail:
	 * checks the given string for html <script> elements and there contents. if
	 * something is found the corresponding parts will be removed.
	 * 
	 * @param parameterValue
	 *            the string to check
	 * @return the cleaned given String if javascript was found or the untouched
	 *         string if no javascript was found
	 */
	public static String cleanStringFromJavascript(final String parameterValue) {
		// TODO searching script tags is not enough

		if (parameterValue.length() > minLengthToBeJavascript)
			return parameterValue.replaceAll(htmlJavaScriptPattern, "");
		else
			return parameterValue;
	}
}