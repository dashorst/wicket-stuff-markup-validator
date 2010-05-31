package org.wicketstuff.htmlvalidator;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class Html5EntityResolver implements EntityResolver
{
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
	{
		int indexOfValidator = systemId.lastIndexOf("htmlvalidator");
		if (indexOfValidator != -1)
			systemId = systemId.substring(indexOfValidator + 14);
		return new InputSource(HtmlValidationResponseFilter.class.getResourceAsStream("/relaxng/"
			+ systemId));
	}
}