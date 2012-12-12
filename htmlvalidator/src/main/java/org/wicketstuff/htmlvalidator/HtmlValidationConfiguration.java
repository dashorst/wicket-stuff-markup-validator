package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.xml.sax.SAXParseException;

/**
 * Configuration for the HTML validator.
 */
public class HtmlValidationConfiguration {
	private List<Pattern> ignoreErrorsForWindow = new ArrayList<Pattern>();

	/**
	 * Suppresses the automatic popup when all errors match the {@code regexp}.
	 * 
	 * @param regexp
	 *            a regular expression for matching parse errors.
	 * @return this
	 */
	public HtmlValidationConfiguration dontPopupWindowFor(String regexp) {
		ignoreErrorsForWindow.add(Pattern.compile(regexp));
		return this;
	}

	/**
	 * Determines whether the validation result window should pop up for this
	 * particular error. If just one error instructs that the window should pop
	 * up, it does so.
	 * 
	 * @param error
	 *            the validation error
	 * @return <code>true</code> when the window should automatically pop up,
	 *         rendering the markup error in the face of the user
	 */
	public boolean mustShowWindowForError(SAXParseException error) {
		for (Pattern curIgnorePattern : ignoreErrorsForWindow) {
			if (curIgnorePattern.matcher(error.getMessage()).find())
				return false;
		}
		return true;
	}
}
