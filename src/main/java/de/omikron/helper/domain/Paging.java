package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Paging implements Serializable {

	private static final long	serialVersionUID	= -3567225810316641668L;
	private int					currentPage			= -1;
	private int					pageCount			= -1;
	private int					resultsPerPage		= -1;
	private PageLink			firstLink			= null;
	private PageLink			lastLink			= null;
	private PageLink			nextLink			= null;
	private PageLink			previousLink		= null;
	private ArrayList<PageLink>	pageLinks			= new ArrayList<PageLink>();

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(final int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(final int pageCount) {
		this.pageCount = pageCount;
	}

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(final int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public PageLink getFirstLink() {
		return firstLink;
	}

	public void setFirstLink(final PageLink firstLink) {
		this.firstLink = firstLink;
	}

	public PageLink getLastLink() {
		return lastLink;
	}

	public void setLastLink(final PageLink lastLink) {
		this.lastLink = lastLink;
	}

	public PageLink getNextLink() {
		return nextLink;
	}

	public void setNextLink(final PageLink nextLink) {
		this.nextLink = nextLink;
	}

	public PageLink getPreviousLink() {
		return previousLink;
	}

	public void setPreviousLink(final PageLink previousLink) {
		this.previousLink = previousLink;
	}

	public ArrayList<PageLink> getPageLinks() {
		return pageLinks;
	}

	public void setPageLinks(final ArrayList<PageLink> pageLinks) {
		this.pageLinks = pageLinks;
	}

	public void addPageLink(final PageLink pageLink) {
		this.pageLinks.add(pageLink);
	}

}
