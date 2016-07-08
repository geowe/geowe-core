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
package org.geowe.client.local.main.tool.map.catalog.model;

import java.io.Serializable;

import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.resources.client.ImageResource;

/**
 * Implementacion base de una definicion de capa, tanto raster como vectorial.
 *  
 * @author Atanasio Muñoz
 *
 */
public abstract class LayerDef implements Serializable {	
	private static final long serialVersionUID = -2605220671126500616L;
	
	public static final String RASTER_TYPE = "RASTER";
	public static final String VECTOR_TYPE = "VECTOR";
	public static final String COMPOSITE_TYPE = "COMPOSITE";
	
	private String name;
	private String type;
	private String description;
	private ImageResource icon;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ImageResource getIcon() {
		return icon;
	}
	public void setIcon(ImageResource icon) {
		this.icon = icon;
	}
	
	/**
	 * Construye la capa en base a los datos de definicion de la misma.
	 * 
	 * @return Layer - capa raster o vectorial
	 */
	public abstract Layer getLayer();
	
	@Override
	public String toString() {
		return "Layer [" + name + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LayerDef other = (LayerDef) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	 
}
