package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
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
		return "    <link rel=\"stylesheet\" type=\"text/css\" href=\""
			+ RequestCycle.get().urlFor(
				new ResourceReference(ValidationReport.class, "validator.css")) + "\" />\n";
	}

	public String getBodyMarkup()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"validation-report\">");
		generateErrorList(sb);
		sb.append("</div>");
		return sb.toString();
	}

	private void generateErrorList(StringBuilder sb)
	{
		sb.append("<ul>\n");
		for (SAXParseException curError : parseErrors)
		{
			generateErrorItem(sb, curError);
		}
		sb.append("</ul>\n");
	}

	private void generateErrorItem(StringBuilder sb, SAXParseException error)
	{
		sb.append("<li><span class=\"position\">");
		if (error.getLineNumber() == -1 && error.getColumnNumber() == -1)
		{
			sb.append("-");
		}
		else
		{
			sb.append(error.getLineNumber());
			sb.append(":");
			sb.append(error.getColumnNumber());
		}
		sb.append("</span> <span class=\"message\">");
		sb.append(error.getLocalizedMessage());
		sb.append("</span></li>\n");
	}
}
