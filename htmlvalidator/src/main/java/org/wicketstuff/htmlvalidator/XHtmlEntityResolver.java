package org.wicketstuff.htmlvalidator;

import java.io.File;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class XHtmlEntityResolver implements EntityResolver {
	@Override
	public InputSource resolveEntity(String publicId, String systemId) {
		String path = "file:" + new File("").getAbsolutePath() + "/";
		if (systemId.startsWith(path)) {
			systemId = systemId.substring(path.length());
		}
		int indexOfValidator = systemId.lastIndexOf("htmlvalidator");
		if (indexOfValidator != -1)
			systemId = systemId.substring(indexOfValidator + 14);
		return new InputSource(HtmlValidationResponseFilter.class
				.getResourceAsStream("/schemas/xhtml10/" + systemId));
	}
}