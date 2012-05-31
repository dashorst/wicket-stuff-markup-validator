package org.wicketstuff.htmlvalidator.examples;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public abstract class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public BasePage() {
		add(new Label("title", "Validator.Wicket - examples"));
		add(new MenuPanel("menu"));
	}
}
