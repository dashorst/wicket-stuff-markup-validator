package org.wicketstuff.htmlvalidator;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.CombineSchema;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.auto.AutoSchemaReader;

public enum DocType
{
	HTML5("")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			InputSource html5In =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/schemas/html5/html5.rnc"));
			InputSource assertionsIn =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/schemas/html5/assertions.sch"));
			PropertyMapBuilder properties = new PropertyMapBuilder();
			properties.put(ValidateProperty.ENTITY_RESOLVER, new Html5EntityResolver());
			Schema base = new AutoSchemaReader().createSchema(html5In, properties.toPropertyMap());
			Schema assertions =
				new AutoSchemaReader().createSchema(assertionsIn, properties.toPropertyMap());
			return new CombineSchema(base, assertions, properties.toPropertyMap());
		}
	},
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
	},
	HTML401_STRICT("-//W3C//DTD HTML 4.01//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML401_TRANSITIONAL("-//W3C//DTD HTML 4.01 Transitional//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML40_STRICT("-//W3C//DTD HTML 4.0//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML40_TRANSITIONAL("-//W3C//DTD HTML 4.0 Transitional//EN")
	{
		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
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
		if (firstQuote > 0)
		{
			int secondQuote = docType.indexOf('\"', firstQuote + 1);
			String publicId = docType.substring(firstQuote + 1, secondQuote);
			for (DocType curType : values())
			{
				if (curType.getIdentifier().equals(publicId))
					return curType;
			}
		}
		else
		{
			String contents = docType.substring(9, docType.length() - 1).trim();
			if (contents.equalsIgnoreCase("html"))
			{
				return HTML5;
			}
		}
		return null;
	}
}
