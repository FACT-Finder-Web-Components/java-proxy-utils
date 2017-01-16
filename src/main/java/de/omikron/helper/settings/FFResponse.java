package de.omikron.helper.settings;

public class FFResponse {

	private String		content;
	private FFService	service;

	public FFResponse(String content, FFService service) {
		super();
		this.content = content;
		this.service = service;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FFService getService() {
		return service;
	}

	public void setService(FFService service) {
		this.service = service;
	}

}
