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
package org.geowe.client.local.model.vector;

import java.io.Serializable;

/**
 * Clase que representa la definición de los metadatos de un atributo de una capa vectorial.
 * Se registran el nombre, el tipo de datos y si se debe mostrar el atributo en el pop-up
 * del Map Tooltip.
 * 
 * @author Atanasio Muñoz
 *
 */
public class FeatureAttributeDef implements Serializable {
	private static final long serialVersionUID = -9120800425164493424L;

	private String name;
	private AttributeType type;
	private boolean showInTooltip; 
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AttributeType getType() {
		return type;
	}
	public void setType(AttributeType type) {
		this.type = type;
	}
	public boolean isShowInTooltip() {
		return showInTooltip;
	}
	public void setShowInTooltip(boolean showInTooltip) {
		this.showInTooltip = showInTooltip;
	}
	
	@Override
	public String toString() {
		return name;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		FeatureAttributeDef other = (FeatureAttributeDef) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}	
}
