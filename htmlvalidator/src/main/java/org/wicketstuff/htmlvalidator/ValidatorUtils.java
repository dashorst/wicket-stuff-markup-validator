package org.wicketstuff.htmlvalidator;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;

class ValidatorUtils {
	static Tag cssFor(String filename) {
		Tag link = new Tag("link");

		link.attr("rel", "stylesheet");
		link.attr("type", "text/css");
		link.attr("href", urlFor(filename));

		return link;
	}

	static Tag jsFor(String filename) {
		Tag script = new Tag("script");
		script.attr("type", "text/javascript");
		script.attr("src", urlFor(filename));
		return script;
	}

	static Tag imgFor(String filename) {
		Tag img = new Tag("img");
		img.attr("src", urlFor(filename));
		return img;
	}

	private static String urlFor(String filename) {
		RequestCycle requestCycle = RequestCycle.get();
		PackageResourceReference reference = new PackageResourceReference(
				ValidationReport.class, filename);
		CharSequence url = requestCycle.urlFor(reference, null);
		return url.toString();
	}
}
