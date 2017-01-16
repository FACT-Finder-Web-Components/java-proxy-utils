package de.omikron.helper.domain;

import java.io.Serializable;

public class SearchControlParams implements Serializable {

	private static final long	serialVersionUID	= 109038621570972305L;
	private boolean				disableCache		= false;
	private boolean				generateAdvisorTree	= false;
	private boolean				idsOnly				= false;
	private boolean				useAsn				= true;
	private boolean				useCampaigns		= true;
	private boolean				useFoundWords		= false;
	private boolean				useKeywords			= true;

	public boolean isDisableCache() {
		return disableCache;
	}

	public void setDisableCache(final boolean disableCache) {
		this.disableCache = disableCache;
	}

	public boolean isGenerateAdvisorTree() {
		return generateAdvisorTree;
	}

	public void setGenerateAdvisorTree(final boolean generateAdvisorTree) {
		this.generateAdvisorTree = generateAdvisorTree;
	}

	public boolean isIdsOnly() {
		return idsOnly;
	}

	public void setIdsOnly(final boolean idsOnly) {
		this.idsOnly = idsOnly;
	}

	public boolean isUseAsn() {
		return useAsn;
	}

	public void setUseAsn(final boolean useAsn) {
		this.useAsn = useAsn;
	}

	public boolean isUseCampaigns() {
		return useCampaigns;
	}

	public void setUseCampaigns(final boolean useCampaigns) {
		this.useCampaigns = useCampaigns;
	}

	public boolean isUseFoundWords() {
		return useFoundWords;
	}

	public void setUseFoundWords(final boolean useFoundWords) {
		this.useFoundWords = useFoundWords;
	}

	public boolean isUseKeywords() {
		return useKeywords;
	}

	public void setUseKeywords(final boolean useKeywords) {
		this.useKeywords = useKeywords;
	}
}
