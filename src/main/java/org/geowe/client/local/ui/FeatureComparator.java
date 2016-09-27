package org.geowe.client.local.ui;

import java.util.Comparator;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

public class FeatureComparator implements Comparator<VectorFeature> {
	public static final int ORDER_ASC = 1;
	public static final int ORDER_DESC = -1;
	
	private String attribute;
	private int order;

	public FeatureComparator(String attribute, int order) {
		this.attribute = attribute;
		this.order = order;
	}
	
	@Override
	public int compare(VectorFeature o1, VectorFeature o2) {		
		return o1.getAttributes().getAttributeAsString(attribute).compareTo(
				o2.getAttributes().getAttributeAsString(attribute)) * order;
	}
}
