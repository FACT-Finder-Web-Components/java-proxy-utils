package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * Contains all information of a suggestion. additional information received in
 * the attributes JsonObject will be stored in the attributes hashmap(call
 * getAttributes() or getAttribute(final String key)) with the received JsonName
 * as key
 * 
 * @author tobias.armbruster
 */
public class Suggestion implements Serializable {

	private static final long		serialVersionUID	= 3668557649260774404L;
	private int						hitCount;
	private String					imgUrl;
	private String					searchParams;
	private String					type;
	private String					name;
	private HashMap<String, String>	attributes;

	public String getAttribute(final String key) {
		return attributes.get(key);
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(final HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(final int hitCount) {
		this.hitCount = hitCount;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(final String searchParams) {
		this.searchParams = searchParams;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

}
