package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class AdvisorQuestion implements Serializable {

	private static final long	serialVersionUID	= -6045089223987811903L;
	private String						id		= null;
	private String						text	= null;
	private boolean						visible	= false;
	private ArrayList<AdvisorAnswer>	answers	= new ArrayList<AdvisorAnswer>();

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void addAdvisorAnswer(final AdvisorAnswer aa) {
		this.answers.add(aa);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public ArrayList<AdvisorAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(final ArrayList<AdvisorAnswer> answers) {
		this.answers = answers;
	}
}
