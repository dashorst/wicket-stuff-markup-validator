package org.wicketstuff.htmlvalidator.examples;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.htmlvalidator.HtmlValidationResponseFilter;

public class HtmlValidatorApplication extends WebApplication {
	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
//		getMarkupSettings().setStripXmlDeclarationFromOutput(false);
		
		getRequestCycleSettings().addResponseFilter(new HtmlValidationResponseFilter());
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return ValidXHtmlPage.class;
	}
}
