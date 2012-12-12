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
	 * The errors are still available in the validation report window, but won't
	 * be 'in your face'. This is especially handy when the validator reports an
	 * error that you can't immediately fix, but is prevalent. You want to be
	 * able to ignore that particular error, but still get notified of any other
	 * error.
	 * 
	 * For example, you can suppress the pop up for the &lt;blink&gt; tag by
	 * configuring:
	 * 
	 * <pre>
	 * {@literal
	 * configuration.dontPopupWindowFor("element \"blink\" not allowed here");
	 * }
	 * </pre>
	 * 
	 * or just (if you want to be less specific, and ignore any errors generated
	 * by the text blink):
	 * 
	 * <pre>
	 * {@literal
	 * configuration.dontPopupWindowFor("\"blink\"");
	 * }
	 * </pre>
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
