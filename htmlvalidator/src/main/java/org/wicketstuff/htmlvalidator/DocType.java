package org.wicketstuff.htmlvalidator;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.auto.AutoSchemaReader;

public enum DocType
{
	XHTML10_STRICT("-//W3C//DTD XHTML 1.0 Strict//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			InputSource xhtml10In =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/schemas/xhtml10/xhtml-strict.rng"));
			PropertyMapBuilder properties = new PropertyMapBuilder();
			properties.put(ValidateProperty.ENTITY_RESOLVER, new XHtmlEntityResolver());
			return new AutoSchemaReader().createSchema(xhtml10In, properties.toPropertyMap());
		}
	},
	XHTML10_TRANSITIONAL("-//W3C//DTD XHTML 1.0 Transitional//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			InputSource xhtml10In =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/schemas/xhtml10/xhtml.rng"));
			PropertyMapBuilder properties = new PropertyMapBuilder();
			properties.put(ValidateProperty.ENTITY_RESOLVER, new XHtmlEntityResolver());
			return new AutoSchemaReader().createSchema(xhtml10In, properties.toPropertyMap());
		}
	};

	private String identifier;

	DocType(String identifier)
	{
		this.identifier = identifier;
	}

	public abstract Schema createSchema() throws IOException, SAXException,
			IncorrectSchemaException;

	public String getIdentifier()
	{
		return identifier;
	}

	public static DocType getDocType(String docType)
	{
		int firstQuote = docType.indexOf('\"');
		int secondQuote = docType.indexOf('\"', firstQuote + 1);
		String publicId = docType.substring(firstQuote + 1, secondQuote);
		for (DocType curType : values())
		{
			if (curType.getIdentifier().equals(publicId))
				return curType;
		}
		return null;
	}
}
