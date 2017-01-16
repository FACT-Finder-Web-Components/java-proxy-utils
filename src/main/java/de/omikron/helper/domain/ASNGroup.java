package de.omikron.helper.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ASNGroup implements Serializable {

	private static final long	serialVersionUID	= -5563870397200998188L;
	private int							detailedLinks		= -1;
	private String						filterStyle			= null;
	private int							groupOrder			= -1;
	private String						name				= null;
	private ArrayList<ASNGroupElement>	elements			= new ArrayList<ASNGroupElement>();
	private ArrayList<ASNGroupElement>	selectedElements	= new ArrayList<ASNGroupElement>();
	private boolean						showPreviewImages	= false;
	private String						type				= null;
	private String						unit				= null;

	public int getDetailedLinks() {
		return detailedLinks;
	}

	public void setDetailedLinks(final int detailedLinks) {
		this.detailedLinks = detailedLinks;
	}

	public String getFilterStyle() {
		return filterStyle;
	}

	public void setFilterStyle(final String filterStyle) {
		this.filterStyle = filterStyle;
	}

	public int getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(final int groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public ArrayList<ASNGroupElement> getElements() {
		return elements;
	}

	public void setElements(final ArrayList<ASNGroupElement> elements) {
		this.elements = elements;
	}

	public ArrayList<ASNGroupElement> getSelectedElements() {
		return selectedElements;
	}

	public void setSelectedElements(final ArrayList<ASNGroupElement> selectedElements) {
		this.selectedElements = selectedElements;
	}

	public boolean isShowPreviewImages() {
		return showPreviewImages;
	}

	public void setShowPreviewImages(final boolean showPreviewImages) {
		this.showPreviewImages = showPreviewImages;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(final String unit) {
		this.unit = unit;
	}

	public void addElement(final ASNGroupElement ge) {
		this.elements.add(ge);
	}

	public void addSelectedElement(final ASNGroupElement ge) {
		this.selectedElements.add(ge);
	}
}
