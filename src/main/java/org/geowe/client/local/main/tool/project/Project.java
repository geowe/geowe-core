package org.geowe.client.local.main.tool.project;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class Project {
	private String name;
	private List<ProjectLayer> layers = new ArrayList<ProjectLayer>();

	public void setName(String name) {
		this.name = name;
	};

	public void add(String layerName, String geojsonContent, String fillColor, String fillOpacity, String strokeColor, String strokeWidth) {
		final ProjectLayer layer = new ProjectLayer(layerName, geojsonContent);
		layer.setFillColor(fillColor);
		layer.setFillOpacity(fillOpacity);
		layer.setStrokeColor(strokeColor);
		layer.setStrokeWidth(strokeWidth);
		layers.add(layer);
	}

	public String getName() {
		return this.name;
	};

	public List<ProjectLayer> getLayers() {
		return this.layers;
	}

	public String toJSON() {

		JSONObject projectObject = new JSONObject();
		projectObject.put("name", new JSONString(getName()));
		
		JSONArray layersArray = new JSONArray();
		int index = 0;
		for(ProjectLayer projectLayer: layers) {
			layersArray.set(index, projectLayer.getJSONObject());
			
			index++;
		}
		
		projectObject.put("layers", layersArray);

		return projectObject.toString();
	}
}
