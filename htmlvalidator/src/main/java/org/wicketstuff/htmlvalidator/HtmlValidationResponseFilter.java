package org.wicketstuff.htmlvalidator;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.wicket.IResponseFilter;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;
import com.thaiopensource.validate.auto.AutoSchemaReader;

public class HtmlValidationResponseFilter implements IResponseFilter {

	private boolean ignoreKnownWicketBugs;

	private boolean ignoreAutocomplete;

	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer) {
		Page responsePage = RequestCycle.get().getResponsePage();

		// when the responsepage is an error page, don't filter the page
		if (responsePage == null || responsePage.isErrorPage()) {
			return responseBuffer;
		}

		String response = responseBuffer.toString();

		if (isXHtml(responseBuffer)) {
			if (isIgnoreKnownWicketBugs()) {
				response = response.replaceAll("&wicket:ignoreIfNotActive",
						"&amp;wicket:ignoreIfNotActive").replaceAll("&&",
						"&amp;&amp;");
			}
			InputSource xhtml10In = new InputSource(
					HtmlValidationResponseFilter.class
							.getResourceAsStream("/schemas/xhtml10/xhtml.rng"));
			try {
				PropertyMapBuilder properties = new PropertyMapBuilder();
				properties.put(ValidateProperty.ENTITY_RESOLVER,
						new EntityResolver() {

							@Override
							public InputSource resolveEntity(String publicId,
									String systemId) throws SAXException,
									IOException {
								System.out.println(systemId);
								int indexOfValidator = systemId
										.lastIndexOf("htmlvalidator");
								if (indexOfValidator != -1)
									systemId = systemId
											.substring(indexOfValidator + 14);
								return new InputSource(
										HtmlValidationResponseFilter.class
												.getResourceAsStream("/schemas/xhtml10/"
														+ systemId));
							}
						});
				Schema schema = new AutoSchemaReader().createSchema(xhtml10In,
						properties.toPropertyMap());

				properties = new PropertyMapBuilder();
				Validator validator = schema.createValidator(properties
						.toPropertyMap());

				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(validator.getContentHandler());
				reader.parse(new InputSource(new StringReader(response)));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IncorrectSchemaException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

			// try {
			// ValidationHandler handler = new ValidationHandler(response, "") {
			// @Override
			// protected boolean isFalseError(LineIssue lineIssue) {
			// if (isIgnoreKnownWicketBugs()
			// && isKnownWicketBug(lineIssue))
			// return true;
			// if (isIgnoreAutocomplete()
			// && isAutocompleteError(lineIssue))
			// return true;
			//
			// return super.isFalseError(lineIssue);
			// }
			// };
			// handler.parse();
			// if (!handler.isValid()) {
			// insertMarkup(responseBuffer, handler);
			// }
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
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
	//
	// private boolean isKnownWicketBug(LineIssue lineIssue) {
	// return isWicket2033(lineIssue) || isWicket2316(lineIssue);
	// }
	//
	// private boolean isWicket2033(LineIssue lineIssue) {
	// return
	// "The reference to entity &quot;wicket:ignoreIfNotActive&quot; must end with the ';' delimiter."
	// .equals(lineIssue.getMessage());
	// }
	//
	// private boolean isWicket2316(LineIssue lineIssue) {
	// return
	// "The entity name must immediately follow the '&amp;' in the entity reference."
	// .equals(lineIssue.getMessage());
	// }

	private boolean isXHtml(AppendingStringBuffer response) {
		int maxLength = Math.min(response.length(), 128);
		String contentSoFar = response.substring(0, maxLength);
		return contentSoFar.indexOf("<!DOCTYPE") != -1
				&& contentSoFar.indexOf("-//W3C//DTD XHTML") != -1;
	}
}
