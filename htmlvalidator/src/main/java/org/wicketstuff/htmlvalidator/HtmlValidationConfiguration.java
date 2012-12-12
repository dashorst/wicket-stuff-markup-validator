package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.xml.sax.SAXParseException;

public class HtmlValidationConfiguration {
	private List<Pattern> ignoreErrorsForWindow = new ArrayList<Pattern>();

	public HtmlValidationConfiguration() {
	}
	
	public HtmlValidationConfiguration dontPopupWindowFor(String regexp) {
		ignoreErrorsForWindow.add(Pattern.compile(regexp));
		return this;
	}

	public boolean mustShowWindowForError(SAXParseException error) {
		for (Pattern curIgnorePattern : ignoreErrorsForWindow) {
			if (curIgnorePattern.matcher(error.getMessage()).find())
				return false;
		}
		return true;
	}
}
