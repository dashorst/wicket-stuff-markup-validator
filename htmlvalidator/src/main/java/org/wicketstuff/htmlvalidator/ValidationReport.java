package org.wicketstuff.htmlvalidator;

import static org.wicketstuff.htmlvalidator.ValidatorUtils.cssFor;
import static org.wicketstuff.htmlvalidator.ValidatorUtils.imgFor;
import static org.wicketstuff.htmlvalidator.ValidatorUtils.jsFor;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class ValidationReport implements ErrorHandler {
	private static final Logger log = LoggerFactory
			.getLogger(ValidationReport.class);
	private List<SAXParseException> parseErrors = new ArrayList<SAXParseException>();

	private String doctype;
	private String markup;
	private String[] lines;

	private String page;

	public ValidationReport(IRequestablePage page, String markup,
			DocType doctype) {
		if (page != null)
			this.page = page.getClass().getName();
		else
			this.page = "<unable to determine page>";
		this.markup = markup;
		this.lines = Strings.split(markup, '\n');
		this.doctype = doctype.name(); // getIdentifier();
	}

	@Override
	public void error(SAXParseException exception) {
		log.error(
				"Line {} contains error at {}: {}",
				new Object[] { exception.getLineNumber(),
						exception.getColumnNumber(), exception.getMessage() });
		parseErrors.add(exception);
	}

	@Override
	public void fatalError(SAXParseException exception) {
		log.error("Line {} contains fatal error at {}: {}", new Object[] {
				exception.getLineNumber(), exception.getColumnNumber(),
				exception.getMessage() });
		parseErrors.add(exception);
	}

	@Override
	public void warning(SAXParseException exception) {
		log.warn(
				"Line {} contains warning at {}: {}",
				new Object[] { exception.getLineNumber(),
						exception.getColumnNumber(), exception.getMessage() });
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
		sb.append("\t\t\twindow.addEventListener('load',function (event) { prettyPrint() },false);\n");
		sb.append("\t\t" + "} else {\n");
		sb.append("\t\t\twindow.attachEvent('onload',function (event) { prettyPrint() });\n");
		sb.append("\t\t" + "}\n");
		sb.append("\t" + "</script>\n");

		return sb.toString();
	}

	public String getBodyMarkup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"validationreportwindow\" class=\"validation-report\""
				+ (isValid() ? " style=\"display:none\"" : "") + ">");
		sb.append("<div class=\"validation-header\">\n");
		sb.append("<button class=\"close\" onclick=\"document.getElementById('validationreportwindow').style.display='none';\">×</button>\n");
		if (isValid())
			sb.append("<h3>Generated document is valid " + doctype + "</h3>\n");
		else
			sb.append("<h3>Markup errors found in generated document</h3>\n");
		sb.append("</div>\n");
		sb.append("<div class=\"validation-content\">\n");
		generateSourceView(sb);
		sb.append("</div>\n");
		sb.append("<div class=\"validation-footer\">\n");
		generateFooter(sb);
		sb.append("</div>\n");
		sb.append("</div>");
		sb.append("<a class=\"validationreportbadge\" href=\"#\" onclick=\"document.getElementById('validationreportwindow').style.display='block';\" title=\"Content is "
				+ (isValid() ? "valid" : "invalid") + " " + doctype + "\">");
		sb.append(imgFor("HTML5_Badge_32.png").toString());
		sb.append("</a>");
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
			String msg = error.getLocalizedMessage().substring(0, pos);
			sb.append(Strings.escapeMarkup(msg));
		} else
			sb.append(Strings.escapeMarkup(error.getLocalizedMessage()));
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
		sb.append("<label class=\"control-label\" for=\"validationReportPage\">Validated page:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\"\" readonly id=\"validationReportPage\" value=\""
				+ page + "\"/>");
		sb.append("</div>\n");
		sb.append("</div>\n");

		sb.append("<div class=\"control-group\">\n");
		sb.append("<label class=\"control-label\" for=\"validationReportResult\">Validation result:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\""
				+ (isValid() ? "validation-success" : "validation-error")
				+ "\" readonly id=\"validationReportResult\" value=\""
				+ (isValid() ? "Valid" : "Invalid") + " " + doctype + "\"/>");
		sb.append("</div>\n");
		sb.append("</div>\n");

		sb.append("<div class=\"control-group\">\n");
		sb.append("<label class=\"control-label\" for=\"validationDocType\">Detected doctype:</label>\n");
		sb.append("<div class=\"controls\">");
		sb.append("<input type=\"text\" class=\"\" readonly id=\"validationDocType\" value=\""
				+ doctype + "\"/>");
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
		Tag td = new Tag("td");
		Tag pre = new Tag("pre").attr("class", "lines", "prettyprint");

		sb.append(td.getOpenTag());
		sb.append(pre.getOpenTag());
		for (int i = 1; i <= lines.length; i++) {
			String line = lines[i - 1];

			if (hasError(i)) {
				generateErrorLine(sb, line, i);
			} else {
				generateCodeLine(sb, line, i);
			}
		}
		sb.append(pre.getCloseTag());
		sb.append(td.getCloseTag());
		sb.append("\n");
	}

	private void generateErrorLine(StringBuilder sb, String line, int i) {
		SAXParseException error = getError(i);

		Tag code = new Tag("code");

		if (hasError(i)) {
			code.attr("class", "error");
			code.attr("title", Strings.escapeMarkup(error.getMessage()));
		} else {
			code.attr("class", "language-xml");
		}
		if (Strings.isEmpty(line))
			code.setBody("&nbsp;");
		else
			code.setBody(Strings.escapeMarkup(line));

		sb.append(code.toString());
	}

	private void generateCodeLine(StringBuilder sb, String line, int i) {
		Tag code = new Tag("code");
		code.attr("class", "language-xml");
		if (Strings.isEmpty(line))
			code.setBody("&nbsp;");
		else
			code.setBody(Strings.escapeMarkup(line));

		sb.append(code.toString());
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
