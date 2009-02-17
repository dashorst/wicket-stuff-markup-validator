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

import org.xml.sax.SAXParseException;
import org.tuckey.web.filters.validation.utils.StringEscapeUtils;

/**
 * Defines an issue on a line.
 *
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-07-12 08:07:49 +1200 (Wed, 12 Jul 2006) $
 */
public final class LineIssue {

    public static final String TYPE_FATAL_ERROR = "fat";
    public static final String TYPE_ERROR = "err";
    public static final String TYPE_WARNING = "war";

    private final String type;
    private final int lineNumber;
    private final int columnNumber;
    private final String message;

    public LineIssue(String type, SAXParseException saxParseException) {
        this.type = type;
        this.lineNumber = saxParseException.getLineNumber();
        this.columnNumber = saxParseException.getColumnNumber();
        this.message = StringEscapeUtils.escapeHtml(saxParseException.getMessage());
    }

    public final String getType() {
        return type;
    }

    public final String getTypeTitle() {
        if (TYPE_ERROR.equals(type)) {
            return "Error";
        } else if (TYPE_FATAL_ERROR.equals(type)) {
            return "Fatal Error";
        } else if (TYPE_WARNING.equals(type)) {
            return "Warning";
        }
        return null;
    }

    public final int getLineNumber() {
        return lineNumber;
    }

    public final int getColumnNumber() {
        return columnNumber;
    }

    public final String getMessage() {
        return message;
    }

    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineIssue)) return false;

        final LineIssue lineIssue = (LineIssue) o;

        if (columnNumber != lineIssue.columnNumber) return false;
        if (lineNumber != lineIssue.lineNumber) return false;
        if (message != null ? !message.equals(lineIssue.message) : lineIssue.message != null) return false;
        return !(type != null ? !type.equals(lineIssue.type) : lineIssue.type != null);

    }

    public int hashCode() {
        int result;
        result = (type != null ? type.hashCode() : 0);
        result = 29 * result + lineNumber;
        result = 29 * result + columnNumber;
        result = 29 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

}
