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


/**
 * A bean class to holding doctype info.
 *
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-07-12 08:07:49 +1200 (Wed, 12 Jul 2006) $
 */
public class ValidationDoctype {

    private String systemId;
    private String publicId;

    public ValidationDoctype(String publicId,
                             String systemId) {
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getPublicId() {
        return publicId;
    }


}
