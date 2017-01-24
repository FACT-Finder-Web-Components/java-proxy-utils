package de.omikron.helper.reponse;

import java.util.List;

import de.omikron.helper.dom.Record;

public class RecommendationResponse extends FFResponse {

	private List<Record> resultRecords;

	public RecommendationResponse() {
		// TODO Auto-generated constructor stub
	}

	public List<Record> getResultRecords() {
		return resultRecords;
	}

	public void setResultRecords(List<Record> resultRecords) {
		this.resultRecords = resultRecords;
	}

	@Override
	public String toString() {
		return "ProductCampaignResponse [resultRecords=" + resultRecords + "]";
	}
}
