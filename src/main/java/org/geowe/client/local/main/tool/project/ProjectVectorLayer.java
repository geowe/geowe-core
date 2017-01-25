package org.geowe.client.local.main.tool.project;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


/**
 * Modelo de capa vectorial para la configuración de la sesión de trabajo del proyecto
 * 
 * @author jose@geowe.org
 *
 */
public class ProjectVectorLayer {
	private String name;
	private String content;
	private StyleProjectLayer style;
	

	public ProjectVectorLayer(String name, String content, StyleProjectLayer style) {
		this.name = name;
		this.content = content;
		this.style = style;
	}

	public StyleProjectLayer getStyle() {
		return style;
	}

	public void setStyle(StyleProjectLayer style) {
		this.style = style;
	}

	public String getName() {
		return this.name;
	};

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return this.content;
	};

	public void setContent(String content) {
		this.content = content;
	}
	
	public JSONObject getJSONObject() {
		JSONObject projectLayerObject = new JSONObject();
		projectLayerObject.put("name", new JSONString(getName()));
		projectLayerObject.put("content", new JSONString(getContent()));
		projectLayerObject.put("style", style.getJSONObject());
		
		
		return projectLayerObject;		
	}
	
	public String toJSON() {
		return getJSONObject().toString();
	}
}
