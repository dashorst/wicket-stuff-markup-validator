package org.wicketstuff.htmlvalidator;

import org.apache.wicket.MetaDataKey;

public final class HtmlValidationResultKey extends MetaDataKey<String> {
	private static final long serialVersionUID = 1L;

	public static final HtmlValidationResultKey KEY = new HtmlValidationResultKey();

	private HtmlValidationResultKey() {
	}
}
