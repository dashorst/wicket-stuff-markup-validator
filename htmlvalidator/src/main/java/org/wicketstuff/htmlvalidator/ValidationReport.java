package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.Strings;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ValidationReport implements ErrorHandler {
	private List<SAXParseException> parseErrors = new ArrayList<SAXParseException>();

	private String markup;
	private String[] lines;

	private String page;

	public ValidationReport(IRequestablePage page, String markup) {
		this.page = page.getClass().getName();
		this.markup = markup;
		this.lines = Strings.split(markup, '\n');
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
		String cssUrl = RequestCycle
				.get()
				.urlFor(new CssResourceReference(ValidationReport.class,
						"validator.css"), null).toString();
		return "    <link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ cssUrl + "\" />\n";
	}

	public String getBodyMarkup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"validationreportwindow\" class=\"validation-report\">");
		sb.append("<a href=\"#\" onclick=\"document.getElementById('validationreportwindow').style.display='none';\">Close</a>");
		generateErrorList(sb);
		generateSourceView(sb);
		sb.append("</div>");
		return sb.toString();
	}

	private void generateErrorList(StringBuilder sb) {
		sb.append("<ul>\n");
		for (SAXParseException curError : parseErrors) {
			generateErrorItem(sb, curError);
		}
		sb.append("</ul>\n");
	}

	private void generateSourceView(StringBuilder sb) {
		sb.append("<table cellpadding='0' cellspacing='0'>");
		sb.append("<thead>");
		sb.append("<tr><th colspan=\"2\">");
		sb.append("<ul>");
		sb.append("<li>");
		sb.append(page);
		sb.append("</li>");
		sb.append("<li>");
		sb.append(lines.length);
		sb.append(" lines</li>");
		sb.append("<li>");
		sb.append(Bytes.bytes(markup.length()).toString());
		sb.append("</li>");
		sb.append("</th></tr></thead>\n");
		sb.append("<tbody><tr>");
		generateLineNrs(sb);
		generateLines(sb);
		sb.append("</tr></tbody>");
		sb.append("</table>");
	}

	private void generateLineNrs(StringBuilder sb) {
		sb.append("<td><pre class=\"linenrs\">");
		for (int i = 1; i <= lines.length; i++) {
			sb.append("<a name=\"LN" + i + "\">");
			sb.append(i);
			sb.append("</a>");
			sb.append("\n");
		}
		sb.append("</pre></td>\n");
	}

	private void generateLines(StringBuilder sb) {
		sb.append("<td><pre class=\"lines\">");
		for (int i = 1; i <= lines.length; i++) {
			String line = lines[i - 1];

			sb.append("<span ");
			if (hasError(i)) {
				sb.append(" class=\"error\"");
				sb.append(" title=\"");
				sb.append(Strings.escapeMarkup(getError(i)));
				sb.append("\"");
			}
			sb.append(">");
			sb.append(Strings.escapeMarkup(line));
			if (line.isEmpty())
				sb.append("\n");
			sb.append("</span>");
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
		sb.append("<li>");
		if (error.getLineNumber() != -1) {
			sb.append("<a href=\"#LN" + error.getLineNumber()
					+ "\" class=\"position\">");
			if (error.getLineNumber() == -1 && error.getColumnNumber() == -1) {
				sb.append("-");
			} else {
				sb.append(error.getLineNumber());
				sb.append(":");
				sb.append(error.getColumnNumber());
			}
			sb.append("</a> ");
		}
		sb.append("<span class=\"message\">");
		sb.append(error.getLocalizedMessage());
		sb.append("</span></li>\n");
	}
}
