package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Campaign implements Serializable {

	private static final long			serialVersionUID	= -8242472884194792759L;
	private String						category			= null;
	private String						flavour				= null;
	private String						id					= null;
	private String						name				= null;
	private String						targetDestination	= null;
	private String						targetName			= null;
	private ArrayList<AdvisorQuestion>	advisorQuestions	= new ArrayList<AdvisorQuestion>();
	private ArrayList<FeedbackText>		feedbackTexts		= new ArrayList<FeedbackText>();
	private ArrayList<Record>			pushedProducts		= new ArrayList<Record>();

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getFlavour() {
		return flavour;
	}

	public void setFlavour(final String flavour) {
		this.flavour = flavour;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getTargetDestination() {
		return targetDestination;
	}

	public void setTargetDestination(final String targetDestination) {
		this.targetDestination = targetDestination;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(final String targetName) {
		this.targetName = targetName;
	}

	public ArrayList<AdvisorQuestion> getAdvisorQuestions() {
		return advisorQuestions;
	}

	public void setAdvisorQuestions(final ArrayList<AdvisorQuestion> advisorQuestions) {
		this.advisorQuestions = advisorQuestions;
	}

	public ArrayList<FeedbackText> getFeedbackTexts() {
		return feedbackTexts;
	}

	public void setFeedbackTexts(final ArrayList<FeedbackText> feedbackTexts) {
		this.feedbackTexts = feedbackTexts;
	}

	public void addAdvisorQuestion(final AdvisorQuestion aq) {
		this.advisorQuestions.add(aq);
	}

	public void addFeedbackText(final FeedbackText ft) {
		this.feedbackTexts.add(ft);
	}

	public void addPushedProduct(final Record record) {
		this.pushedProducts.add(record);
	}

	public ArrayList<Record> getPushedProducts() {
		return pushedProducts;
	}

	public void setPushedProducts(final ArrayList<Record> pushedProducts) {
		this.pushedProducts = pushedProducts;
	}

}
