package org.wicketstuff.htmlvalidator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.util.string.Strings;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ValidationReport implements ErrorHandler {
	private List<SAXParseException> parseErrors = new ArrayList<SAXParseException>();

	private String[] markup;

	public ValidationReport(String markup) {
		this.markup = Strings.split(markup, '\n');
	}

	@Override
	public void error(SAXParseException exception) {
		parseErrors.add(exception);
	}

	@Override
	public void fatalError(SAXParseException exception) {
		parseErrors.add(exception);
	}

	@Override
	public void warning(SAXParseException exception) {
		parseErrors.add(exception);
	}

	public boolean isValid() {
		return parseErrors.isEmpty();
	}

	public String getHeadMarkup() {
		return "    <link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ RequestCycle.get().urlFor(
						new ResourceReference(ValidationReport.class,
								"validator.css")) + "\" />\n";
	}

	public String getBodyMarkup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"validation-report\">");
		generateErrorList(sb);
		sb.append("</div>");
		return sb.toString();
	}

	private void generateErrorList(StringBuilder sb) {
		sb.append("<table>");
		sb.append("<tr>");
		generateLineNrs(sb);
		generateLines(sb);
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<ul>\n");
		for (SAXParseException curError : parseErrors) {
			generateErrorItem(sb, curError);
		}
		sb.append("</ul>\n");
	}

	private void generateLineNrs(StringBuilder sb) {
		sb.append("<td><pre class=\"linenrs\">");
		for (int i = 1; i <= markup.length; i++) {
			sb.append("<span id=\"LN" + i + "\">");
			sb.append(i);
			sb.append("</span>");
			sb.append("\n");
		}
		sb.append("</pre></td>\n");
	}

	private void generateLines(StringBuilder sb) {
		sb.append("<td><pre class=\"lines\">");
		for (int i = 1; i <= markup.length; i++) {
			String line = markup[i - 1];

			sb.append("<div id=\"LC");
			sb.append(i);
			sb.append("\"");
			if (hasError(i)) {
				sb.append(" class=\"error\"");
				sb.append(" title=\"");
				sb.append(Strings.escapeMarkup(getError(i)));
				sb.append("\"");
			}
			sb.append(">");
			sb.append(Strings.escapeMarkup(line));
			sb.append("</div>\n");
		}
		sb.append("</pre></td>\n");
	}

	private String getError(int i) {
		for (SAXParseException error : parseErrors) {
			if (error.getLineNumber() == i)
				return error.getMessage();
		}
		return "";
	}

	private boolean hasError(int i) {
		for (SAXParseException error : parseErrors) {
			if (error.getLineNumber() == i)
				return true;
		}
		return false;
	}

	private void generateErrorItem(StringBuilder sb, SAXParseException error) {
		sb.append("<li><span class=\"position\">");
		if (error.getLineNumber() == -1 && error.getColumnNumber() == -1) {
			sb.append("-");
		} else {
			sb.append(error.getLineNumber());
			sb.append(":");
			sb.append(error.getColumnNumber());
		}
		sb.append("</span> <span class=\"message\">");
		sb.append(error.getLocalizedMessage());
		sb.append("</span></li>\n");
	}
}
