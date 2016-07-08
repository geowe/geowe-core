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
package org.geowe.client.local.style;

import java.io.Serializable;

import com.google.gwt.resources.client.ImageResource;

/**
 * Definicion del estilo de representacion de vertices de una geometria. 
 * 
 * @author Atanasio Muñoz
 *
 */
public class VertexStyleDef implements Serializable {
	private static final long serialVersionUID = 2075464863461080362L;

	private String name;
	private String styleName;
	private ImageResource image;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	public ImageResource getImage() {
		return image;
	}
	public void setImage(ImageResource image) {
		this.image = image;
	}
}
