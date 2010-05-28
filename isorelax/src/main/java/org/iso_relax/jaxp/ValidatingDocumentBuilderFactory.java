package org.iso_relax.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.VerifierConfigurationException;

/**
 * Wraps another {@link DocumentBuilderFactory} and adds validation capability.
 * 
 * @author Daisuke OKAJIMA
 */
public class ValidatingDocumentBuilderFactory extends DocumentBuilderFactory
{
    protected Schema _Schema;
    protected DocumentBuilderFactory _WrappedFactory;
    
    private boolean validation = true;

    
    /**
     * creates a new instance that wraps the default DocumentBuilderFactory
     * @param schema the compiled Schema object. It can not be null.
     */
    public ValidatingDocumentBuilderFactory(Schema schema)
    {
        this(DocumentBuilderFactory.newInstance(), schema);
    }    
    
    /**
     * creates a new instance with an internal DocumentBuilderFactory and Schema.
     * @param wrapped internal DocumentBuilderFactory
     * @param schema  compiled schema. 
     */
    public ValidatingDocumentBuilderFactory(DocumentBuilderFactory wrapped, Schema schema)
    {
        _WrappedFactory = wrapped;
        _Schema = schema;
    }

    /**
     * returns a new DOM parser.
     * If setValidating(false) is called previously, this method
     * simply returns the implementation of wrapped DocumentBuilder.
     */
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException
    {
        if(isValidating()) {
            try {
                return new ValidatingDocumentBuilder(
                    _WrappedFactory.newDocumentBuilder(),
                    _Schema.newVerifier());
            } catch(VerifierConfigurationException ex) {
                throw new ParserConfigurationException(ex.getMessage());
            }
        } else
            //if validation is disabled, we simply return the implementation of wrapped DocumentBuilder
            return _WrappedFactory.newDocumentBuilder();
    }

    /**
     * @see DocumentBuilderFactory#setAttribute(String, Object)
     */
    public void setAttribute(String name, Object value)
    {
        _WrappedFactory.setAttribute(name, value);
    }

    /**
     * @see DocumentBuilderFactory#getAttribute(String)
     */
    public Object getAttribute(String name)
    {
        return _WrappedFactory.getAttribute(name);
    }

    public boolean isValidating()
    { return validation; }
    public void setValidating(boolean _validating)
    { this.validation = _validating; }
    
    public boolean isCoalescing()
    { return _WrappedFactory.isCoalescing(); }
    public boolean isExpandEntityReference()
    { return _WrappedFactory.isExpandEntityReferences(); }
    public boolean isIgnoringComments()
    { return _WrappedFactory.isIgnoringComments(); }
    public boolean isIgnoringElementContentWhitespace()
    { return _WrappedFactory.isIgnoringElementContentWhitespace(); }
    public boolean isNamespaceAware()
    { return _WrappedFactory.isNamespaceAware(); }
    public void setCoalescing(boolean coalescing)
    { _WrappedFactory.setCoalescing(coalescing); }
    public void setExpandEntityReference(boolean expandEntityRef)
    { _WrappedFactory.setExpandEntityReferences(expandEntityRef); }
    public void setIgnoringComments(boolean ignoreComments)
    { _WrappedFactory.setIgnoringComments(ignoreComments); }
    public void setIgnoringElementContentWhitespace(boolean whitespace)
    { _WrappedFactory.setIgnoringElementContentWhitespace(whitespace); }
    public void setNamespaceAware(boolean awareness)
    { _WrappedFactory.setNamespaceAware(awareness); }
}
