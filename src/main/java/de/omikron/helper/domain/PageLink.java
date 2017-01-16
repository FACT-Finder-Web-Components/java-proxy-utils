package de.omikron.helper.domain;

import java.io.Serializable;

public class PageLink implements Serializable {

	private static final long	serialVersionUID	= -8634797864297567071L;
	private String				caption				= null;
	private boolean				currentPage			= false;
	private int					number				= -1;
	private String				searchParams		= null;

	public String getCaption() {
		return caption;
	}

	public void setCaption(final String caption) {
		this.caption = caption;
	}

	public boolean isCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(final boolean currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(final String searchParams) {
		this.searchParams = searchParams;
	}

}
