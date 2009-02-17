/**
 * Copyright iTender & Hosted Information Systems 2006
 * @author Paul Wilton & Paul Tuckey
 * @author $Author: $
 * @version $Rev: $ $Date: $
 */
package org.tuckey.web.filters.validation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ResponseWrapper
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private StreamWrapper os = new StreamWrapper();

    private int status = HttpServletResponse.SC_OK;

    /**
     * Main constructor that calls super(response) then creates an output object.
     *
     * @param response        the response to fallback to when content is not xhtml
     * @param dtdCheckEnabled enable check for xhtml dtd
     */
    public ResponseWrapper(HttpServletResponse response, boolean dtdCheckEnabled) {
        super(response);
        os.setResponse(response);
        os.setCheckForDtdDefnEnabled(dtdCheckEnabled);
    }

    /**
     * Turns the outputStream into a string.
     *
     * @return outputStream into a string
     * @throws java.io.IOException if stream is broken
     */
    public String outputStreamToString() throws IOException {
        pw.flush();
        return os.outputStreamToString();
    }

    PrintWriter pw = new PrintWriter(os);

    public PrintWriter getWriter() {
        return pw;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return os;
    }

    public void setContentLength(int i) {
        // suppress content length as we might be writing out more
    }

    /**
     * Overridden method that does nothing to prevent buffering.
     */
    public void flushBuffer() throws IOException {
        pw.flush();
        // do nothing else
    }

    /**
     * Overridden method that does nothing to prevent buffering.
     */
    public void resetBuffer() {
        // do nothing
    }

    /**
     * Check for not modified so that users of this class can use it.
     */
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.status = sc;
    }

    public int getStatus() {
        return status;
    }

    public boolean isContentXHTML() throws IOException {
        pw.flush();
        os.checkForDocType();
        return os.isContentXHTML();
    }

    /**
     * A class to get servlet output into a single string.
     *
     * @author Paul Tuckey
     * @version $Revision: 1.3 $ $Date: 2005/02/20 22:03:40 $
     */
    class StreamWrapper extends ServletOutputStream {

        //private CharArrayWriter charArrayWriter = new CharArrayWriter();
        private ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        private boolean checkForDtdDefnDone = false;
        private boolean checkForDtdDefnEnabled = false;
        private boolean contentXHTML = false;

        private HttpServletResponse response;
        private ServletOutputStream servletOutputStream;

        public StreamWrapper() {
            super();
        }

        public void close() throws IOException {
            // no need to do anything
            checkForDocType();
        }

        public void flush() throws IOException {
            if (servletOutputStream != null) {
                servletOutputStream.flush();
                return;
            }
            byteArrayOS.flush();
            checkForDocType();
        }

        public void write(byte[] b) throws IOException {
            if (servletOutputStream != null) {
                servletOutputStream.write(b);
                return;
            }
            byteArrayOS.write(b);
            checkForDocType();
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if (servletOutputStream != null) {
                servletOutputStream.write(b, off, len);
                return;
            }
            byteArrayOS.write(b, off, len);
            checkForDocType();
        }

        public synchronized void write(int b) throws IOException {
            if (servletOutputStream != null) {
                servletOutputStream.write(b);
                return;
            }
            byteArrayOS.write(b);
            checkForDocType();
        }

        public String outputStreamToString() throws IOException {
            if (servletOutputStream != null) {
                throw new IOException("servlet output stream in use this should not be called");
            }
            flush();
            return byteArrayOS.toString();
        }

        private void checkForDocType() throws IOException {
            if (!checkForDtdDefnEnabled) return;
            if (checkForDtdDefnDone) return;
            if (byteArrayOS.size() < 128) return;
            checkForDtdDefnDone = true;
            String contentSoFar = byteArrayOS.toString();
            //log.trace("isContentXHTML called on " + contentSoFar);
            if (contentSoFar.indexOf("<!DOCTYPE") != -1 &&
                    contentSoFar.indexOf("-//W3C//DTD XHTML") != -1) {
                contentXHTML = true;

            } else {
                contentXHTML = false;
                // change to write directly to buffer now
                servletOutputStream = response.getOutputStream();
                servletOutputStream.write(byteArrayOS.toByteArray());
                byteArrayOS = new ByteArrayOutputStream();
            }
        }

        public void setCheckForDtdDefnEnabled(boolean checkForDtdDefnEnabled) {
            this.checkForDtdDefnEnabled = checkForDtdDefnEnabled;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }


        public boolean isContentXHTML() {
            return contentXHTML;
        }

    }


}


