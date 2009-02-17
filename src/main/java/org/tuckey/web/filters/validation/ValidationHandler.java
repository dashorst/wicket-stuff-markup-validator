/**
 * Copyright (c) 2005, Paul Tuckey
 * All rights reserved.
 * ====================================================================
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * ====================================================================
 */
package org.tuckey.web.filters.validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.validation.generated.GeneratedValidationReport;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Paul Tuckey
 * @version $Revision: 9 $ $Date: 2006-09-20 20:36:12 +1200 (Wed, 20 Sep 2006) $
 */
public class ValidationHandler extends DefaultHandler {

    private static Logger log = LoggerFactory.getLogger(ValidationEntityResolver.class);

    private static final String XHTML_BASIC_IGNORE_ERROR = "A colon is not allowed in the name 'IS10744:arch' when namespaces are enabled";

    /**
     * the raw source of the document.
     */
    private String source;

    private DocumentBuilder parser;

    /**
     * the thing that resolves dtd's and other xml entities.
     */
    private final ValidationEntityResolver er;

    /**
     * the list of errors.
     */
    private List errors = new ArrayList();
    private final String url;

    /**
     * Create a handler using the default xml parser.
     */
    public ValidationHandler(String source, String url)
            throws FileNotFoundException {
        this(source, url, DocumentBuilderFactory.newInstance());
    }

    /**
     * Create a new Handler using a custom xml parser.
     */
    public ValidationHandler(String source, String url, String xmlParser)
            throws FileNotFoundException, BadXmlParserException {
        this(source, url, getCustomParserInstance(xmlParser));
    }

    /**
     * This will setup ready for parsing, call parse() to actually parse the document.
     *
     * @param source
     * @throws FileNotFoundException
     */
    private ValidationHandler(String source, String url, DocumentBuilderFactory factory)
            throws FileNotFoundException {
        this.url = url;
        this.source = source;
        this.er = new ValidationEntityResolver();

        //System.getProperties().setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");

        log.debug("setting up parser");
        log.debug("factory: " + factory.getClass().getName());
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        try {
            this.parser = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("unable to setup parser", e);
            return;
        }
        log.debug("parser: " + this.parser.getClass().getName());

        this.parser.setErrorHandler(this);
        this.parser.setEntityResolver(er);
    }

    /**
     * DOM parse the document.
     */
    public void parse() throws IOException {
        if (parser == null) return;

        log.debug("parsing");
        try {
            InputSource inputSource = new InputSource(new StringReader(source));
            parser.parse(inputSource);

        } catch (org.xml.sax.SAXParseException spe) {
            addError(new LineIssue(LineIssue.TYPE_FATAL_ERROR, spe));

        } catch (org.xml.sax.SAXException se) {
            if (se.getException() != null) {
                se.getException().printStackTrace(System.err);
            } else {
                se.printStackTrace(System.err);
            }
        }

    }

    //
    // ErrorHandler methods
    //

    public void warning(SAXParseException ex) {
        addError(new LineIssue(LineIssue.TYPE_WARNING, ex));
    }

    public void error(SAXParseException ex) {
        addError(new LineIssue(LineIssue.TYPE_ERROR, ex));
        log.debug("error: " + ex.getMessage());
    }

    public void fatalError(SAXParseException ex) throws SAXException {
        addError(new LineIssue(LineIssue.TYPE_FATAL_ERROR, ex));
    }


    public boolean isValid() {
        return errors.isEmpty();
    }

    private void addError(LineIssue lineIssue) {
        if (errors.contains(lineIssue)) return;
        if (isFalseError(lineIssue)) return;
        for (int i = 0; i < errors.size(); i++) {
            LineIssue issue = (LineIssue) errors.get(i);
            // already in List
            if (issue.equals(lineIssue)) return;
        }
        // skip certain errors
        String msg = lineIssue.getMessage();
        if (msg != null) {
            if (msg.startsWith("Using original entity definition for") &&
                    LineIssue.TYPE_WARNING.equals(lineIssue.getType())) {
                log.debug("this warning comes from crimson and it's not important: " + lineIssue.getMessage());
                return;
            }
        }
        errors.add(lineIssue);

    }

    private static boolean isFalseError(LineIssue lineIssue) {
        if (lineIssue == null) return false;
        String message = lineIssue.getMessage();
        if (message == null) return false;
        /**
         * There appears to be a problem with the syntax of "xhtml-arch-1.mod" from the official W3C site,
         * so this is correct behavior on the part of Xerces. They know it's busted
         * http://hades.mn.aptest.com/cgi-bin/voyager-issues/Modularization-DTDs?id=468;page=3;user=guest
         */
        return message.indexOf(XHTML_BASIC_IGNORE_ERROR) != -1;
    }

    public final List getErrors() {
        return errors;
    }

    public String[] getSourceAsArrayOfLines() {
        return source.split("\n");
    }

    public ValidationDoctype getDoctype() {
        return er.getDoctype();
    }

    public DocumentBuilder getParser() {
        return parser;
    }

    /**
     * Helper method to get a custom parser.
     */
    public static DocumentBuilderFactory getCustomParserInstance(String xmlParser)
            throws BadXmlParserException {
        DocumentBuilderFactory factory;
        Class c;
        try {
            c = Class.forName(xmlParser);
            Object o = c.newInstance();
            factory = (DocumentBuilderFactory) o;
        } catch (ClassNotFoundException e) {
            throw new BadXmlParserException(e);
        } catch (IllegalAccessException e) {
            throw new BadXmlParserException(e);
        } catch (InstantiationException e) {
            throw new BadXmlParserException(e);
        }
        return factory;
    }

}
