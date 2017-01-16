package de.omikron.helper.domain;

import java.io.Serializable;

public class FeedbackText implements Serializable {

	private static final long	serialVersionUID	= -233676860886629010L;
	private boolean				html				= false;
	private int					id					= -1;
	private String				label				= null;
	private String				text				= null;

	public boolean isHtml() {
		return html;
	}

	public void setHtml(final boolean html) {
		this.html = html;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
