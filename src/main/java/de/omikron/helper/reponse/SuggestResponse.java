package de.omikron.helper.reponse;

import java.util.List;

import de.omikron.helper.dom.Suggestion;

public class SuggestResponse extends FFResponse {

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
