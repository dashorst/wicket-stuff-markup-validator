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
package org.tuckey.web.filters.validation.utils;

import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * Helps you write to a CharArrayWriter adding some
 * basic formatting methods for outputting plain text
 */
public class HtmlWriter extends CharArrayWriter {


    public void writeln(String str) throws IOException {
        super.write("\n");
        super.write(str);
    }

    public void write(String str) throws IOException {
        super.write(str);
    }

    public void write(String[] strArr) throws IOException {
        for (int i = 0; i < strArr.length; i++) {
            String s = strArr[i];
            super.write(s);
            super.write("\n");
        }
    }

    public static String arrayToString(String[] strArr) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            String s = strArr[i];
            buff.append(s);
            buff.append("\n");
        }
        return buff.toString();
    }

}
