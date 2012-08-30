package org.wicketstuff.htmlvalidator;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.response.filter.IResponseFilter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;

/**
 * Markup validating response filter that inserts an error report when the
 * rendered markup is not valid according to the specified DOCTYPE.
 * 
 * <h3>DOCTYPE</h3>
 * 
 * The validator tries to determine the markup standard that needs to be
 * validated based on the DOCTYPE that is defined in the rendered document. See
 * {@link DocType} for the supported standards.
 * 
 * When no DOCTYPE is specified, or the DOCTYPE is unknown, this filter will
 * call {@link #onUnknownDocType(AppendingStringBuffer)}. The default
 * implementation will log a message in the application log (using level INFO).
 * 
 * <h3>Valid markup</h3>
 * 
 * When the rendered markup is valid, an icon will be inserted in the response
 * markup telling the user that the rendered markup is valid.
 * 
 * Override {@link #onValidMarkup(AppendingStringBuffer, ValidationReport)} to
 * change this behavior.
 * 
 * <h3>Invalid markup</h3>
 * 
 * When the rendered markup is not valid, a popup will be rendered and presented
 * to the user, showing the detected standard, page class and the discovered
 * errors. It also shows the markup with the errors inline.
 * 
 * Override {@link #onInvalidMarkup(AppendingStringBuffer, ValidationReport)} to
 * change this default behavior.
 * 
 * <h3>Metadata</h3>
 * 
 * The filter reports the validation result in the response page's meta data.
 * You can retrieve the validation result text with the
 * {@link HtmlValidationResultKey} key.
 * 
 * <pre>
 * String result = page.getMetaData(HtmlValidationResultKey.KEY);
 * </pre>
 */
public class HtmlValidationResponseFilter implements IResponseFilter {
	private static final Pattern DOCTYPE_PATTERN = Pattern
			.compile("<!DOCTYPE[^>]*>");

	private static final Logger log = LoggerFactory
			.getLogger(HtmlValidationResponseFilter.class);

	/**
	 * Called when the validated markup does not contain any errors.
	 * 
	 * @param responseBuffer
	 *            the validated response markup
	 * @param report
	 *            the validation report
	 */
	protected void onValidMarkup(AppendingStringBuffer responseBuffer,
			ValidationReport report) {
		IRequestablePage responsePage = getResponsePage();
		DocType doctype = getDocType(responseBuffer);

		log.info("Markup for {} is valid {}",
				responsePage != null ? responsePage.getClass().getName()
						: "<unable to determine page class>", doctype.name());

		String head = report.getHeadMarkup();
		String body = report.getBodyMarkup();

		int indexOfHeadClose = responseBuffer.lastIndexOf("</head>");
		responseBuffer.insert(indexOfHeadClose, head);

		int indexOfBodyClose = responseBuffer.lastIndexOf("</body>");
		responseBuffer.insert(indexOfBodyClose, body);
	}

	/**
	 * Called when the validated markup contains errors.
	 * 
	 * @param responseBuffer
	 *            the validated response markup
	 * @param report
	 *            the validation report containing the errors
	 */
	protected void onInvalidMarkup(AppendingStringBuffer responseBuffer,
			ValidationReport report) {
		String head = report.getHeadMarkup();
		String body = report.getBodyMarkup();

		int indexOfHeadClose = responseBuffer.lastIndexOf("</head>");
		responseBuffer.insert(indexOfHeadClose, head);

		int indexOfBodyClose = responseBuffer.lastIndexOf("</body>");
		responseBuffer.insert(indexOfBodyClose, body);
	}

	/**
	 * Called when no known {@link DocType} could be determined from the
	 * {@code responseBuffer}. The markup is not validated.
	 * 
	 * @param responseBuffer
	 *            the response markup that could not be validated.
	 */
	protected void onUnknownDocType(AppendingStringBuffer response) {
		IRequestablePage responsePage = getResponsePage();

		String detectionString = getFirstCharacters(response, 128);

		if (responsePage != null) {
			log.info("No or unknown DOCTYPE detected for page {}: {}",
					responsePage.getClass().getName(), detectionString);
		} else {
			log.info("No or unknown DOCTYPE detected: {}", detectionString);
		}
	}

	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer) {
		IRequestablePage responsePage = getResponsePage();
		if (responsePage != null && !(responsePage instanceof Page)
				|| RequestCycle.get().find(AjaxRequestTarget.class) != null) {
			return responseBuffer;
		}

		DocType docType = getDocType(responseBuffer);

		if (docType == null) {
			setMetaData("Unknown doctype");
			onUnknownDocType(responseBuffer);
			return responseBuffer;
		}

		try {
			ValidationReport report = validateMarkup(responseBuffer.toString(),
					docType);
			if (report.isValid()) {
				setMetaData("Markup is valid " + docType);
				onValidMarkup(responseBuffer, report);
			} else {
				setMetaData("Markup is invalid " + docType);
				onInvalidMarkup(responseBuffer, report);
			}
		} catch (Exception e) {
			log.error(
					"Error validating markup from "
							+ responsePage.getClass().getName() + ": "
							+ e.getMessage(), e);
		}
		return responseBuffer;
	}

	private ValidationReport validateMarkup(String response, DocType docType)
			throws Exception {
		IRequestablePage responsePage = getResponsePage();
		ValidationReport report = new ValidationReport(responsePage, response,
				docType);

		PropertyMapBuilder properties = new PropertyMapBuilder();
		properties.put(ValidateProperty.ERROR_HANDLER, report);

		Schema schema = docType.createSchema();
		Validator validator = schema
				.createValidator(properties.toPropertyMap());

		XMLReader reader = docType.createParser();
		reader.setContentHandler(validator.getContentHandler());
		try {
			reader.parse(new InputSource(new StringReader(response)));
		} catch (SAXParseException parseError) {
			report.fatalError(parseError);
		}
		return report;
	}

	private IRequestablePage getResponsePage() {
		IRequestHandler requestHandler = RequestCycle.get()
				.getActiveRequestHandler();

		IRequestablePage responsePage = null;
		if (requestHandler instanceof IPageRequestHandler) {
			responsePage = ((IPageRequestHandler) requestHandler).getPage();
		}
		return responsePage;
	}

	private void setMetaData(String msg) {
		IRequestablePage responsePage = getResponsePage();
		if (responsePage instanceof Page) {
			((Page) responsePage).setMetaData(HtmlValidationResultKey.KEY, msg);
		}
	}

	/**
	 * Gets the DOCTYPE from the response. Returns {@code null} when the DOCTYPE
	 * is not detected, or unknown. See {@link DocType} for supported DOCTYPEs.
	 */
	protected DocType getDocType(AppendingStringBuffer response) {
		String contentSoFar = getFirstCharacters(response, 128);

		Matcher matcher = DOCTYPE_PATTERN.matcher(contentSoFar);
		if (!matcher.find())
			return null;

		String docTypeStr = matcher.group();

		if (Strings.isEmpty(docTypeStr))
			return null;

		DocType docType = DocType.getDocType(docTypeStr);
		return docType;
	}

	private String getFirstCharacters(AppendingStringBuffer response, int max) {
		int maxLength = Math.min(response.length(), max);
		String contentSoFar = response.substring(0, maxLength);
		return contentSoFar;
	}
}
