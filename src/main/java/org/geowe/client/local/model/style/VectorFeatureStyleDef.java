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

import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.VertexStyles;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Esta clase modela de una manera estructurada los estilos de un elemento
 * vectorial, de manera que se puedan manejar de manera directa y sencilla
 * sin tener que acceder a propiedades Javascript.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
public class VectorFeatureStyleDef extends VectorStyleDef {
	
	public VectorFeatureStyleDef() {
		super();
	}
	
	/**
	 * Crea un nuevo modelo de estilos a partir del objeto Style del elemento
	 * que recibe como parámetro.
	 *  
	 * @param feature Elemento cuyo estilo se desea modelar
	 * @param layer Capa a la que pertenece el elemento
	 */
	public VectorFeatureStyleDef(VectorFeature feature, VectorLayer layer) {
		super();
		
		getColorStyleData(feature.getStyle());
		getVertexStyleData(feature.getStyle());
		getLabelStyleData(feature.getStyle(), layer);
	}

	private void getColorStyleData(Style style) {
		if(style != null) {
			JSObject jsStyle = style.getJSObject();
			String fillColor = jsStyle.getPropertyAsString("userFillColor");			
			String strokeColor = jsStyle.getPropertyAsString("userStrokeColor");
			int thickness = (int) Math.floor(style.getStrokeWidth());
						
			/**
			 * Si estamos en modo de estilo por feature, hay que coger el color 
			 * de referencia de las propiedades userFillColor y userStrokeColor, 
			 * puesto que los features afectados están seleccionados y por tanto
			 * tienen asignado el color de relleno y linea del modo selección 
			 */
			getFill().setNormalColor(fillColor != null ? fillColor : style.getFillColor());
			getFill().setOpacity(style.getFillOpacity());
			getLine().setNormalColor(strokeColor != null ? strokeColor : style.getStrokeColor());
			getLine().setThickness(thickness);	
		}
	}
	
	private void getVertexStyleData(Style style) {
		if(style != null) {			
			getPoint().setVertexStyle(VertexStyles.getByStyleName(
					style.getGraphicName()));
			
			getPoint().setExternalGraphic(style.getExternalGraphic());
			getPoint().setGraphicWidth(style.getGraphicWidth());
			getPoint().setGraphicHeight(style.getGraphicHeight());
		}
	}
	
	private void getLabelStyleData(Style style, VectorLayer layer) {
		if(style != null) {
			JSObject jsStyle = style.getJSObject();
			
			boolean enabled = jsStyle.getPropertyAsString("userAttributeLabel") != null 
					&& !jsStyle.getPropertyAsString("userAttributeLabel").isEmpty();			
					
			if(enabled) {
				getLabel().setAttribute(layer.getAttribute(
						jsStyle.getPropertyAsString("userAttributeLabel")));
											
				getLabel().setFontSize(getFontSize(jsStyle));
				getLabel().setBoldStyle(isBoldFont(jsStyle));
				getLabel().setBackgroundColor(jsStyle.getPropertyAsString("labelOutlineColor"));
			} 
		} 
	}
	
	private Integer getFontSize(JSObject style) {		
		String fontSize = style.getPropertyAsString("fontSize");
		
		if(fontSize != null && !fontSize.isEmpty()) {
			fontSize = fontSize.substring(0, fontSize.lastIndexOf("px"));
			return Integer.parseInt(fontSize);					
		}
		return LabelStyle.DEFAULT_FONT_SIZE;
	}
	
	private boolean isBoldFont(JSObject style) {
		String fontWeight = style.getPropertyAsString("fontWeight");
		
		if(fontWeight == null || fontWeight.isEmpty()) {
			fontWeight = "regular";
		}
		return fontWeight.equalsIgnoreCase("bold");
	}
	
	public Style toStyle(VectorFeature feature) {
		Style style = new Style();
		
		applyColorStyle(style);
		applyVertextStyle(style);
		applyLabelStyle(style, feature);
		
		return style;
	}
		
	private void applyColorStyle(Style style) {
		style.setFillColor(getFill().getNormalColor());
		style.setFillOpacity(getFill().getOpacity());
		style.setStrokeColor(getLine().getNormalColor());
		style.setStrokeWidth(getLine().getThickness());
		
		/**
		 * Se almacena el mismo color de relleno y linea en las propiedades
		 * userFillColor y userStrokeColor para tenerlo siempre disponible
		 * y asi poder aplicar los efectos de select y hover mediante eventos
		 * (ver VectorLayer.addFeatureSelectListeners())
		 */
		JSObject jsStyle = style.getJSObject();
		jsStyle.setProperty("userFillColor", getFill().getNormalColor());
		jsStyle.setProperty("userStrokeColor", getLine().getNormalColor());
	}
	
	private void applyVertextStyle(Style style) {		 
		style.setGraphicName(getPoint().getVertexStyle().getStyleName());
		style.setExternalGraphic(getPoint().getExternalGraphic());
		style.setGraphicSize(getPoint().getGraphicWidth(), 
				getPoint().getGraphicHeight());								
	}
	
	private void applyLabelStyle(Style style, VectorFeature feature) {				
		JSObject jsStyle = style.getJSObject();
		
		if(getLabel().isEnabled()) {								
			style.setLabel(feature.getAttributes().getAttributeAsString(
					getLabel().getAttribute().getName()));
			style.setFontSize(getLabel().getFontSize() + "px");
				
			/**
			 * Se almacena el atributo de etiquetado en la propiedad
			 * userAttributeLabel para tenerlo disponible a la hora de
			 * recargar el dialogo de estilos
			 */
			jsStyle.setProperty("userAttributeLabel", 
					getLabel().getAttribute().getName());
			
			style.setFontWeight(getLabel().isBoldStyle() ? "bold" : "regular");					
			
			final boolean labelBackgroung = !getLabel()
					.getBackgroundColor().isEmpty();	
			
			jsStyle.setProperty("labelOutlineWidth", (labelBackgroung ? 10 : 0));
			jsStyle.setProperty("labelOutlineColor", (labelBackgroung ?
					getLabel().getBackgroundColor() : ""));			
		} else {
			style.setLabel(null);
			jsStyle.unsetProperty("userAttributeLabel");
		}
	}			
}
