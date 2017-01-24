package de.omikron.helper.reponse;

import java.util.List;

import de.omikron.helper.dom.Campaign;

/**
 * TODO: This is not implemented yet.
 * 
 * @author arno.pitters
 *
 */
public class ProductCampaignResponse extends FFResponse {

	private List<Campaign> campaigns;

	public ProductCampaignResponse() {
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	@Override
	public String toString() {
		return "ProductCampaignResponse [campaigns=" + campaigns + "]";
	}

	
}
