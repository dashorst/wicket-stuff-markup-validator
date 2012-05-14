package org.wicketstuff.htmlvalidator;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
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
		StringBuilder sb = new StringBuilder();
		sb.append("\t" + cssFor("prettify/prettify.css") + "\n");
		sb.append("\t" + cssFor("validator.css") + "\n");
		sb.append("\t" + jsFor("prettify/prettify.js") + "\n");
		sb.append("\t" + "<script>\n");
		sb.append("\t\t" + "if(window.addEventListener) {\n");
		sb.append("\t\t\t"
				+ "window.addEventListener('load',function (event) { prettyPrint() },false);\n");
		sb.append("\t\t" + "}\n");
		sb.append("\t\t" + "else {\n");
		sb.append("\t\t\t"
				+ "window.attachEvent('onload',function (event) { prettyPrint() });\n");
		sb.append("\t\t" + "}\n");
		sb.append("\t" + "</script>\n");
		return sb.toString();
	}

	private String cssFor(String filename) {
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ urlFor(filename) + "\" />";
	}

	private String jsFor(String filename) {
		return "<script src=\"" + urlFor(filename) + "\"></script>";
	}

	private String urlFor(String filename) {
		RequestCycle requestCycle = RequestCycle.get();
		PackageResourceReference reference = new PackageResourceReference(
				ValidationReport.class, filename);
		CharSequence url = requestCycle.urlFor(reference, null);
		return url.toString();
	}

	public String getBodyMarkup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"validationreportwindow\" class=\"validation-report\">");
		sb.append("<div class=\"validation-header\">\n");
		sb.append("<button class=\"close\" onclick=\"document.getElementById('validationreportwindow').style.display='none';\">×</button>\n");
		sb.append("<h3>Markup errors found in generated document</h3>\n");
		sb.append("</div>\n");
		sb.append("<div class=\"validation-content\">\n");
		generateSourceView(sb);
		sb.append("</div>\n");
		sb.append("<div class=\"validation-footer\">\n");
		generateFooter(sb);
		sb.append("</div>\n");
		sb.append("</div>");
		return sb.toString();
	}

	private void generateErrorItem(StringBuilder sb, SAXParseException error) {
		sb.append("<div class=\"markup-error\">\n");
		sb.append("<button class=\"close\" title=\"Remove this message\" onclick=\"this.parentNode.style.display='none'\">×</button>\n");
		if (error.getLineNumber() != -1) {
			sb.append("<a href=\"#LN" + error.getLineNumber()
					+ "\" class=\"position\">");
			if (error.getLineNumber() == -1 && error.getColumnNumber() == -1) {
				sb.append("-");
			} else {
				sb.append(error.getLineNumber());
				// sb.append(":");
				// sb.append(error.getColumnNumber());
			}
			sb.append("</a> ");
		}
		if (error.getMessage().contains(";")) {
			int pos = error.getLocalizedMessage().indexOf(';');
			sb.append(error.getLocalizedMessage().substring(0, pos));
		} else
			sb.append(error.getLocalizedMessage());
		sb.append("</div>\n");
	}

	private void generateSourceView(StringBuilder sb) {
		sb.append("<table cellpadding='0' cellspacing='0'>");
		sb.append("<thead>");
		sb.append("<tr>\n");
		sb.append("<td colspan=\"2\">\n");
		generateHeader(sb);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("</thead>\n");
		sb.append("<tbody>\n");
		sb.append("<tr>\n");
		sb.append("<td class=\"linenrs\">&nbsp;</td>\n");
		sb.append("<td>\n");
		generateErrorList(sb);
		sb.append("</td>\n");
		sb.append("</tr>\n");
		sb.append("<tr>");
		generateLineNrs(sb);
		generateLines(sb);
		sb.append("</tr>\n");
		sb.append("</tbody>\n");
		sb.append("</table>\n");
	}

	private void generateFooter(StringBuilder sb) {
		sb.append("<a href=\"#\" onclick=\"this.parentNode.parentNode.style.display='none'\" class=\"btn\">Close</a>\n");
	}

	private void generateHeader(StringBuilder sb) {
		sb.append("<div class=\"control-group\">\n");
		sb.append("<label class=\"control-label\" for=\"validationReportPage\">Offending page:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\"\" readonly id=\"validationReportPage\" value=\""
				+ page + "\"/>");
		sb.append("</div>\n");
		sb.append("</div>\n");

		sb.append("<div class=\"control-group\">\n");
		sb.append("<label class=\"control-label\" for=\"validationReportLines\">Lines:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\"\" readonly  id=\"validationReportLines\" value=\""
				+ lines.length + "\"/>");
		sb.append("</div>\n");
		sb.append("</div>\n");

		sb.append("<div class=\"control-group\">\n");
		sb.append("<label class=\"control-label\" for=\"validationReportSize\">Size:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\"\" readonly  id=\"validationReportSize\" value=\""
				+ Bytes.bytes(markup.length()).toString() + "\"/>");
		sb.append("</div>\n");
		sb.append("</div>\n");
	}

	private void generateErrorList(StringBuilder sb) {
		sb.append("<div class=\"markup-errors\">\n");
		for (SAXParseException curError : parseErrors) {
			generateErrorItem(sb, curError);
		}
		sb.append("</div>\n");
	}

	private void generateLineNrs(StringBuilder sb) {
		sb.append("<td class=\"linenrs\"><pre>");
		for (int i = 1; i <= lines.length; i++) {
			sb.append("<a name=\"LN" + i + "\">");
			sb.append(i);
			sb.append("</a>");
			sb.append("\n");
		}
		sb.append("</pre></td>\n");
	}

	private void generateLines(StringBuilder sb) {
		sb.append("<td><pre class=\"lines prettyprint\">");
		for (int i = 1; i <= lines.length; i++) {
			String line = lines[i - 1];

			if (hasError(i)) {
				generateErrorLine(sb, line, i);
			} else {
				generateCodeLine(sb, line, i);
			}
		}
		sb.append("</pre></td>\n");
	}

	private void generateErrorLine(StringBuilder sb, String line, int i) {
		SAXParseException error = getError(i);

		sb.append("<code ");
		if (hasError(i)) {
			sb.append(" class=\"error\"");
			sb.append(" title=\"");
			sb.append(Strings.escapeMarkup(error.getMessage()));
			sb.append("\"");
		} else {
			sb.append("class=\"language-xml\"");
		}
		sb.append(">");
		sb.append(Strings.escapeMarkup(line));
		if (line.trim().isEmpty())
			sb.append("&nbsp;\n");
		sb.append("</code>");
	}

	private void generateCodeLine(StringBuilder sb, String line, int i) {
		sb.append("<code ");
		sb.append("class=\"language-xml\"");
		sb.append(">");
		sb.append(Strings.escapeMarkup(line));
		if (line.trim().isEmpty())
			sb.append("&nbsp;\n");
		sb.append("</code>");
	}

	private SAXParseException getError(int i) {
		for (SAXParseException error : parseErrors) {
			if (error.getLineNumber() == i)
				return error;
		}
		return null;
	}

	private boolean hasError(int i) {
		for (SAXParseException error : parseErrors) {
			if (error.getLineNumber() == i)
				return true;
		}
		return false;
	}
}
