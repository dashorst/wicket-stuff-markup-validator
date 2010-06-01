package org.wicketstuff.htmlvalidator;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nu.validator.htmlparser.common.DoctypeExpectation;
import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.common.XmlViolationPolicy;
import nu.validator.htmlparser.sax.HtmlParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.CombineSchema;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.auto.AutoSchemaReader;
import com.thaiopensource.validate.rng.CompactSchemaReader;

public enum DocType
{
	HTML5("")
	{
		@Override
		public XMLReader createParser()
		{
			return createHtmlParser(DoctypeExpectation.HTML);
		}

		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			InputSource html5In =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/relaxng/html5.rnc"));
			InputSource assertionsIn =
				new InputSource(HtmlValidationResponseFilter.class
					.getResourceAsStream("/relaxng/assertions.sch"));
			PropertyMapBuilder properties = new PropertyMapBuilder();
			properties.put(ValidateProperty.ENTITY_RESOLVER, new Html5EntityResolver());
			properties.put(ValidateProperty.ERROR_HANDLER, new DebugErrorHandler());
			Schema base =
				CompactSchemaReader.getInstance().createSchema(html5In, properties.toPropertyMap());
			Schema assertions =
				new AutoSchemaReader().createSchema(assertionsIn, properties.toPropertyMap());
			return new CombineSchema(base, assertions, properties.toPropertyMap());
		}
	},
	XHTML10_STRICT("-//W3C//DTD XHTML 1.0 Strict//EN")
	{
		@Override
		public XMLReader createParser() throws ParserConfigurationException, SAXException
		{
			return createXmlParser();
		}

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
		public XMLReader createParser() throws ParserConfigurationException, SAXException
		{
			return createXmlParser();
		}

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
		public XMLReader createParser()
		{
			return createHtmlParser(DoctypeExpectation.HTML401_STRICT);
		}

		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML401_TRANSITIONAL("-//W3C//DTD HTML 4.01 Transitional//EN")
	{
		@Override
		public XMLReader createParser()
		{
			return createHtmlParser(DoctypeExpectation.HTML401_TRANSITIONAL);
		}

		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML40_STRICT("-//W3C//DTD HTML 4.0//EN")
	{
		@Override
		public XMLReader createParser()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Schema createSchema() throws IOException, SAXException, IncorrectSchemaException
		{
			return null;
		}
	},
	HTML40_TRANSITIONAL("-//W3C//DTD HTML 4.0 Transitional//EN")
	{
		@Override
		public XMLReader createParser()
		{
			// TODO Auto-generated method stub
			return null;
		}

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

	public abstract XMLReader createParser() throws ParserConfigurationException, SAXException;

	public abstract Schema createSchema() throws IOException, SAXException,
			IncorrectSchemaException;

	public String getIdentifier()
	{
		return identifier;
	}

	private static HtmlParser createHtmlParser(DoctypeExpectation docTypeExpectation)
	{
		HtmlParser htmlParser = new HtmlParser();
		htmlParser.setCommentPolicy(XmlViolationPolicy.ALLOW);
		htmlParser.setContentNonXmlCharPolicy(XmlViolationPolicy.ALLOW);
		htmlParser.setContentSpacePolicy(XmlViolationPolicy.ALTER_INFOSET);
		htmlParser.setNamePolicy(XmlViolationPolicy.ALLOW);
		htmlParser.setStreamabilityViolationPolicy(XmlViolationPolicy.FATAL);
		htmlParser.setXmlnsPolicy(XmlViolationPolicy.ALTER_INFOSET);
		htmlParser.setMappingLangToXmlLang(true);
		htmlParser.setHtml4ModeCompatibleWithXhtml1Schemata(true);
		htmlParser.setHeuristics(Heuristics.ALL);
		htmlParser.setDoctypeExpectation(docTypeExpectation);
		return htmlParser;
	}

	private static XMLReader createXmlParser() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		reader.setFeature("http://xml.org/sax/features/string-interning", true);
		reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		return reader;
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
