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
package org.geowe.client.local.main.tool.search.searcher;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

/**
 * 
 * @author geowe
 *
 */
public abstract class VectorFeatureSearcher implements Searcher {

	@Override
	public List<VectorFeature> search(List<VectorFeature> features,
			String valueToSearch, String attributeName, boolean caseSensitive) {
		List<VectorFeature> filteredFeatures = new ArrayList<VectorFeature>();
		for (VectorFeature vector : features) {

			String attributeValue = getAttributeValue(vector, attributeName);
			if (caseSensitive) {
				if (hasBeenFound(attributeValue, valueToSearch, attributeName)) {
					filteredFeatures.add(vector);
				}
			} else {
				if (hasBeenFound(attributeValue.toUpperCase(),
						valueToSearch.toUpperCase(),
						attributeName)) {
					filteredFeatures.add(vector);
				}
			}


		}
		return filteredFeatures;
	}

	private String getAttributeValue(VectorFeature vector, String attributeName) {
		return vector.getAttributes().getAttributeAsString(attributeName);
	}

	public abstract boolean hasBeenFound(String string, String valueToSearch,
			String attributeName);

	public double getDoubleValue(String value) {
		return Double.parseDouble(value);
	}

}
