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

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Wraps ServletOutputStream so that we can collect the output into a string.
 *
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-07-12 08:07:49 +1200 (Wed, 12 Jul 2006) $
 */
public class ServletStreamCollector extends ServletOutputStream {

    private static final int BUFFER_SIZE = 4096;

    private OutputStream outputStream;
    private boolean checkForDocTypeDone = false;
    ResponseCollector rc;

    protected ServletStreamCollector(ResponseCollector rc) {
        outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
        this.rc = rc;
    }

    public String toString() {
        checkForDocType();
        return outputStream.toString();
    }

    public void write(int b) throws IOException {
        outputStream.write(b);
        checkForDocType();
    }

    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        checkForDocType();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        checkForDocType();
    }

    public byte[] getBytes() {
        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    private void checkForDocType() {
        if ( ! rc.isCheckForDtdDefnEnabled() ) return;
        if ( outputStream instanceof ServletOutputStream ) return;
        if (((ByteArrayOutputStream)outputStream).size() < 128) return;
        if (checkForDocTypeDone) return;
        checkForDocTypeDone = true;
        String contentSoFar = outputStream.toString();
        if ( ! rc.isContentXHTML(contentSoFar) ) {
            // change to write directly to buffer now
            try {
                ServletOutputStream sos = rc.getResponseOutputStream();
                sos.write(((ByteArrayOutputStream)outputStream).toByteArray());
                outputStream = sos;
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}
