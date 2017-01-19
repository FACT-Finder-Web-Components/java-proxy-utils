package de.omikron.helper.dom;

public class Answer {

	private String	id;
	private String	params;
	// private List<Question> questions;//TODO!
	private String	text;

	public Answer() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Answer [id=" + id + ", params=" + params + ", text=" + text + "]";
	}

}
