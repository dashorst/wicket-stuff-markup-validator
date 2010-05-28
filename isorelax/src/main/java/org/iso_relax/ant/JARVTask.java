package org.iso_relax.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFactory;
import org.xml.sax.SAXException;

/**
 * Ant Task that performs XML validation through JARV.
 * 
 * @author Kohsuke Kawaguchi (kk@kohsuke.org)
 */
public class JARVTask extends Task {
    /** Schema file. */
    private File schemaFile;
    
    /** Schema language. */
    private String schemaLanguage;
    
    /** XML file that will be validated. */
    private File xmlFile;
    
    /** FileSets to be validated. */
    private List xmlFiles = new ArrayList();
    
    /**
     * Handles the schema attribute.
     */
    public void setSchema(String schemaFilename) {
      schemaFile = project.resolveFile(schemaFilename);
    }
    
    /**
     * Handles the schemaLanguage attribute.
     */
    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }
    
    /**
     * Handles the file attribute.
     */
    public void setFile(File file) {
      this.xmlFile = file;
    }
    
    /**
     * Handles the nested file set.
     */
    public void addFileset( FileSet fs ) {
        xmlFiles.add(fs);
    }

    public void execute() throws BuildException {
        try {
            if( schemaLanguage==null )
                throw new BuildException(
                    "schema language needs to be specified through the schemaLanguage attribute",
                    location );
            
            if( schemaFile==null )
                throw new BuildException(
                    "schema file needs to be specified through the schema attribute",
                    location );
             
            // load verifier factory       
            VerifierFactory factory;
            try {
                factory = VerifierFactory.newInstance(schemaLanguage,this.getClass().getClassLoader());
            } catch( VerifierConfigurationException e ) {
                throw new BuildException(
                    "unable to load a validator for the schema language: "+schemaLanguage,
                    e, location );
            }
            
            // create a verifier by compiling a schema
            Verifier verifier;
            ErrorHandlerImpl errorHandler = new ErrorHandlerImpl(this);
            try {
                verifier = factory.newVerifier(schemaFile);
                verifier.setErrorHandler(errorHandler);
            } catch( VerifierConfigurationException e ) {
                throw new BuildException(
                    "failed to compile the schema:"+ e.getMessage(),
                    e, location );
            }
            
            boolean noError = true;
            
            // do the validation
            if( xmlFile!=null ) {
                log( "validating "+xmlFile, Project.MSG_INFO );
                noError |= verifier.verify( xmlFile );
            }
            
            for( int i=0; i<xmlFiles.size(); i++ ) {
                FileSet fs = (FileSet)xmlFiles.get(i);
                DirectoryScanner ds = fs.getDirectoryScanner(project);
                File dir = fs.getDir(project);
                String[] src = ds.getIncludedFiles();
                for( int j=0; j<src.length; j++ ) {
                    File f = new File(dir,src[j]);
                    log( "validating "+f, Project.MSG_INFO );
                    noError |= verifier.verify( f );
                }
            }
            
            if(errorHandler.hadError)
                noError = false;    // cover sloppy implementation
            
            if( !noError )
                throw new BuildException(
                    "validation error. the error message should have been provided");
        } catch( SAXException e ) {
            throw new BuildException( e, location );
        } catch( IOException e ) {
            throw new BuildException( e, location );
        }
    }
}
