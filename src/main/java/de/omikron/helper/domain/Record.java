package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.HashMap;

public class Record implements Serializable {

	private static final long		serialVersionUID	= -5163229358038785196L;
	private String					id					= null;
	private HashMap<String, String>	fields				= new HashMap<String, String>();
	private int						position			= -1;

	private float					searchSimilarity;
	private short					simiMalusAdd;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}

	public HashMap<String, String> getFields() {
		return fields;
	}

	public void addField(final String fieldName, final String fieldContent) {
		fields.put(fieldName, fieldContent);
	}

	public void setFields(final HashMap<String, String> fields) {
		this.fields = fields;
	}

	public float getSearchSimilarity() {
		return searchSimilarity;
	}

	public short getSimiMalusAdd() {
		return simiMalusAdd;
	}

	public void setSearchSimilarity(final float searchSimilarity) {
		this.searchSimilarity = searchSimilarity;
	}

	public void setSimiMalusAdd(final short simiMalusAdd) {
		this.simiMalusAdd = simiMalusAdd;
	}

}
