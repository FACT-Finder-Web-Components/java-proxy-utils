package de.omikron.helper.domain;

import java.io.Serializable;

public class SortItem implements Serializable {

	private static final long	serialVersionUID	= -4073882830335230756L;
	private String				description			= null;
	private String				name				= null;
	private String				order				= null;
	private String				searchParams		= null;
	private boolean				selected			= false;

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(final String order) {
		this.order = order;
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

}
