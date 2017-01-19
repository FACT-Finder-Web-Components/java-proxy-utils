package de.omikron.helper.reponse;

import java.util.List;

public class SuggestResponse {

	private List<Suggestion> suggestions;

	public SuggestResponse() {
	}

	public List<Suggestion> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<Suggestion> suggestions) {
		this.suggestions = suggestions;
	}

	@Override
	public String toString() {
		return "SuggestResponse [suggestions=" + suggestions + "]";
	}

}
