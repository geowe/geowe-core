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
