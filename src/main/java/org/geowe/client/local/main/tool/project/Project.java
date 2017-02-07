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
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Modelo de proyecto para la sesión de trabajo con GeoWE
 * 
 * @author jose@geowe.org
 *
 */
public class Project {
	public static final String VERSION_NAME = "version";
	public static final String DATE_NAME = "date";
	public static final String TITLE_NAME = "title";
	public static final String DESCRIPTION_NAME = "description";
	public static final String VECTORS_NAME = "vectors";
	public static final String NAME = "name";
	public static final String CONTENT_NAME = "content";
	public static final String STYLE_NAME = "style";
	
	private String version;
	private String title;
	private String description;
	private String date;
	private String name;
	private List<ProjectVectorLayer> vectors = new ArrayList<ProjectVectorLayer>();
	
	public Project() {
		
	}
	
	public Project(String json) {
		final JSONValue jsonValue = JSONParser.parseLenient(json);
		final JSONObject jsonObject = jsonValue.isObject();
		
		String projectDate = jsonObject.get(DATE_NAME).isString().stringValue();
		String projectTitle = jsonObject.get(TITLE_NAME).isString().stringValue();
		String projectVersion = jsonObject.get(VERSION_NAME).isString().stringValue();
		String projectDescription = jsonObject.get(DESCRIPTION_NAME).isString().stringValue();
		
		setDate(projectDate);
		setTitle(projectTitle);
		setVersion(projectVersion);
		setDescription(projectDescription);
		
		JSONArray layersArray = jsonObject.get(VECTORS_NAME).isArray();
		
		if (layersArray != null) {
	        for (int i = 0; i < layersArray.size(); i++) {
	            JSONObject projectLayerObj = layersArray.get(i).isObject();
	            String name = projectLayerObj.get(NAME).isString().stringValue();
	            String content = projectLayerObj.get(CONTENT_NAME).isString().stringValue();	            
	            JSONObject styleProjectLayer = projectLayerObj.get(STYLE_NAME).isObject();
	            
	            String fillColor = styleProjectLayer.get(ProjectLayerStyle.FILL_COLOR_NAME).isString().stringValue();
	            Double fillOpacity = styleProjectLayer.get(ProjectLayerStyle.FILL_OPACITY_NAME).isNumber().doubleValue();
	            String strokeColor = styleProjectLayer.get(ProjectLayerStyle.STROKE_COLOR_NAME).isString().stringValue();
	            Double strokeWidth = styleProjectLayer.get(ProjectLayerStyle.STROKE_WIDTH_NAME).isNumber().doubleValue();
	            
	            add(name, content, fillColor, fillOpacity, strokeColor, strokeWidth);	           
	        }
		
		}
	}
	

	public void setName(String name) {
		this.name = name;
	};

	public void add(String layerName, String geojsonContent, String fillColor, Double fillOpacity, String strokeColor, Double strokeWidth) {
		final ProjectLayerStyle style = new ProjectLayerStyle(fillColor, fillOpacity, strokeColor, strokeWidth);		
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
