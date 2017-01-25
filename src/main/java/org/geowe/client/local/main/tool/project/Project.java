package org.geowe.client.local.main.tool.project;

import java.util.ArrayList;
import java.util.List;

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
}
