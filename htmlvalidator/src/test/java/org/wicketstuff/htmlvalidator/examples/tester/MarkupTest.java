package org.wicketstuff.htmlvalidator.examples.tester;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wicketstuff.htmlvalidator.HtmlValidationResultKey;
import org.wicketstuff.htmlvalidator.ValidationReport;

public class MarkupTest {
	private WicketTester tester;

	@Before
	public void setup() {
		tester = new WicketTester(new ValidatorApplication());
	}

	@Test
	public void scriptId() {
		tester.startPage(new ScriptId());
		assertValid();
	}

	private void assertValid() {
		Page page = tester.getLastRenderedPage();
		String validationResult = page.getMetaData(HtmlValidationResultKey.KEY);
		Assert.assertEquals("Markup is valid XHTML10_STRICT",
				validationResult);
	}
}
