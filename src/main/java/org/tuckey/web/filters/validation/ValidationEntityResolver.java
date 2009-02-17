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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Paul Tuckey
 * @version $Revision: 1 $ $Date: 2006-07-12 08:07:49 +1200 (Wed, 12 Jul 2006) $
 */
public class ValidationEntityResolver implements EntityResolver {

    private static Logger log = LoggerFactory.getLogger(ValidationEntityResolver.class);

    private static Hashtable entities = new Hashtable();

    private ValidationDoctype doctype = null;

    /**
     * The root urls of the dtd files.
     */
    private static URL DTD_ROOT_URL = null;
    static {
        DTD_ROOT_URL = ValidationEntityResolver.class.getResource("/org/tuckey/web/filters/validation/dtds");
    }

    /**
     * Will setup the entity resolver, dtd's are stores in clas structure to save fetching over the web.
     *
     * @throws FileNotFoundException
     */
    public ValidationEntityResolver() throws FileNotFoundException {
        if (DTD_ROOT_URL != null) return;
        // try to initialise again just in case
        log.debug("/ is " + ValidationEntityResolver.class.getResource("/"));
        throw new FileNotFoundException("Could not find dtds folder " + DTD_ROOT_URL);
    }

    /**
     * Resolve the requested external entity.
     *
     * @param publicId The public identifier of the entity being referenced
     * @param systemId The system identifier of the entity being referenced
     * @throws org.xml.sax.SAXException if a parsing exception occurs
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {
        String entity = null;

        // in case this gets called more than once
        if (doctype == null) {
            doctype = new ValidationDoctype(publicId, systemId);
        }
        if (log.isDebugEnabled()) {
            log.debug("resolveEntity('" + publicId + "', '" + systemId + "')");
        }

        if ( publicId != null && publicId.startsWith("-//W3C//")) {
            if ( "-//W3C//ENTITIES Latin 1 for XHTML//EN".equals(publicId)) {
                systemId = DTD_ROOT_URL + "/www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent";
            }   else {
                systemId = convertFromFilePath(systemId);
            }
        }

        // grab from cache by public id
        if (publicId != null) {
            entity = (String) entities.get(publicId);
        }
        // try via systemid if that failed
        if (systemId != null && entity == null ) {
            entity = (String) entities.get(systemId);
        }

        // if we still didn't get anything from cache fetch from classes dir
        if (entity == null && systemId != null) {
            if (systemId.startsWith("http://")) {
                entity = convertToSystemPath(systemId);

            } else {
                // maybe we can actually use the systemId to load the dtd
                entity = systemId;
            }
            try {
                // check the entity URL is okay, then register it for next time.
                if (publicId != null) {
                    ValidationEntityResolver.register(publicId, entity);
                } else {
                    ValidationEntityResolver.register(systemId, entity);
                }
            } catch (MalformedURLException e) {
                log.error(e.toString(), e);
            }
        }

        if (entity == null) {
            if (log.isDebugEnabled()) {
                log.debug("Couldn't resolve DTD: " + publicId + ", " + systemId);
            }
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Resolving to DTD " + entity);
        }
        return new InputSource(entity);
    }

    static String convertToSystemPath(String systemId) {
        String entity;
        // strip http://
        String dtdPath = systemId.substring("http://".length());
        // find first / after host
        int pathIdx = dtdPath.indexOf("/");
        String dtdHost = dtdPath.substring(0, pathIdx);
        entity = dtdPath.substring(pathIdx);
        entity = dtdHost + entity;
        if ( !DTD_ROOT_URL.toString().endsWith("/")) entity = "/" + entity;
        return DTD_ROOT_URL + entity;
    }

    static String convertFromFilePath(String systemId) {
        String rootUrlStr = ValidationEntityResolver.class.getResource("/").toString();
        if ( !systemId.startsWith("file:/")) return systemId;
        int idx = systemId.indexOf(rootUrlStr);
        if ( idx != 0) return systemId;
        String entityUri = systemId.substring(rootUrlStr.length());
        if ( entityUri.startsWith("org/tuckey/") ) return systemId;
        if ( !DTD_ROOT_URL.toString().endsWith("/")) entityUri = "/" + entityUri;
        return DTD_ROOT_URL + entityUri;
    }


    /**
     * Cache the entity lookup.
     */
    public static void register(String publicId, String entityURL) throws MalformedURLException {
        if (log.isDebugEnabled()) {
            log.debug("register " + publicId + ", " + entityURL);
        }
        URL dtdURL = new URL(entityURL);
        if (log.isDebugEnabled()) {
            log.debug("url '" + dtdURL + "'");
        }
        entities.put(publicId, entityURL);
    }

    /**
     * the doctype.
     *
     * @return ValidationDoctype
     */
    public ValidationDoctype getDoctype() {
        return doctype;
    }

}
