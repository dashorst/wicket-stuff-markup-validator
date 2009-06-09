package org.wicketstuff.htmlvalidator;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.wicket.IResponseFilter;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.tuckey.web.filters.validation.LineIssue;
import org.tuckey.web.filters.validation.ValidationHandler;
import org.tuckey.web.filters.validation.generated.GeneratedValidationReport;

public class HtmlValidationResponseFilter implements IResponseFilter {
	
	private boolean ignoreKnownWicketBugs;
	
	private boolean ignoreAutocomplete;
	
	public AppendingStringBuffer filter(AppendingStringBuffer responseBuffer) {
		Page responsePage = RequestCycle.get().getResponsePage();
		
		// when the responsepage is an error page, don't filter the page
		if (responsePage.isErrorPage()) {
			return responseBuffer;
		}

		String response = responseBuffer.toString();

		if (isXHtml(responseBuffer)) {
			try {
				ValidationHandler handler = new ValidationHandler(response, "") {
					@Override
					protected boolean isFalseError(LineIssue lineIssue) {
						if (isIgnoreKnownWicketBugs() && isKnownWicketBug(lineIssue))
							return true;
						if (isIgnoreAutocomplete() && isAutocompleteError(lineIssue))
							return true;
						
						return super.isFalseError(lineIssue);
					}
				};
				handler.parse();
				if (!handler.isValid()) {
					insertMarkup(responseBuffer, handler);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return responseBuffer;
	}

	public void setIgnoreAutocomplete(boolean ignoreAutocomplete) {
		this.ignoreAutocomplete = ignoreAutocomplete;
	}

	public boolean isIgnoreAutocomplete() {
		return ignoreAutocomplete;
	}
	
	public void setIgnoreKnownWicketBugs(boolean ignoreKnownWicketBugs) {
		this.ignoreKnownWicketBugs = ignoreKnownWicketBugs;
	}

	public boolean isIgnoreKnownWicketBugs() {
		return ignoreKnownWicketBugs;
	}
	
	private boolean isAutocompleteError(LineIssue lineIssue) {
		return "Attribute &quot;autocomplete&quot; must be declared for element type &quot;input&quot;."
				.equals(lineIssue.getMessage());
	}
	
	private boolean isKnownWicketBug(LineIssue lineIssue) {
		return isWicket2033(lineIssue) || isWicket2316(lineIssue);
	}
	
	private boolean isWicket2033(LineIssue lineIssue) {
		return "The reference to entity &quot;wicket:ignoreIfNotActive&quot; must end with the ';' delimiter."
				.equals(lineIssue.getMessage());
	}
	
	private boolean isWicket2316(LineIssue lineIssue) {
		return "The entity name must immediately follow the '&amp;' in the entity reference."
				.equals(lineIssue.getMessage());
	}
	
	private boolean isXHtml(AppendingStringBuffer response) {
		int maxLength = Math.min(response.length(), 128);
		String contentSoFar = response.substring(0, maxLength);
		return contentSoFar.indexOf("<!DOCTYPE") != -1
				&& contentSoFar.indexOf("-//W3C//DTD XHTML") != -1;
	}

	private void insertMarkup(AppendingStringBuffer original,
			ValidationHandler handler) {

		GeneratedValidationReport report = new GeneratedValidationReport();
		report.validationHandler = handler;

		String head = report.getHeadMarkup();
		String body = report.getBodyMarkup();

		int indexOfHeadClose = original.lastIndexOf("</head>");
		original.insert(indexOfHeadClose, head);

		int indexOfBodyClose = original.lastIndexOf("</body>");
		original.insert(indexOfBodyClose, body);
	}
}
