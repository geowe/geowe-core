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
package org.geowe.client.local.layermanager.tool.export;

import java.util.ArrayList;
import java.util.List;

public class VectorFormat {
	public static final VectorFormat GEO_JSON_FORMAT = new VectorFormat(1, "GeoJSON", "x-geo+json");
	public static final VectorFormat KML_FORMAT = new VectorFormat(2, "KML", "vnd.google-earth.kml+xml");
	public static final VectorFormat GML_FORMAT = new VectorFormat(3, "GML", "gml+xml");
	public static final VectorFormat WKT_FORMAT = new VectorFormat(4, "WKT", "x-wkt");
	public static final VectorFormat CSV_FORMAT = new VectorFormat(5, "CSV", "text/plain");
	public static final VectorFormat TOPO_JSON_FORMAT = new VectorFormat(6,
			"TopoJSON", "x-topo+json");
	public static final VectorFormat GPX_FORMAT = new VectorFormat(7, "GPX",
			"application/gpx+xml");
	public static final VectorFormat GEOJSON_CSS_FORMAT = new VectorFormat(8, "CSS-GeoJSON",
			"x-geo+json");
	
	private final int id;
	private final String name;
	private final String mimeType;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}	
	
	public String getMimeType() {
		return mimeType;
	}

	public VectorFormat(final int id, final String name, final String mimeType) {
		this.id = id;
		this.name = name;
		this.mimeType = mimeType;
	}
	
	public static VectorFormat getFromName(final String format) {
		final List<VectorFormat> allFormats = getSupportedImportFormat();
		
		for(final VectorFormat vectorFormat : allFormats) {
			if(vectorFormat.getName().toLowerCase().equals(format.toLowerCase())) {
				return vectorFormat; 
			}
		}
		
		return null;
	}
	
	public static List<VectorFormat> getSupportedImportFormat() {
		final List<VectorFormat> vectorFormats = new ArrayList<VectorFormat>();
		vectorFormats.add(GEO_JSON_FORMAT);
		vectorFormats.add(GEOJSON_CSS_FORMAT);
		vectorFormats.add(KML_FORMAT);
		vectorFormats.add(GML_FORMAT);
		vectorFormats.add(WKT_FORMAT);
		vectorFormats.add(TOPO_JSON_FORMAT);
		vectorFormats.add(GPX_FORMAT);
		vectorFormats.add(CSV_FORMAT);
		
		return vectorFormats;
	}


	public static List<VectorFormat> getSupportedExportVectorFormat() {
		final List<VectorFormat> vectorFormats = getSupportedImportFormat();
		vectorFormats.remove(TOPO_JSON_FORMAT);
		return vectorFormats;
	}
}
