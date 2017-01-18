package de.omikron.helper.reponse;

import de.omikron.helper.dom.SearchResult;

public class SearchResponse {

	private SearchResult searchResult;

	public SearchResponse() {
	}

	public SearchResult getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(SearchResult searchResult) {
		this.searchResult = searchResult;
	}

	@Override
	public String toString() {
		return "SearchResponse [searchResult=" + searchResult + "]";
	}

}
