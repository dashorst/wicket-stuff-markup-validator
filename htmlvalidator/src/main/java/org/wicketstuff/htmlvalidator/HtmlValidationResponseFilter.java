package org.wicketstuff.htmlvalidator;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.wicket.IResponseFilter;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;

public class HtmlValidationResponseFilter implements IResponseFilter
{
	private static final Pattern DOCTYPE_PATTERN = Pattern.compile("<!DOCTYPE[^>]*>");

	private boolean ignoreKnownWicketBugs;

	private boolean ignoreAutocomplete;

	public HtmlValidationResponseFilter()
	{
		System.setProperty("org.whattf.datatype.charset-registry",
			HtmlValidationResponseFilter.class.getResource("/data/character-sets").toString());
		System.setProperty("org.whattf.datatype.lang-registry", HtmlValidationResponseFilter.class
			.getResource("/data/language-subtag-registry").toString());
	}

	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer)
	{
		Page responsePage = RequestCycle.get().getResponsePage();

		// when the responsepage is an error page, don't filter the page
		if (responsePage == null || responsePage.isErrorPage())
		{
			return responseBuffer;
		}

		String docTypeStr = getDocType(responseBuffer);
		if (docTypeStr != null)
		{
			DocType docType = DocType.getDocType(docTypeStr);
			String response = responseBuffer.toString();
			try
			{
				Schema schema = docType.createSchema();

				PropertyMapBuilder properties = new PropertyMapBuilder();
				properties.put(ValidateProperty.ERROR_HANDLER, new ErrorHandler()
				{
					@Override
					public void warning(SAXParseException arg0) throws SAXException
					{
						System.out.println(arg0.getMessage());
					}

					@Override
					public void fatalError(SAXParseException arg0) throws SAXException
					{
						System.out.println(arg0.getMessage());
					}

					@Override
					public void error(SAXParseException arg0) throws SAXException
					{
						System.out.println(arg0.getMessage());
					}
				});
				Validator validator = schema.createValidator(properties.toPropertyMap());

				XMLReader reader = docType.createParser();
				reader.setContentHandler(validator.getContentHandler());
				reader.parse(new InputSource(new StringReader(response)));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				e.printStackTrace();
			}
			catch (IncorrectSchemaException e)
			{
				e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			}
		}
		return responseBuffer;
	}

	public void setIgnoreAutocomplete(boolean ignoreAutocomplete)
	{
		this.ignoreAutocomplete = ignoreAutocomplete;
	}

	public boolean isIgnoreAutocomplete()
	{
		return ignoreAutocomplete;
	}

	public void setIgnoreKnownWicketBugs(boolean ignoreKnownWicketBugs)
	{
		this.ignoreKnownWicketBugs = ignoreKnownWicketBugs;
	}

	public boolean isIgnoreKnownWicketBugs()
	{
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

	private String getDocType(AppendingStringBuffer response)
	{
		int maxLength = Math.min(response.length(), 128);
		String contentSoFar = response.substring(0, maxLength);

		Matcher matcher = DOCTYPE_PATTERN.matcher(contentSoFar);
		if (!matcher.find())
			return null;

		return matcher.group();
	}
}
