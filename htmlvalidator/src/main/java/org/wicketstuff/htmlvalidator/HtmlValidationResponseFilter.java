package org.wicketstuff.htmlvalidator;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.wicket.Page;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.IPageRequestHandler;
import org.apache.wicket.response.filter.IResponseFilter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;

public class HtmlValidationResponseFilter implements IResponseFilter {
	private static final Pattern DOCTYPE_PATTERN = Pattern
			.compile("<!DOCTYPE[^>]*>");

	private boolean ignoreKnownWicketBugs;

	private boolean ignoreAutocomplete;

	public HtmlValidationResponseFilter() {
		System.setProperty(
				"org.whattf.datatype.charset-registry",
				HtmlValidationResponseFilter.class.getResource(
						"/data/character-sets").toString());
		System.setProperty(
				"org.whattf.datatype.lang-registry",
				HtmlValidationResponseFilter.class.getResource(
						"/data/language-subtag-registry").toString());
	}

	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer) {
		IRequestHandler requestHandler = RequestCycle.get()
				.getActiveRequestHandler();
		IRequestablePage responsePage = null;
		if (requestHandler instanceof IPageRequestHandler) {
			responsePage = ((IPageRequestHandler) requestHandler).getPage();
		}

		// when the responsepage is an error page, don't filter the page
		if (!(responsePage instanceof Page)
				|| ((Page) responsePage).isErrorPage()) {
			return responseBuffer;
		}

		String docTypeStr = getDocType(responseBuffer);
		if (docTypeStr != null) {
			DocType docType = DocType.getDocType(docTypeStr);
			String response = responseBuffer.toString();
			try {
				Schema schema = docType.createSchema();
				ValidationReport report = new ValidationReport(responsePage, response, docType);

				PropertyMapBuilder properties = new PropertyMapBuilder();
				properties.put(ValidateProperty.ERROR_HANDLER, report);
				Validator validator = schema.createValidator(properties
						.toPropertyMap());

				XMLReader reader = docType.createParser();
				reader.setContentHandler(validator.getContentHandler());
				try {
					reader.parse(new InputSource(new StringReader(response)));
				} catch (SAXParseException parseError) {
					report.fatalError(parseError);
				}
				insertMarkup(responseBuffer, report);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IncorrectSchemaException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return responseBuffer;
	}

	public void setIgnoreAutocomplete(boolean ignoreAutocomplete) {
		this.ignoreAutocomplete = ignoreAutocomplete;
	}

	public boolean isIgnoreAutocomplete() {
		return ignoreAutocomplete;
	}

	public void setIgnoreKnownWicketBugs(boolean ignoreKnownWicketBugs) {
		this.ignoreKnownWicketBugs = ignoreKnownWicketBugs;
	}

	public boolean isIgnoreKnownWicketBugs() {
		return ignoreKnownWicketBugs;
	}

	// private boolean isAutocompleteError(LineIssue lineIssue) {
	// return
	// "Attribute &quot;autocomplete&quot; must be declared for element type &quot;input&quot;."
	// .equals(lineIssue.getMessage());
	// }

	private String getDocType(AppendingStringBuffer response) {
		int maxLength = Math.min(response.length(), 128);
		String contentSoFar = response.substring(0, maxLength);

		Matcher matcher = DOCTYPE_PATTERN.matcher(contentSoFar);
		if (!matcher.find())
			return null;

		return matcher.group();
	}

	private void insertMarkup(AppendingStringBuffer original,
			ValidationReport report) {
		if (report.isValid())
			return;

		String head = report.getHeadMarkup();
		String body = report.getBodyMarkup();

		int indexOfHeadClose = original.lastIndexOf("</head>");
		original.insert(indexOfHeadClose, head);

		int indexOfBodyClose = original.lastIndexOf("</body>");
		original.insert(indexOfBodyClose, body);
	}
}
