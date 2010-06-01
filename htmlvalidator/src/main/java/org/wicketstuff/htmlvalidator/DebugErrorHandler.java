package org.wicketstuff.htmlvalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class DebugErrorHandler implements ErrorHandler
{
	private static final Logger log = LoggerFactory.getLogger(DebugErrorHandler.class);

	@Override
	public void warning(SAXParseException exception)
	{
		log.warn(exception.getLineNumber() + ":" + exception.getColumnNumber() + " "
			+ exception.getMessage());
	}

	@Override
	public void fatalError(SAXParseException exception)
	{
		log.warn(exception.getLineNumber() + ":" + exception.getColumnNumber() + " "
			+ exception.getMessage());
	}

	@Override
	public void error(SAXParseException exception)
	{
		log.warn(exception.getLineNumber() + ":" + exception.getColumnNumber() + " "
			+ exception.getMessage());
	}
}