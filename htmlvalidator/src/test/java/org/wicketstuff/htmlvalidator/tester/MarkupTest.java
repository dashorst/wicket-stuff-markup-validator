package org.wicketstuff.htmlvalidator.tester;

import org.apache.wicket.Page;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wicketstuff.htmlvalidator.DocType;
import org.wicketstuff.htmlvalidator.HtmlValidationResultKey;

public class MarkupTest {
	private WicketTester tester;

	@Before
	public void setup() {
		tester = new WicketTester(new ValidatorApplication());
	}

	@Test
	public void scriptId() {
		tester.startPage(new ScriptId());
		assertValid(DocType.XHTML10_STRICT);
	}

	@Test
	public void inputFile() {
		tester.startPage(new InputTypes());
		assertValid(DocType.XHTML10_STRICT);
	}

	private void assertValid(DocType docType) {
		Page page = tester.getLastRenderedPage();
		String validationResult = page.getMetaData(HtmlValidationResultKey.KEY);
		Assert.assertEquals("Markup is valid " + docType, validationResult);
	}
}
