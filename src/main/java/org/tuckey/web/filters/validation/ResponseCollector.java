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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A response wrapper to collect the response html into a string.
 * <p/>
 * Note we start collecting by default then stop if we decide we don't like it.
 *
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-07-12 08:07:49 +1200 (Wed, 12 Jul 2006) $
 */
public class ResponseCollector extends HttpServletResponseWrapper {

    private static Logger log = LoggerFactory.getLogger(ResponseCollector.class);

    private CharArrayWriter writer;
    private boolean checkForDtdDefnDone = false;
    private boolean checkForDtdDefnEnabled = false;
    private ServletStreamCollector os;
    private boolean contentXHTML = false;
    private HttpServletResponse response;

    public ResponseCollector(HttpServletResponse response, boolean checkForDtdDefnEnabled) {
        super(response);
        this.response = response;
        writer = new CharArrayWriter(4096);
        os = new ServletStreamCollector(this);
        this.checkForDtdDefnEnabled = checkForDtdDefnEnabled;
    }

    public String toString() {
        log.debug("toString Called");

        if (!checkForDtdDefnDone && checkForDtdDefnEnabled ) {
            // we haven't check to see if the doc is xhtml yet
            if (writer.size() > 0) {
                if (writer.size() > 128) {
                    log.debug("checking output for xhtml header");
                    isContentXHTML(writer.toString());
                } else {
                    log.debug("content probably not xhtml as not even long enough for dtd defn");
                    // todo: collecting = false;
                }
                return writer.toString();

            } else {
                log.debug("os has has itself checked via ServletStreamCollector");
            }
        }
        if (writer.size() > 0) {
            return writer.toString();
        }
        log.debug("returning os");
        return os.toString();
    }

    public PrintWriter getWriter() throws IOException {
        log.debug("getWriter Called");
        return new PrintWriter(writer);
    }

    public CharArrayWriter getCharArrayWriter() throws IOException {
        log.debug("getCharArrayWriter Called");
        return writer;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        log.debug("getOutputStream Called");
        return os;
    }

    public ServletOutputStream getResponseOutputStream() throws IOException {
        log.debug("getResponseOutputStream Called");
        return response.getOutputStream();
    }

    /**
     * Overridden method to prevent buffering of response.
     */
    public void resetBuffer() {
        log.debug("resetBuffer Called");
        // do nothing
    }

    /**
     * Overridden method to prevent buffering of response.
     */
    public void flushBuffer() throws IOException {
        log.debug("flushBuffer Called");
        // do nothing
    }

    public boolean isContentXHTML() {
        return contentXHTML;
    }

    boolean isContentXHTML(String contentSoFar) {
        //log.trace("isContentXHTML called on " + contentSoFar);
        checkForDtdDefnDone = true;
        if (contentSoFar.indexOf("<!DOCTYPE") != -1 &&
                contentSoFar.indexOf("-//W3C//DTD XHTML") != -1) {
            // we have an xhtml doc continue getting content
            contentXHTML = true;
            return true;
        } else {
            // must be something else convert to std output mode
            contentXHTML = false;
        }
        return false;
    }

    public boolean isCheckForDtdDefnEnabled() {
        return checkForDtdDefnEnabled;
    }

    public ServletStreamCollector getServletStreamCollector() {
        return os;
    }
}
