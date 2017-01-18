package de.omikron.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;

import de.omikron.helper.domain.ASNGroup;
import de.omikron.helper.domain.ASNGroupElement;
import de.omikron.helper.domain.AdvisorAnswer;
import de.omikron.helper.domain.AdvisorQuestion;
import de.omikron.helper.domain.Campaign;
import de.omikron.helper.domain.FeedbackText;
import de.omikron.helper.domain.JSONErrorMessage;
import de.omikron.helper.domain.PageLink;
import de.omikron.helper.domain.Paging;
import de.omikron.helper.domain.Record;
import de.omikron.helper.domain.Result;
import de.omikron.helper.domain.ResultsPerPageItem;
import de.omikron.helper.domain.SearchControlParams;
import de.omikron.helper.domain.SortItem;
import de.omikron.helper.domain.Suggestion;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class JSON610Adapter {

	// private static final Logger LOG = Logger.getLogger(JSON610Adapter.class);

	private static JsonPath	rawResultPath		= JsonPath.compile("$.searchResult");

	private static JsonPath	rawArray			= JsonPath.compile("$.[*]");

	private static JsonPath	rawSuggestionsArray	= JsonPath.compile("$.suggestions.[*]");

	private static JsonPath	rawError			= JsonPath.compile("$");

	public static JSONErrorMessage parseErrorMessage(final String rawJson) {
		try {
			JSONObject read = rawError.read(rawJson);
			String errorMessage = (String) read.get("error");
			String stacktrace = " " + (String) read.get("stacktrace");
			return new JSONErrorMessage(errorMessage, stacktrace);
		} catch (Exception e) {
			return new JSONErrorMessage(
					"Could not create the Error Message object. Parsing the given rawJson String failed: ",
					e.getStackTrace().toString());
		}
	}

	/**
	 * 
	 * @param rawJson
	 *            The rawJson Array with the records
	 * @return NULL if the rawJson String was NULL or an ArrayList<
	 *         {@link de.factfinder.json.json610.Record}>
	 */
	public static ArrayList<Record> getRecommendationFromRawJson(final String rawJson) {
		ArrayList<Record> result = null;
		try {
			if (rawJson != null) {
				JSONArray recommendedRecords = rawArray.read(rawJson);
				result = new ArrayList<Record>(recommendedRecords.size());
				for (int i = 0; i < recommendedRecords.size(); i++) {
					JSONObject currentRecord = (JSONObject) recommendedRecords.get(i);
					result.add(JSON610Adapter.generateRecordFromJSON(currentRecord));
				}
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
		return result;
	}

	/**
	 * This method sets the given JSONObject Baseinformation for the given
	 * result
	 * 
	 * @param result
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setBaseInfoForResult(final Result result, final JSONObject object) {
		try {
			result.setChannel((String) object.get("channel"));
			result.setRefKey((String) object.get("refKey"));
			result.setResultStatus((String) object.get("resultStatus"));
			result.setResultArticleNumberStatus((String) object.get("resultArticleNumberStatus"));
			result.setSearchParams((String) object.get("searchParams"));
			result.setTimedOut((Boolean) object.get("timedOut"));
			result.setSearchTime((Integer) object.get("searchTime"));
			result.setSimiFirstRecord((Integer) object.get("simiFirstRecord"));
			result.setSimiLastRecord((Integer) object.get("simiLastRecord"));
			result.setResultCount((Integer) object.get("resultCount"));
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the {@link de.factfinder.json.json610.domain.Record}
	 * Objects for the {@link de.factfinder.json.json610.domain.Result}
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	@SuppressWarnings("unchecked")
	public static void setRecordsForResult(final Result ffResult, final JSONObject object) {
		List<JSONObject> recordsJson;
		try {
			recordsJson = (List<JSONObject>) object.get("records");

			for (final JSONObject JSONrecord : recordsJson) {
				// Create a new record
				final Record record = generateRecordFromJSON(JSONrecord);
				// Add the record to the result
				ffResult.addRecord(record);
			}
		} catch (final InvalidPathException e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the {@link de.factfinder.json.json610.domain.ASNGroups}
	 * Objects for the {@link de.factfinder.json.json610.domain.Result}
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	@SuppressWarnings("unchecked")
	public static void setASNGroupsForResult(final Result ffResult, final JSONObject object) {
		List<JSONObject> groupsJson;
		try {
			groupsJson = (List<JSONObject>) object.get("groups");

			for (final JSONObject groupJson : groupsJson) {
				// Create a new record
				final ASNGroup group = new ASNGroup();

				// Fill the base info for the group
				group.setDetailedLinks((Integer) groupJson.get("detailedLinks"));
				group.setFilterStyle((String) groupJson.get("filterStyle"));
				group.setGroupOrder((Integer) groupJson.get("groupOrder"));
				group.setName((String) groupJson.get("name"));
				group.setShowPreviewImages((Boolean) groupJson.get("showPreviewImages"));
				group.setType((String) groupJson.get("type"));
				group.setUnit((String) groupJson.get("unit"));

				// Fill the elements and selected elements of the current group
				fillASNElementList(groupJson, group, "elements");
				fillASNElementList(groupJson, group, "selectedElements");

				// Add the record to the result
				ffResult.addASNGroup(group);
			}
		} catch (final InvalidPathException e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the {@link de.factfinder.json.json610.domain.Paging}
	 * (<i>including the {@link de.factfinder.json.json610.domain.PageLink}
	 * Objects by invoking the {link {@link #createPageLink(JSONObject)}
	 * method</i>) Objects for the
	 * {@link de.factfinder.json.json610.domain.Result}.
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setPagingForResult(final Result ffResult, final JSONObject object) {
		try {
			final JSONObject pagingJson = (JSONObject) object.get("paging");
			final Paging paging = new Paging();
			paging.setCurrentPage((Integer) pagingJson.get("currentPage"));
			paging.setPageCount((Integer) (pagingJson.get("pageCount")));
			paging.setResultsPerPage((Integer) (pagingJson.get("resultsPerPage")));

			JSONObject jsonLink = (JSONObject) pagingJson.get("firstLink");
			paging.setFirstLink(createPageLink(jsonLink));

			jsonLink = (JSONObject) pagingJson.get("lastLink");
			paging.setLastLink(createPageLink(jsonLink));

			jsonLink = (JSONObject) pagingJson.get("nextLink");
			paging.setNextLink(createPageLink(jsonLink));

			jsonLink = (JSONObject) pagingJson.get("previousLink");
			paging.setPreviousLink(createPageLink(jsonLink));

			final JSONArray jsonPageLinks = (JSONArray) pagingJson.get("pageLinks");
			if (jsonPageLinks != null) {
				for (int i = 0; i < jsonPageLinks.size(); i++) {
					paging.addPageLink(createPageLink((JSONObject) jsonPageLinks.get(i)));
				}
			}

			ffResult.setPaging(paging);
		} catch (final Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the
	 * {@link de.factfinder.json.json610.domain.ResultsPerPageItem} Objects for
	 * the {@link de.factfinder.json.json610.domain.Result}
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setResultsPerPageForResult(final Result ffResult, final JSONObject object) {
		try {
			final JSONArray jsonResultsPerPageList = (JSONArray) object.get("resultsPerPageList");
			for (int i = 0; i < jsonResultsPerPageList.size(); i++) {
				final JSONObject jsonResultsPerPageItem = (JSONObject) jsonResultsPerPageList.get(i);
				final ResultsPerPageItem rppi = new ResultsPerPageItem();
				rppi.setDefaultValue((Boolean) jsonResultsPerPageItem.get("default"));
				rppi.setSearchParams((String) jsonResultsPerPageItem.get("searchParams"));
				rppi.setSelected((Boolean) jsonResultsPerPageItem.get("selected"));
				rppi.setValue((Integer) jsonResultsPerPageItem.get("value"));
				ffResult.addResultsPerPageItem(rppi);
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the
	 * {@link de.factfinder.json.json610.domain.SearchControlParams} Objects for
	 * the {@link de.factfinder.json.json610.domain.Result}
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setSearchControlParamsForResult(final Result ffResult, final JSONObject object) {
		try {
			final JSONObject jsonSearchControlParams = (JSONObject) object.get("searchControlParams");
			final SearchControlParams scp = new SearchControlParams();
			scp.setDisableCache((Boolean) jsonSearchControlParams.get("disableCache"));
			scp.setGenerateAdvisorTree((Boolean) jsonSearchControlParams.get("generateAdvisorTree"));
			scp.setIdsOnly((Boolean) jsonSearchControlParams.get("idsOnly"));
			scp.setUseAsn((Boolean) jsonSearchControlParams.get("useAsn"));
			scp.setUseCampaigns((Boolean) jsonSearchControlParams.get("useCampaigns"));
			scp.setUseFoundWords((Boolean) jsonSearchControlParams.get("useFoundWords"));
			scp.setUseKeywords((Boolean) jsonSearchControlParams.get("useKeywords"));
			ffResult.setSearchControlParams(scp);
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the {@link de.factfinder.json.json610.domain.SortItem}
	 * Objects for the {@link de.factfinder.json.json610.domain.Result}
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setSortItemsForResult(final Result ffResult, final JSONObject object) {
		try {
			final JSONArray jsonSortsList = (JSONArray) object.get("sortsList");
			for (int i = 0; i < jsonSortsList.size(); i++) {
				final JSONObject jsonSortItem = (JSONObject) jsonSortsList.get(i);
				final SortItem sortItem = new SortItem();
				sortItem.setDescription((String) jsonSortItem.get("description"));
				sortItem.setName((String) jsonSortItem.get("name"));
				sortItem.setOrder((String) jsonSortItem.get("order"));
				sortItem.setSearchParams((String) jsonSortItem.get("searchParams"));
				sortItem.setSelected((Boolean) jsonSortItem.get("selected"));
				ffResult.addSortItem(sortItem);
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * This method sets the {@link de.factfinder.json.json610.domain.Campaign}
	 * for the {@link de.factfinder.json.json610.domain.Result} by invoking the
	 * {@link #parseCampaignFromJSON(JSONObject)} Method for each Campaign in
	 * the received JSON-Respopnse.
	 * 
	 * @param ffResult
	 *            the result where the data goes to
	 * @param object
	 *            the JSONObject of the complete JSON-Response received from
	 *            FACT-Finder
	 */
	public static void setCampaignsForResult(final Result ffResult, final JSONObject object) {
		try {
			final JSONArray jsonCampaigns = (JSONArray) object.get("campaigns");
			for (int i = 0; i < jsonCampaigns.size(); i++) {
				final JSONObject campaign = (JSONObject) jsonCampaigns.get(i);
				ffResult.addCampaign(parseCampaignFromJSON(campaign));
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * 
	 * 
	 * @param rawJson
	 *            This string have to be a JSON-Array
	 * @return Returns ArrayList<{@link de.factfinder.json.json610.Campaign}>.
	 *         The size is 0 if no campaigns could be found or something went
	 *         wrong.
	 */
	public static ArrayList<Campaign> getProductCampaignsFromRawJson(final String rawJson) {
		final JSONArray jsonCampaigns = rawArray.read(rawJson);
		final ArrayList<Campaign> results = new ArrayList<Campaign>();
		if (rawJson == null || rawJson.isEmpty()) {
			return results;
		}
		try {
			for (final Object currentCampaign : jsonCampaigns) {
				results.add(parseCampaignFromJSON((JSONObject) currentCampaign));
			}
		} catch (final Exception e) {
			// LOG.error("ERROR::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
			// LOG.error("ERROR::::::: currentString: " + rawJson);
		}
		return results;
	}

	/**
	 * 
	 * 
	 * @param rawJson
	 *            This string have to be a JSON-Array.
	 * @return returns ArrayList<{link de.factfinder.json.json610.Suggestion}>.
	 *         size is 0 if no suggestions could be found or something went
	 *         wrong.
	 */
	public static ArrayList<Suggestion> getSuggestionsFromRawJson(final String rawJson) {
		final ArrayList<Suggestion> results = new ArrayList<Suggestion>();
		if (rawJson == null || rawJson.isEmpty()) {
			return results;
		}

		try {
			final List<JSONObject> suggestions = rawSuggestionsArray.read(rawJson);
			// iterate throug the received JsonObjects(Suggestions)
			for (final Iterator<JSONObject> iterator = suggestions.iterator(); iterator.hasNext();) {
				final JSONObject currentSuggestion = iterator.next();

				// create a new sugestion and fill it
				final Suggestion newSuggestion = new Suggestion();
				newSuggestion.setHitCount((Integer) currentSuggestion.get("hitCount"));
				newSuggestion.setImgUrl((String) currentSuggestion.get("image"));
				newSuggestion.setSearchParams((String) currentSuggestion.get("searchParams"));
				newSuggestion.setName((String) currentSuggestion.get("name"));
				newSuggestion.setType((String) currentSuggestion.get("type"));

				// fill the attributes hashmap for the additional information
				final JSONObject attributes = (JSONObject) currentSuggestion.get("attributes");
				final HashMap<String, String> suggestionAttributes = new HashMap<String, String>();
				for (final String key : attributes.keySet()) {
					suggestionAttributes.put(key, (String) attributes.get(key));
				}

				newSuggestion.setAttributes(suggestionAttributes);
				results.add(newSuggestion);
			}
		} catch (final Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::");
			// LOG.error("currentString: " + rawJson, e);
		}
		return results;
	}

	/**
	 * 
	 * @param jsonRecord
	 *            The {@link de.factfinder.json.json610.Record} as raw-JSON
	 * @return the generated Record or NULL if something went wrong
	 */
	@SuppressWarnings("unchecked")
	public static Record generateRecordFromJSON(final JSONObject jsonRecord) {
		final Record record = new Record();
		// Fill the fields of the current record
		try {
			final Map<String, String> oneRecord = (Map<String, String>) jsonRecord.get("record");
			for (final Entry<String, String> entry : oneRecord.entrySet()) {
				record.addField(entry.getKey(), entry.getValue());
			}
			// Fill the basic info for the record
			record.setId((String) jsonRecord.get("id"));
			try {
				record.setPosition((Integer) jsonRecord.get("position"));
				record.setSearchSimilarity(Float.valueOf(jsonRecord.get("searchSimilarity").toString()));
				record.setSimiMalusAdd(Short.valueOf(jsonRecord.get("simiMalusAdd").toString()));

			} catch (NullPointerException e) {
				// do nothing its probably a Recommendation Record
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
			return null;
		}
		return record;
	}

	/**
	 * 
	 * This method sets the
	 * {@link de.factfinder.json.json610.domain.ASNGroupElement} Objects for the
	 * {@link de.factfinder.json.json610.domain.ASNGroup}
	 * 
	 * @param groupJson
	 * @param group
	 *            the ASNGroup where the data goes to
	 * @param identifier
	 *            specifies if the groupJson Parameter is an element or an
	 *            selectedElement
	 */
	public static void fillASNElementList(final JSONObject groupJson, final ASNGroup group, final String identifier) {
		// identifier can be elements or selectedElements
		try {
			final JSONArray elements = (JSONArray) groupJson.get(identifier);
			for (int i = 0; i < elements.size(); i++) {
				final ASNGroupElement groupElement = new ASNGroupElement();
				groupElement
						.setAssociatedFieldName((String) (((JSONObject) elements.get(i)).get("associatedFieldName")));
				groupElement.setClusterLevel((Integer) (((JSONObject) elements.get(i)).get("clusterLevel")));
				groupElement.setName((String) (((JSONObject) elements.get(i)).get("name")));
				groupElement.setPreviewImageURL((String) ((JSONObject) elements.get(i)).get("previewImageURL"));
				groupElement.setRecordCount((Integer) (((JSONObject) elements.get(i)).get("recordCount")));
				groupElement.setSearchParams((String) ((JSONObject) elements.get(i)).get("searchParams"));
				groupElement.setSelected((Boolean) (((JSONObject) elements.get(i)).get("selected")));
				;
				if (((JSONObject) elements.get(i)).get("absoluteMaxValue") != null)
					groupElement.setAbsoluteMaxValue((Double) ((JSONObject) elements.get(i)).get("absoluteMaxValue"));
				if (((JSONObject) elements.get(i)).get("absoluteMinValue") != null)
					groupElement.setAbsoluteMinValue((Double) ((JSONObject) elements.get(i)).get("absoluteMinValue"));
				if (((JSONObject) elements.get(i)).get("selectedMaxValue") != null)
					groupElement.setSelectedMaxValue((Double) ((JSONObject) elements.get(i)).get("selectedMaxValue"));
				if (((JSONObject) elements.get(i)).get("selectedMinValue") != null)
					groupElement.setSelectedMinValue((Double) ((JSONObject) elements.get(i)).get("selectedMinValue"));
				if ("elements".equals(identifier)) {
					group.addElement(groupElement);
				} else {
					group.addSelectedElement(groupElement);
				}
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the given
			// String:::::::::", e);
		}
	}

	/**
	 * 
	 * @param obj
	 *            The PageLink JSONObject which represents a single PageLink
	 * @return the pageLink or NULL if something went wrong
	 */
	public static PageLink createPageLink(final JSONObject obj) {
		try {
			final PageLink pageLink = new PageLink();
			pageLink.setCaption((String) obj.get("caption"));
			pageLink.setCurrentPage((Boolean) obj.get("currentPage"));
			pageLink.setNumber((Integer) obj.get("number"));
			pageLink.setSearchParams((String) obj.get("searchParams"));
			return pageLink;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * 
	 * Used in {@link #setCampaignsForResult(Result, JSONObject)}
	 * 
	 * @param jsonCampaign
	 *            a JSONObject which represents a single Campaign
	 * 
	 * @return The parsed {@link de.factfinder.json.json610.domain.Campaign} or
	 *         NULL if the JSONObject was invalid
	 */
	public static Campaign parseCampaignFromJSON(final JSONObject jsonCampaign) {

		Campaign campaign = new Campaign();
		try {
			campaign.setFlavour((String) jsonCampaign.get("flavour"));
			campaign.setId((String) jsonCampaign.get("id"));
			campaign.setName((String) jsonCampaign.get("name"));
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the redirect
			// campaign:::::::::", e);
			return null;
		}
		if ("REDIRECT".equals(campaign.getFlavour())) {
			campaign = addRedirectInformation(campaign, jsonCampaign);
		} else if ("FEEDBACK".equals(campaign.getFlavour())) {
			campaign = addFeedbackCampaignInformation(campaign, jsonCampaign);
		} else if ("ADVISOR".equals(campaign.getFlavour())) {
			campaign = addAdvisorInformation(campaign, jsonCampaign);
		}
		return campaign;
	}

	/**
	 * This method calls:<br />
	 * JSON610Adapter.setBaseInfoForResult<br />
	 * JSON610Adapter.setRecordsForResult<br />
	 * JSON610Adapter.setASNGroupsForResult<br />
	 * JSON610Adapter.setPagingForResult<br />
	 * JSON610Adapter.setResultsPerPageForResult<br />
	 * JSON610Adapter.setSearchControlParamsForResult<br />
	 * JSON610Adapter.setSortItemsForResult<br />
	 * JSON610Adapter.setCampaignsForResult<br />
	 * 
	 * @param result
	 *            the result where the received data goes to
	 * @param rawJson
	 *            the rawjson received from FACT-Finder
	 */
	public static void fillResult(final Result result, final String rawJson) {
		try {
			System.out.println("fillResult");
			JSONObject object = rawResultPath.read(rawJson);
			System.out.println("JSONObject:" + object);
			setBaseInfoForResult(result, object);
			setRecordsForResult(result, object);
			setASNGroupsForResult(result, object);
			setPagingForResult(result, object);
			setResultsPerPageForResult(result, object);
			setSearchControlParamsForResult(result, object);
			setSortItemsForResult(result, object);
			setCampaignsForResult(result, object);
		} catch (Exception e) {
			e.printStackTrace();
			// LOG.error("::::::: Couldnt fill the result:::::::::", e);
		}
	}

	private static Campaign addFeedbackCampaignInformation(final Campaign campaign, final JSONObject jsonCampaign) {
		try {
			// Feedback campaign, texts or pushed products:
			// Feebback texts:
			final JSONArray jsonFeedbacks = (JSONArray) jsonCampaign.get("feedbackTexts");
			if (jsonFeedbacks != null && jsonFeedbacks.size() > 0) {
				for (int i = 0; i < jsonFeedbacks.size(); i++) {
					final JSONObject JSONfeedback = (JSONObject) jsonFeedbacks.get(i);
					final FeedbackText fbt = new FeedbackText();
					fbt.setHtml((Boolean) JSONfeedback.get("html"));
					fbt.setId((Integer) JSONfeedback.get("id"));
					fbt.setLabel((String) JSONfeedback.get("label"));
					fbt.setText((String) JSONfeedback.get("text"));
					campaign.addFeedbackText(fbt);
				}
			}
			// Pushed Products:
			final JSONArray jsonPushedProducts = (JSONArray) jsonCampaign.get("pushedProductsRecords");
			for (int i = 0; i < jsonPushedProducts.size(); i++) {
				final JSONObject JSONpushedProduct = (JSONObject) jsonPushedProducts.get(i);
				final Record record = generateRecordFromJSON(JSONpushedProduct);
				campaign.addPushedProduct(record);
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the feedback
			// campaign:::::::::", e);
			return null;
		}
		return campaign;
	}

	private static Campaign addRedirectInformation(final Campaign campaign, final JSONObject jsonCampaign) {
		try {
			// Redirect campaign:
			final JSONObject jsonTarget = (JSONObject) jsonCampaign.get("target");
			if (jsonTarget != null) {
				campaign.setTargetDestination((String) jsonTarget.get("destination"));
				campaign.setTargetName((String) jsonTarget.get("name"));
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the redirect
			// campaign:::::::::", e);
			return null;
		}
		return campaign;
	}

	private static Campaign addAdvisorInformation(final Campaign campaign, final JSONObject jsonCampaign) {
		try {
			// Advisor campaign:
			final JSONArray jsonAdvisorQuestions = (JSONArray) jsonCampaign.get("activeQuestions");
			if (jsonAdvisorQuestions != null && jsonAdvisorQuestions.size() > 0) {
				for (int i = 0; i < jsonAdvisorQuestions.size(); i++) {
					final AdvisorQuestion aq = new AdvisorQuestion();
					final JSONObject jsonAdvisorQuestion = (JSONObject) jsonAdvisorQuestions.get(i);
					aq.setId((String) jsonAdvisorQuestion.get("id"));
					aq.setText((String) jsonAdvisorQuestion.get("text"));
					aq.setVisible((Boolean) jsonAdvisorQuestion.get("visible"));
					final JSONArray jsonAnswers = (JSONArray) jsonAdvisorQuestion.get("answers");
					if (jsonAnswers != null && jsonAnswers.size() > 0) {
						for (int j = 0; j < jsonAnswers.size(); j++) {
							final JSONObject jsonAnswer = (JSONObject) jsonAnswers.get(j);
							final AdvisorAnswer aa = new AdvisorAnswer();
							aa.setId((String) jsonAnswer.get("id"));
							aa.setParams((String) jsonAnswer.get("params"));
							aa.setText((String) jsonAnswer.get("text"));
							aq.addAdvisorAnswer(aa);
						}
					}
					campaign.addAdvisorQuestion(aq);
				}
			}
		} catch (Exception e) {
			// LOG.error("::::::: JSON610Adapter couldnt parse the advisor
			// campaign:::::::::", e);
			return null;
		}
		return campaign;
	}

}
