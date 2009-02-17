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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.validation.utils.StringUtils;
import org.tuckey.web.filters.validation.utils.WildcardHelper;

/**
 *
 *
 */
public class ServerNameMatcher {

    private static Logger log = LoggerFactory.getLogger(ServerNameMatcher.class);

    private List patterns = new ArrayList();
    WildcardHelper wh = new WildcardHelper();

    public ServerNameMatcher(String options) {
        String[] enableOnHostsArr = options.split(",");
        for (int i = 0; i < enableOnHostsArr.length; i++) {
            String s = enableOnHostsArr[i];
            if (StringUtils.isBlank(s)) continue;
            String rawPattern = StringUtils.trim(enableOnHostsArr[i]).toLowerCase();
            int[] compiledPattern = wh.compilePattern(rawPattern);
            patterns.add(compiledPattern);
        }
    }

    public boolean isMatch(String serverName) {
        log.debug("looking for hostname match on current server name " + serverName);
        if (patterns == null || StringUtils.isBlank(serverName)) {
            return false;
        }
        serverName = StringUtils.trim(serverName).toLowerCase();
        for (int i = 0; i < patterns.size(); i++) {
            int[] compiledPattern = (int[]) patterns.get(i);
            if (wh.matchWithNoResults(serverName, compiledPattern)) return true;
        }
        return false;
    }

}
