package org.wicketstuff.htmlvalidator.tester;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.wicketstuff.htmlvalidator.HtmlValidationResponseFilter;

public class ValidatorApplication extends WebApplication {
	private HtmlValidationResponseFilter validatingFilter = new HtmlValidationResponseFilter();

	@Override
	protected void init() {
		super.init();

		getRequestCycleSettings().addResponseFilter(validatingFilter);
	}

	public HtmlValidationResponseFilter getValidatingFilter() {
		return validatingFilter;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return ScriptId.class;
	}
}
