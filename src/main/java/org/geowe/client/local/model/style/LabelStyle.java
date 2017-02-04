/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2017 GeoWE.org
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
package org.geowe.client.local.model.style;

import org.geowe.client.local.model.vector.FeatureAttributeDef;

/**
 * Modelo del estilo de etiquetado de una capa vectorial.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class LabelStyle {
	public static final int DEFAULT_FONT_SIZE = 12;
	
	private FeatureAttributeDef attribute;
	private int fontSize;
	private boolean boldStyle;
	private String backgroundColor;
	
	public LabelStyle() {
		this.fontSize = DEFAULT_FONT_SIZE;
		this.boldStyle = true;
		this.backgroundColor = "";
	}
	
	public boolean isEnabled() {
		return getAttribute() != null; 
	}
	
	public FeatureAttributeDef getAttribute() {
		return attribute;
	}
	
	public void setAttribute(FeatureAttributeDef attribute) {
		this.attribute = attribute;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public boolean isBoldStyle() {
		return boldStyle;
	}
	
	public void setBoldStyle(boolean boldStyle) {
		this.boldStyle = boldStyle;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
