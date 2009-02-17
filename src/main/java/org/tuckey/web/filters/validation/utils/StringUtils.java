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

/**
 * Utilities for string handling in the style of Commons Lang.
 */
public final class StringUtils {

    public static boolean isBlank(String str) {
        if (str == null) return true;
        str = trimToNull(str);
        return str == null;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }


    public static String trimToNull(String str) {
        str = trim(str);
        return str == null || str.length() == 0 ? null : str;
    }


    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }

        int outputLength = inputLength * repeat;

        StringBuffer buf = new StringBuffer(outputLength);
        for (int i = 0; i < repeat; i++) {
            buf.append(str);
        }
        return buf.toString();
    }

}

