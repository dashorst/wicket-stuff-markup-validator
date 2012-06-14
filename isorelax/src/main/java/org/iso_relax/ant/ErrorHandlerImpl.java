package org.iso_relax.ant;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Error handler implementation that reports errors through
 * the logging mechanism of Ant task.
 * 
 * @author Kohsuke Kawaguchi (kk@kohsuke.org)
 */
public class ErrorHandlerImpl implements ErrorHandler {

    private final Task task;
    
    boolean hadError = false;

    public ErrorHandlerImpl( Task t ) {
        this.task = t;
    }
    
    public void warning(SAXParseException e) throws SAXException {
        print( e, Project.MSG_WARN );
    }

    public void error(SAXParseException e) throws SAXException {
        print( e, Project.MSG_ERR );
        hadError = true;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        print( e, Project.MSG_ERR );
        hadError = true;
    }

    private void print( SAXParseException e, int msgLevel ) {
        task.log( e.getMessage(), msgLevel );
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        task.log( sw.toString(), Project.MSG_VERBOSE );
    }
}
