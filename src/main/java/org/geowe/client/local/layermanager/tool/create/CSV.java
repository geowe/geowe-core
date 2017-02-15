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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.util.Attributes;


/**
 * Write CSV from VectorFeature and read CSV to VectorFeature.<br>
 * First line indicates the attribute names. <br>
 * geoCSV (WKT option):. Last column must be WKT. <br>
 * geoCSV (Point option): last two column must be X and Y. <br>
 * 
 * @author rafa@geowe.org
 * @since 15-02-2017 Added support for alphanumeric csv. First line indicates
 *        the attribute names. <br>
 */
public class CSV {
	private String projection;
	private final String CSV_SEPARATOR = ";";
	private final String GEOM_COLUMN_NAME = "WKT";

	public CSV() {
		super();
	}

	public CSV(String projection) {
		super();
		this.projection = projection;
	}

	public String write(VectorLayer layer) {
		String csvContent = "undefined";
		if (layer != null && layer.getFeatures() != null) {
			csvContent = write(new ArrayList<VectorFeature>(Arrays.asList(layer.getFeatures())));
		}
		return csvContent;
	}

	public String write(List<VectorFeature> selectedFeatures) {
		StringBuffer csv = new StringBuffer(getColumnName(selectedFeatures.get(0)));

		for (VectorFeature vectorFeature : selectedFeatures) {
			VectorFeature transformedFeature = getTransformedFeatures(vectorFeature);
			for (String attributeValue : transformedFeature.getAttributes().getAttributeValues()) {
				csv.append(attributeValue + CSV_SEPARATOR);
			}
			csv.append(getWkt(transformedFeature));
			csv.append("\n");
		}
		return csv.toString();
	}

	private String getColumnName(VectorFeature feature) {
		StringBuffer columnName = new StringBuffer("");
		for (String attributeName : feature.getAttributes().getAttributeNames()) {
			columnName.append(attributeName + CSV_SEPARATOR);
		}
		columnName.append(GEOM_COLUMN_NAME);
		columnName.append("\n");
		return columnName.toString();
	}

	private String getWkt(VectorFeature vectorFeature) {
		WKT wktFormat = new WKT();
		return wktFormat.write(vectorFeature);
	}

	private VectorFeature getTransformedFeatures(VectorFeature vectorFeature) {
		VectorFeature featureToExport = vectorFeature.clone();
		featureToExport.getGeometry().transform(new Projection(GeoMap.INTERNAL_EPSG), new Projection(this.projection));

		return featureToExport;
	}

	public List<VectorFeature> read(String geoDataString) {
		String[] csvLines = geoDataString.split("\n");

		String[] csvAttrName = csvLines[0].split(CSV_SEPARATOR);

		return createFeatures(csvLines, csvAttrName);
	}

	private List<VectorFeature> createFeatures(String[] csvLines, String[] csvAttrName) {
		List<VectorFeature> features = new ArrayList<VectorFeature>();
		for (int i = 1; i < csvLines.length; i++) {
			String[] csvFeature = csvLines[i].split(CSV_SEPARATOR);

			VectorFeature feature = null;
			if (hasWKT(csvAttrName[csvAttrName.length - 1])) {
				feature = new VectorFeature(getGeom(csvFeature[csvFeature.length - 1]));
			} else {
				feature = new VectorFeature(
						getPoint(csvFeature[csvFeature.length - 2], csvFeature[csvFeature.length - 1]));
			}

			if (isCsvLineValid(csvAttrName, csvFeature)) {
				feature.setAttributes(getAttributes(csvAttrName, csvFeature));
			}
			features.add(feature);
		}
		return features;
	}

	private boolean hasWKT(String attrName) {
		return GEOM_COLUMN_NAME.equals(attrName);
	}

	private Geometry getGeom(String wkt) {
		return Geometry.fromWKT((wkt));
	}

	private Point getPoint(String coordX, String coordY) {
		return new Point(Double.valueOf(coordX), Double.valueOf(coordY));
	}

	private Attributes getAttributes(String[] csvAttrName, String[] csvFeature) {
		Attributes attributes = new Attributes();
		for (int i = 0; i < csvAttrName.length; i++) {
			if (GEOM_COLUMN_NAME.equals(csvAttrName[i])) {
				continue;
			}
			attributes.setAttribute(csvAttrName[i], csvFeature[i]);
		}
		return attributes;
	}

	private boolean isCsvLineValid(String[] csvAttrName, String[] csvFeature) {
		return csvAttrName.length == csvFeature.length;
	}

	public String[] readAttributeNames(String data) {
		String[] csvLines = data.split("\n");

		String[] csvAttrName = csvLines[0].split(CSV_SEPARATOR);
		return csvAttrName;
	}

	public List<CsvItem> getItems(String data) {
		List<CsvItem> items = new ArrayList<CsvItem>();
		String[] csvLines = data.split("\n");
		String[] csvAttrNames = csvLines[0].split(CSV_SEPARATOR);
		for (int i = 1; i < csvLines.length; i++) {
			String[] csvItem = csvLines[i].split(CSV_SEPARATOR);

			CsvItem item = null;
			if (isCsvLineValid(csvAttrNames, csvItem)) {
				item = getCsvItem(csvAttrNames, csvItem);
			}
			items.add(item);
		}

		return items;
	}

	private CsvItem getCsvItem(String[] csvAttrNames, String[] csvItem) {
		CsvItem item = new CsvItem();
		for (int i = 0; i < csvAttrNames.length; i++) {
			item.addAttribute(csvAttrNames[i], csvItem[i]);
		}

		return item;
	}

	/**
	 * Representa un elemento extraido del csv.
	 * 
	 * @author rafa@geowe.org
	 *
	 */
	public class CsvItem {
		private Map<String, String> attributes = new HashMap<String, String>();

		public void addAttribute(String attrName, String attrValue) {
			attributes.put(attrName, attrValue);
		}

		public String getValue(String attrName) {
			return attributes.get(attrName);
		}

		public Set<String> getAttributeNames() {
			return attributes.keySet();
		}

		@Override
		public String toString() {
			return "CsvItem [attributes=" + attributes + "]";
		}

	}
}
