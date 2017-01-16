package de.omikron.helper.domain;

import java.io.Serializable;

public class ASNGroupElement implements Serializable {

	private static final long	serialVersionUID	= 2962211373506887824L;
	private String				associatedFieldName	= null;
	private int					clusterLevel		= -1;
	private String				name				= null;
	private String				previewImageURL		= null;
	private int					recordCount			= -1;
	private String				searchParams		= null;
	private boolean				selected			= false;

	private double				absoluteMaxValue	= -1;
	private double				absoluteMinValue	= -1;
	private double				selectedMaxValue	= -1;
	private double				selectedMinValue	= -1;

	public double getAbsoluteMaxValue() {
		return absoluteMaxValue;
	}

	public void setAbsoluteMaxValue(final double absoluteMaxValue) {
		this.absoluteMaxValue = absoluteMaxValue;
	}

	public double getAbsoluteMinValue() {
		return absoluteMinValue;
	}

	public void setAbsoluteMinValue(final double absoluteMinValue) {
		this.absoluteMinValue = absoluteMinValue;
	}

	public double getSelectedMaxValue() {
		return selectedMaxValue;
	}

	public void setSelectedMaxValue(final double selectedMaxValue) {
		this.selectedMaxValue = selectedMaxValue;
	}

	public double getSelectedMinValue() {
		return selectedMinValue;
	}

	public void setSelectedMinValue(final double selectedMinValue) {
		this.selectedMinValue = selectedMinValue;
	}

	public String getAssociatedFieldName() {
		return associatedFieldName;
	}

	public void setAssociatedFieldName(final String associatedFieldName) {
		this.associatedFieldName = associatedFieldName;
	}

	public int getClusterLevel() {
		return clusterLevel;
	}

	public void setClusterLevel(final int clusterLevel) {
		this.clusterLevel = clusterLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPreviewImageURL() {
		return previewImageURL;
	}

	public void setPreviewImageURL(final String previewImageURL) {
		this.previewImageURL = previewImageURL;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(final int recordCount) {
		this.recordCount = recordCount;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(final String searchParams) {
		this.searchParams = searchParams;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}
}
