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
package org.geowe.client.local.main.tool.project;

import java.util.ArrayList;
import java.util.List;

import org.gwtopenmaps.openlayers.client.format.GeoJSON;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Modelo de proyecto para la sesión de trabajo con GeoWE
 * 
 * @author jose@geowe.org
 *
 */
public class Project {
	private String version;
	private String title;
	private String description;
	private String date;
	private String name;
	private List<ProjectVectorLayer> vectors = new ArrayList<ProjectVectorLayer>();

	public void setName(String name) {
		this.name = name;
	};

	public void add(String layerName, String geojsonContent, String fillColor, Double fillOpacity, String strokeColor, Double strokeWidth) {
		final StyleProjectLayer style = new StyleProjectLayer(fillColor, fillOpacity, strokeColor, strokeWidth);		
		final ProjectVectorLayer layer = new ProjectVectorLayer(layerName, geojsonContent, style);		
		GeoJSON geoJSON = new GeoJSON();		
		layer.setNumElements(geoJSON.read(geojsonContent).length);
		vectors.add(layer);		
	}

	public String getName() {
		return this.name;
	};

	public List<ProjectVectorLayer> getVectors() {
		return this.vectors;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String toJSON() {

		JSONObject projectObject = new JSONObject();
		projectObject.put("version", new JSONString(getVersion()));
		projectObject.put("title", new JSONString(getTitle()));
		projectObject.put("description", new JSONString(getDescription()));
		projectObject.put("date", new JSONString(getDate()));
		//projectObject.put("name", new JSONString(getName()));
		
		
		JSONArray layersArray = new JSONArray();
		int index = 0;
		for(ProjectVectorLayer projectLayer: vectors) {
			layersArray.set(index, projectLayer.getJSONObject());
			
			index++;
		}
		
		projectObject.put("vectors", layersArray);

		return projectObject.toString();
	}

	public int getNumElements() {
		int total = 0;
		for(ProjectVectorLayer layer: vectors) {
			total = total + layer.getNumElements();
		}
		return total;
	}
}
