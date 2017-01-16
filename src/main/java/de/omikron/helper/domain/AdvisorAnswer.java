package de.omikron.helper.domain;

import java.io.Serializable;

public class AdvisorAnswer implements Serializable {

	private static final long	serialVersionUID	= 8387644787885455062L;
	private String				id					= null;
	private String				params				= null;
	private String				text				= null;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(final String params) {
		this.params = params;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

}
