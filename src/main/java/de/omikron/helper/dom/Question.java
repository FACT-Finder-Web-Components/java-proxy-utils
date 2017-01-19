package de.omikron.helper.dom;

import java.util.List;

public class Question {

	private List<Answer>	answers;
	private String			id;
	private String			text;

	public Question() {
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Question [answers=" + answers + ", id=" + id + ", text=" + text + "]";
	}

}
