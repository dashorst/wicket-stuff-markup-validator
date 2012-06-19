package org.wicketstuff.htmlvalidator;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.wicket.util.string.Strings;

class Tag {
	private Collection<Attribute> attributes;

	private final String name;
	private CharSequence body;

	Tag(String name) {
		this.name = name;
		this.attributes = new TreeSet<Attribute>();
	}

	public Tag setBody(CharSequence body) {
		this.body = body;
		return this;
	}

	@Override
	public String toString() {
		if ("img|link".contains(name))
			return getOpenCloseTag();

		if (Strings.isEmpty(body))
			return getOpenTag() + getCloseTag();

		return getOpenTag() + body + getCloseTag();
	}

	public String getName() {
		return name;
	}

	public Tag attr(String name, CharSequence... values) {
		this.attributes.add(new Attribute(name, values));
		return this;
	}

	public String getOpenCloseTag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(name);
		if (!attributes.isEmpty()) {
			for (Attribute attribute : attributes) {
				sb.append(" ");
				sb.append(attribute);
			}
		}
		sb.append(" />");

		return sb.toString();
	}

	public String getOpenTag() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(name);
		if (!attributes.isEmpty()) {
			for (Attribute attribute : attributes) {
				sb.append(" ");
				sb.append(attribute);
			}
		}
		sb.append(">");

		return sb.toString();
	}

	public String getCloseTag() {
		StringBuilder sb = new StringBuilder();
		sb.append("</");
		sb.append(name);
		sb.append(">");
		return sb.toString();
	}
}
