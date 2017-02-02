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

import org.geowe.client.local.style.VertexStyleDef;
import org.geowe.client.local.style.VertexStyles;

/**
 * Modelo del estilo de puntos (vértices) de una capa vectorial.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class PointStyle {
	public static final String DEFAULT_VERTEX_STYLE = "circle";
	public static final int DEFAULT_GRAPHIC_WIDTH = 32;
	public static final int DEFAULT_GRAPHIC_HEIGHT = 32;
	
	private VertexStyleDef vertexStyle;
	private String externalGraphic;
	private int graphicWidth;
	private int graphicHeight;

	public PointStyle() {
		this.vertexStyle = VertexStyles.getByStyleName(DEFAULT_VERTEX_STYLE);
		this.graphicWidth = DEFAULT_GRAPHIC_WIDTH;
		this.graphicHeight = DEFAULT_GRAPHIC_HEIGHT;
		
		this.externalGraphic = null;
	}

	public VertexStyleDef getVertexStyle() {
		return vertexStyle;
	}

	public void setVertexStyle(VertexStyleDef vertexStyle) {
		this.vertexStyle = vertexStyle;
	}

	public String getExternalGraphic() {
		return externalGraphic;
	}

	public void setExternalGraphic(String externalGraphic) {
		this.externalGraphic = externalGraphic;
	}

	public int getGraphicWidth() {
		return graphicWidth;
	}

	public void setGraphicWidth(int graphicWidth) {
		this.graphicWidth = graphicWidth;
	}

	public int getGraphicHeight() {
		return graphicHeight;
	}

	public void setGraphicHeight(int graphicHeight) {
		this.graphicHeight = graphicHeight;
	}
}
