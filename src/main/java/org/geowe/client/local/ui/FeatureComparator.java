/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.ui;

import java.util.Comparator;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

/**
 * Comparador de features vectoriales para un atributo dado. Se usa 
 * habitualmente para la ordenación de features en los listados.
 * 
 * @author Atanasio Muñoz
 *
 */
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
