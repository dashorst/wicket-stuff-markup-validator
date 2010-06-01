package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ValidationReport implements ErrorHandler
{
	private List<SAXParseException> parseErrors = new ArrayList<SAXParseException>();

	@Override
	public void error(SAXParseException exception)
	{
		parseErrors.add(exception);
	}

	@Override
	public void fatalError(SAXParseException exception)
	{
		parseErrors.add(exception);
	}

	@Override
	public void warning(SAXParseException exception)
	{
		parseErrors.add(exception);
	}

	public boolean isValid()
	{
		return parseErrors.isEmpty();
	}

	public String getHeadMarkup()
	{
		return "";
	}

	public String getBodyMarkup()
	{
		return parseErrors.toString();
	}
}
