package org.iso_relax.jaxp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Wraps another {@link SAXParserFactory} and adds validation capability.
 * 
 * @author Daisuke OKAJIMA
 */
public class ValidatingSAXParserFactory extends SAXParserFactory
{
    protected SAXParserFactory _WrappedFactory;
    protected Schema _Schema;

    private boolean validation = true;

    /**
     * creates a new instance that wraps the default DocumentBuilderFactory
     * @param schema the compiled Schema object. It can not be null.
     */
    public ValidatingSAXParserFactory(Schema schema)
    {
        this(SAXParserFactory.newInstance(), schema);
    }    
    
    /**
     * creates a new instance with an internal SAXParserFactory and Schema.
     * @param wrapped internal SAXParser
     * @param schema  compiled schema. 
     */
    public ValidatingSAXParserFactory(SAXParserFactory wrapped, Schema schema)
    {
        _WrappedFactory = wrapped;
        _Schema = schema;
    }

    /**
     * returns a new SAX parser.
     * If setValidating(false) is called previously, this method simply
     * returns the implementation of wrapped SAXParser.
     */
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException
    {
        if(isValidating()) {
            try {
                  return new ValidatingSAXParser(
                      _WrappedFactory.newSAXParser(),
                      _Schema.newVerifier());
             } catch(VerifierConfigurationException ex) {
                 throw new ParserConfigurationException(ex.getMessage());
             }
        } else
            return _WrappedFactory.newSAXParser();
    }

    /**
     * @see SAXParserFactory#setFeature(String, boolean)
     */
    public void setFeature(String name, boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
    {
        _WrappedFactory.setFeature(name, value);
    }

    /**
     * @see SAXParserFactory#getFeature(String)
     */
    public boolean getFeature(String name) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
    {
        return _WrappedFactory.getFeature(name);
    }

    public boolean isNamespaceAware()
    { return _WrappedFactory.isNamespaceAware(); }
    public void setNamespaceAware(boolean awareness)
    { _WrappedFactory.setNamespaceAware(awareness); }
    
    public boolean isValidating()
    { return validation; }
    public void setValidating(boolean validating)
    { validation = validating; }

}
