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
package org.geowe.client.local.layermanager.tool.create;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;

public class CSV {

	private final String projection;

	public CSV(String projection) {
		super();
		this.projection = projection;
	}

	public String write(VectorLayer layer) {
		return write(new ArrayList<VectorFeature>(Arrays.asList(layer
				.getFeatures())));
	}

	public String write(List<VectorFeature> selectedFeatures) {
		StringBuffer csv = new StringBuffer(
				getColumnName(selectedFeatures.get(0)));

		;
		for (VectorFeature vectorFeature : selectedFeatures) {
			VectorFeature transformedFeature = getTransformedFeatures(vectorFeature);
			for (String attributeValue : transformedFeature.getAttributes()
					.getAttributeValues()) {
				csv.append(attributeValue + ",");
			}
			csv.append(getWkt(transformedFeature));
			csv.append("\n");
		}
		return csv.toString();
	}

	private String getColumnName(VectorFeature feature) {
		StringBuffer columnName = new StringBuffer("");
		for (String attributeName : feature.getAttributes().getAttributeNames()) {
			columnName.append(attributeName + ",");
		}
		columnName.append("WKT");
		columnName.append("\n");
		return columnName.toString();
	}

	private String getWkt(VectorFeature vectorFeature) {
		WKT wktFormat = new WKT();
		return wktFormat.write(vectorFeature);
	}

	private VectorFeature getTransformedFeatures(VectorFeature vectorFeature) {

		VectorFeature featureToExport = vectorFeature.clone();
		featureToExport.getGeometry().transform(
				new Projection(GeoMap.INTERNAL_EPSG),
				new Projection(this.projection));

		return featureToExport;
	}

}