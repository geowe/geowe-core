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

import org.geowe.client.local.style.StyleFactory;

import com.google.gwt.user.client.Random;

/**
 * Modelo del estilo de relleno de una capa vectorial.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class FillStyle {
	public static final String DEFAULT_NORMAL_COLOR = "#FF0000";
	public static final String DEFAULT_HOVER_COLOR = "#0000FF";
	public static final String DEFAULT_SELECTED_COLOR = "#00FF00";	
	public static final int DEFAULT_OPACITY = 50;
	public static final int MIN_OPACITY = 0;
	public static final int MAX_OPACITY = 100;
	
	private String normalColor;
	private String hoverColor;
	private String selectedColor;
	private int fillOpacity;
	
	public FillStyle() {
		this.normalColor = StyleFactory.stringToColour(
				String.valueOf(Random.nextInt()));
		this.hoverColor = DEFAULT_HOVER_COLOR;
		this.selectedColor = DEFAULT_SELECTED_COLOR;
		this.fillOpacity = DEFAULT_OPACITY;
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

	public int getOpacity() {
		return fillOpacity;
	}
	
	public void setOpacity(int fillOpacity) {
		
		if(fillOpacity < MIN_OPACITY) {
			this.fillOpacity = MIN_OPACITY;
		} else if(fillOpacity > MAX_OPACITY) {
			this.fillOpacity = MAX_OPACITY;
		} else {		
			this.fillOpacity = fillOpacity;
		}
	}
}
