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
import org.geowe.client.local.style.StyleFactory;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Esta clase modela de una manera estructurada los estilos de una capa
 * vectorial, de manera que se puedan manejar de manera directa y sencilla
 * sin tener que acceder a propiedades Javascript.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class VectorStyleDef {
	private PointStyle point;
	private LineStyle line;
	private FillStyle fill;
	private LabelStyle label;

	private FeatureAttributeDef colorThemingAttribute;
	private boolean enableLegend;
	
	public VectorStyleDef() {
		this.point = new PointStyle();
		this.line = new LineStyle();
		this.fill = new FillStyle();		
		this.label = new LabelStyle();
		
		this.enableLegend = false;
	}
		
	public PointStyle getPoint() {
		return point;
	}

	public void setPoint(PointStyle point) {
		this.point = point;
	}	
	public LineStyle getLine() {
		return line;
	}

	public void setLine(LineStyle line) {
		this.line = line;
	}

	public FillStyle getFill() {
		return fill;
	}

	public void setFill(FillStyle fill) {
		this.fill = fill;
	}

	public LabelStyle getLabel() {
		return label;
	}

	public void setLabel(LabelStyle label) {
		this.label = label;
	}

	public FeatureAttributeDef getColorThemingAttribute() {
		return colorThemingAttribute;
	}

	public void setColorThemingAttribute(FeatureAttributeDef colorThemingAttribute) {
		this.colorThemingAttribute = colorThemingAttribute;
	}

	public boolean isEnableLegend() {
		return enableLegend;
	}

	public void setEnableLegend(boolean enableLegend) {
		this.enableLegend = enableLegend;
	}

	public boolean isColorThemingEnabled() {
		return getColorThemingAttribute() != null;
	}

	public StyleMap toStyleMap() {
		String labelAttribute = getLabel().isEnabled() ? getLabel().getAttribute().getName() : null;
		String colorThemingAttribute = isColorThemingEnabled() ? getColorThemingAttribute().getName() : null;
		
		Style normalStyle = StyleFactory.createStyle(
				getLine().getNormalColor(), getLine().getThickness(),
				getFill().getNormalColor(), getFill().getOpacity(), 
				labelAttribute, colorThemingAttribute);

		Style selectedStyle = StyleFactory.createStyle(
				getLine().getSelectedColor(), getLine().getThickness(),
				getFill().getSelectedColor(), getFill().getOpacity(), 
				labelAttribute, null);

		Style hoverStyle = StyleFactory.createStyle(
				getLine().getHoverColor(), getLine().getThickness(),
				getFill().getHoverColor(), getFill().getOpacity(), 
				labelAttribute, null);			
				
		JSObject jsStyle = normalStyle.getJSObject().getProperty("defaultStyle");						
		
		if (getLabel().isEnabled()) {
			jsStyle.setProperty("fontSize", getLabel().getFontSize() + "px");
			jsStyle.setProperty("fontWeight", getLabel().isBoldStyle() ? "bold" : "regular");
			
			final boolean labelBackgroung = getLabel().getBackgroundColor() != null;				
			jsStyle.setProperty("labelOutlineWidth", (labelBackgroung ? 10 : 0));
			jsStyle.setProperty("labelOutlineColor", labelBackgroung ?
					getLabel().getBackgroundColor() : "");				
		} 
		
		jsStyle.setProperty("graphicName", getPoint().getVertexStyle().getStyleName());
		jsStyle.setProperty("externalGraphic", getPoint().getExternalGraphic());
		jsStyle.setProperty("graphicWidth", getPoint().getGraphicWidth());
		jsStyle.setProperty("graphicHeight", getPoint().getGraphicHeight());
					
		return new StyleMap(normalStyle, selectedStyle, hoverStyle);
	}
}
