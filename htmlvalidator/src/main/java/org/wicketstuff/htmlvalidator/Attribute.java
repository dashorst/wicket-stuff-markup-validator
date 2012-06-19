package org.wicketstuff.htmlvalidator;

import org.apache.wicket.util.string.Strings;

class Attribute implements Comparable<Attribute> {
	private final String name;
	private final CharSequence value;

	Attribute(String name) {
		this.name = name;
		this.value = "";
	}

	Attribute(String name, CharSequence value) {
		this.name = name;
		this.value = value;
	}

	Attribute(String name, CharSequence[] values) {
		this.name = name;
		StringBuilder sb = new StringBuilder();
		String separator = "";
		for (CharSequence value : values) {
			sb.append(separator);
			sb.append(value);
			separator = " ";
		}
		this.value = sb;
	}

	String getName() {
		return name;
	}

	CharSequence getValue() {
		return value;
	}

	@Override
	public String toString() {
		return name + "=\"" + value + "\"";
	}

	@Override
	public int compareTo(Attribute o) {
		return name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
