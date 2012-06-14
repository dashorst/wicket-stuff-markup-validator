package org.iso_relax.jaxp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.iso_relax.verifier.Verifier;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Wrapper SAXParser with validation through JARV API. For the present, SAX1
 * features are not supported.
 * 
 * @author Daisuke OKAJIMA
 */
class ValidatingSAXParser extends SAXParser
{
    protected SAXParser _WrappedParser;
    protected Verifier _Verifier;

    /**
     * creates a new instance with an internal SAXParser and Schema.
     * @param wrapped internal SAXParser
     * @param schema  compiled schema. 
     */
    public ValidatingSAXParser(SAXParser wrapped, Verifier verifier)
    {
        _WrappedParser = wrapped;
        _Verifier = verifier;
    }

    /**
     * unsupported
     */
    public Parser getParser()
    {
        throw new UnsupportedOperationException("getParser() method is not supported. Use getXMLReader().");
    }

    /**
     * returns a new XMLReader for parsing and validating the input
     */
    public XMLReader getXMLReader() throws SAXException
    {
        XMLFilter filter = _Verifier.getVerifierFilter();
        filter.setParent(_WrappedParser.getXMLReader());
        return filter;
    }

    /**
     * @see SAXParser#isNamespaceAware()
     */
    public boolean isNamespaceAware()
    {
        return _WrappedParser.isNamespaceAware();
    }

    /**
     * returns true always
     */
    public boolean isValidating()
    {
        return true;
    }

    /**
     * @see SAXParser#setProperty(String, Object)
     */
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        _WrappedParser.setProperty(name, value);
    }

    /**
     * @see SAXParser#getProperty(String)
     */
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        return _WrappedParser.getProperty(name);
    }

    //SAX1 features are not supported
    public void parse(File f, HandlerBase hb) { throw new UnsupportedOperationException("SAX1 features are not supported"); }
    public void parse(InputSource is, HandlerBase hb) { throw new UnsupportedOperationException("SAX1 features are not supported"); }
    public void parse(InputStream is, HandlerBase hb) { throw new UnsupportedOperationException("SAX1 features are not supported"); }
    public void parse(InputStream is, HandlerBase hb, String systemId) { throw new UnsupportedOperationException("SAX1 features are not supported"); }
    public void parse(String uri, HandlerBase hb) { throw new UnsupportedOperationException("SAX1 features are not supported"); }

    /**
     * parses and validates the given File using the given DefaultHandler
     */
    public void parse(File f, DefaultHandler dh) throws SAXException, IOException
    {
        XMLReader reader = getXMLReader();
        InputSource source = new InputSource(new FileInputStream(f));
        reader.setContentHandler(dh);
        reader.parse(source);
    }
    /**
     * parses and validates the given InputSource using the given DefaultHandler
     */
    public void parse(InputSource source, DefaultHandler dh) throws SAXException, IOException
    {
        XMLReader reader = getXMLReader();
        reader.setContentHandler(dh);
        reader.parse(source);
    }
    /**
     * parses and validates the given InputSource using the given DefaultHandler
     */
    public void parse(InputStream is, DefaultHandler dh) throws SAXException, IOException
    {
        XMLReader reader = getXMLReader();
        InputSource source = new InputSource(is);
        reader.setContentHandler(dh);
        reader.parse(source);
    }
    /**
     * parses and validates the given InputSream using the given DefaultHandler and systemId
     */
    public void parse(InputStream is, DefaultHandler dh, String systemId) throws SAXException, IOException
    {
        XMLReader reader = getXMLReader();
        InputSource source = new InputSource(is);
        source.setSystemId(systemId);
        reader.setContentHandler(dh);
        reader.parse(source);
    }
    /**
     * parses and validates the given uri using the given DefaultHandler
     */
    public void parse(String uri, DefaultHandler dh) throws SAXException, IOException
    {
        XMLReader reader = getXMLReader();
        InputSource source = new InputSource(uri);
        reader.setContentHandler(dh);
        reader.parse(source);
    }
    
}
