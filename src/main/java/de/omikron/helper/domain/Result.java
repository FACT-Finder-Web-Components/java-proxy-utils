package de.omikron.helper.domain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import de.omikron.helper.client.JSONParameterParser;

/**
 * @author arno.pitters
 *
 */
/**
 * @author arno.pitters
 *
 */
public class Result implements Serializable {

	private static final long				serialVersionUID			= 7059690891505121578L;
	
	private String							channel						= null;
	private String							refKey						= null;
	private String							resultArticleNumberStatus	= null;
	private int								resultCount					= -1;
	private String							resultStatus				= null;
	private String							searchParams				= null;
	private int								searchTime					= -1;
	private int								simiFirstRecord				= -1;
	private int								simiLastRecord				= -1;
	private boolean							timedOut					= false;
	private ArrayList<Record>				records						= new ArrayList<Record>();
	private ArrayList<ASNGroup>				asnGroups					= new ArrayList<ASNGroup>();
	private Paging							paging						= new Paging();
	private ArrayList<SortItem>				sortItems					= new ArrayList<SortItem>();
	private ArrayList<ResultsPerPageItem>	resultsPerPageItems			= new ArrayList<ResultsPerPageItem>();
	private ArrayList<Campaign>				campaigns					= new ArrayList<Campaign>();
	private SearchControlParams				searchControlParams			= null;

	public Result() {

	}

	public SearchControlParams getSearchControlParams() {
		return searchControlParams;
	}

	public void setSearchControlParams(final SearchControlParams searchControlParams) {
		this.searchControlParams = searchControlParams;
	}

	public String getQuery() {
		try {
			return URLDecoder.decode(JSONParameterParser.getParameterStringFromSearchParams(searchParams,
					JSONParameterParser.PARAMS_QUERY), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public void setCampaigns(final ArrayList<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public ArrayList<ResultsPerPageItem> getResultsPerPageItems() {
		return resultsPerPageItems;
	}

	public void setResultsPerPageItems(final ArrayList<ResultsPerPageItem> resultsPerPageItems) {
		this.resultsPerPageItems = resultsPerPageItems;
	}

	public ArrayList<SortItem> getSortItems() {
		return sortItems;
	}

	public void setSortItems(final ArrayList<SortItem> sortItems) {
		this.sortItems = sortItems;
	}

	public ArrayList<ASNGroup> getAsnGroups() {
		return asnGroups;
	}

	public void setAsnGroups(final ArrayList<ASNGroup> asnGroups) {
		this.asnGroups = asnGroups;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(final Paging paging) {
		this.paging = paging;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(final String channel) {
		this.channel = channel;
	}

	public String getRefKey() {
		return refKey;
	}

	public void setRefKey(final String refKey) {
		this.refKey = refKey;
	}

	public String getResultArticleNumberStatus() {
		return resultArticleNumberStatus;
	}

	public void setResultArticleNumberStatus(final String resultArticleNumberStatus) {
		this.resultArticleNumberStatus = resultArticleNumberStatus;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(final int resultCount) {
		this.resultCount = resultCount;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(final String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(final String searchParams) {
		this.searchParams = searchParams;
	}

	public int getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(final int searchTime) {
		this.searchTime = searchTime;
	}

	public int getSimiFirstRecord() {
		return simiFirstRecord;
	}

	public void setSimiFirstRecord(final int simiFirstRecord) {
		this.simiFirstRecord = simiFirstRecord;
	}

	public int getSimiLastRecord() {
		return simiLastRecord;
	}

	public void setSimiLastRecord(final int simiLastRecord) {
		this.simiLastRecord = simiLastRecord;
	}

	public boolean isTimedOut() {
		return timedOut;
	}

	public void setTimedOut(final boolean timedOut) {
		this.timedOut = timedOut;
	}

	public ArrayList<Record> getRecords() {
		return records;
	}

	public void setRecords(final ArrayList<Record> records) {
		this.records = records;
	}

	public void addRecord(final Record record) {
		this.records.add(record);
	}

	public void addASNGroup(final ASNGroup asnGroup) {
		this.asnGroups.add(asnGroup);
	}

	public void addSortItem(final SortItem sortItem) {
		this.sortItems.add(sortItem);
	}

	public void addResultsPerPageItem(final ResultsPerPageItem rppi) {
		this.resultsPerPageItems.add(rppi);
	}

	public void addCampaign(final Campaign c) {
		this.campaigns.add(c);
	}

	public ArrayList<Campaign> getCampaigns() {
		return campaigns;
	}

	@Override
	public String toString() {
		return "Result [channel=" + channel + ", refKey=" + refKey + ", resultArticleNumberStatus="
				+ resultArticleNumberStatus + ", resultCount=" + resultCount + ", resultStatus=" + resultStatus
				+ ", searchParams=" + searchParams + ", searchTime=" + searchTime + ", simiFirstRecord="
				+ simiFirstRecord + ", simiLastRecord=" + simiLastRecord + ", timedOut=" + timedOut + ", records="
				+ records + ", asnGroups=" + asnGroups + ", paging=" + paging + ", sortItems=" + sortItems
				+ ", resultsPerPageItems=" + resultsPerPageItems + ", campaigns=" + campaigns + ", searchControlParams="
				+ searchControlParams + "]";
	}

}
