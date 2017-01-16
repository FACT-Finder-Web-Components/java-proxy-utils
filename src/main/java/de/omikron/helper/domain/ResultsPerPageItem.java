package de.omikron.helper.domain;

import java.io.Serializable;

public class ResultsPerPageItem implements Serializable {

	private static final long	serialVersionUID	= -3970374701513734900L;
	boolean						defaultValue		= false;
	String						searchParams		= null;
	boolean						selected			= false;
	int							value				= -1;

	public boolean isDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(final boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(final String searchParams) {
		this.searchParams = searchParams;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
