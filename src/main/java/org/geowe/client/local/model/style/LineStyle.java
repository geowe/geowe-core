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

/**
 * Modelo del estilo de línea de una capa vectorial. 
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class LineStyle {
	public static final String DEFAULT_NORMAL_COLOR = "#000000";
	public static final String DEFAULT_HOVER_COLOR = "#000000";
	public static final String DEFAULT_SELECTED_COLOR = "#ffff00";
	public static final int DEFAULT_THICKNESS = 3;
	public static final int MIN_THICKNESS = 0;
	public static final int MAX_THICKNESS = 20;
	
	private String normalColor;
	private String hoverColor;
	private String selectedColor;
	private int thickness;
	
	public LineStyle() {
		this.normalColor = DEFAULT_NORMAL_COLOR;
		this.hoverColor = DEFAULT_HOVER_COLOR;
		this.selectedColor = DEFAULT_SELECTED_COLOR;
		this.thickness = DEFAULT_THICKNESS;
	}
	
	public String getNormalColor() {
		return normalColor;
	}

	public void setNormalColor(String normalColor) {
		this.normalColor = normalColor;
	}

	public String getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(String hoverColor) {
		this.hoverColor = hoverColor;
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(String selectedColor) {
		this.selectedColor = selectedColor;
	}

	public int getThickness() {
		return thickness;
	}
	
	public void setThickness(int thickness) {
		
		if(thickness < MIN_THICKNESS) {
			this.thickness = MIN_THICKNESS;
		} else if(thickness > MAX_THICKNESS) {
			this.thickness = MAX_THICKNESS;		
		} else {
			this.thickness = thickness;
		}
	}
}
