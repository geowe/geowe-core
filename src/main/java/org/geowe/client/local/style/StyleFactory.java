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

import org.geowe.client.local.model.style.FillStyle;
import org.geowe.client.local.model.style.LineStyle;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.Random;

/**
 * Factoria de creacion de estilos y mapas de estilos para las capas vectoriales
 * de manera centralizada y en todas sus combinaciones.
 * 
 * @author Atanasio Muñoz
 *
 */
public final class StyleFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(StyleFactory.class.getName());

	private StyleFactory() {
	}

	/**
	 * Crea un mapa de estilos por defecto, aplicando color normal
	 * uno obtenido de manera aleatoria.
	 * 
	 * @return
	 */
	public static StyleMap createDefaultStyleMap() {
		String default_normal_color = String.valueOf(Random.nextInt());
		return createStyleMap(
				StyleFactory.stringToColour(default_normal_color),
				FillStyle.DEFAULT_SELECTED_COLOR, 
				FillStyle.DEFAULT_HOVER_COLOR, null, null);
	}

	/**
	 * Método para crear un mapa de estilos personalizando solamente
	 * el color de relleno / color temático, y el etiquetado. En el resto
	 * de parámetros se aplican los valores por defecto. Si se especifica
	 * el color temático, se obvia el color de relleno normal.
	 * 
	 * @param normalColor
	 * @param selectedColor
	 * @param hoverColor
	 * @param attributeLabel
	 * @param colorThemingAttribute
	 * @return
	 */
	public static StyleMap createStyleMap(String normalColor,
			String selectedColor, String hoverColor,
			String attributeLabel, String colorThemingAttribute) {

		return new StyleMap(
				createStyle(LineStyle.DEFAULT_NORMAL_COLOR, LineStyle.DEFAULT_THICKNESS,
						normalColor, FillStyle.DEFAULT_OPACITY / 100.0,
						attributeLabel, colorThemingAttribute), 
				createStyle(LineStyle.DEFAULT_SELECTED_COLOR, LineStyle.DEFAULT_THICKNESS,
						selectedColor, FillStyle.DEFAULT_OPACITY / 100.0,
						attributeLabel, null),
				createStyle(LineStyle.DEFAULT_HOVER_COLOR, LineStyle.DEFAULT_THICKNESS,
						hoverColor, FillStyle.DEFAULT_OPACITY / 100.0,
						attributeLabel, null));
	}

	/**
	 * Método básico para creación de un estilo personalizado. Si se 
	 * especifica el atributo para el color temático, se obvia el color
	 * de relleno normal.
	 * 
	 * @param strokeColor
	 * @param strokeWidth
	 * @param fillColor
	 * @param fillOpacity
	 * @param attributeLabel
	 * @param colorThemingAttribute
	 * @return
	 */
	public static Style createStyle(String strokeColor, int strokeWidth,
			String fillColor, double fillOpacity, 
			String attributeLabel, String colorThemingAttribute) {

		Style style = new org.gwtopenmaps.openlayers.client.Style();
		style.setJSObject(createOpenLayersStyle(strokeColor, strokeWidth,
				fillColor, fillOpacity,
				attributeLabel, colorThemingAttribute));

		return style;
	}

	public static Style createReshapeStyle() {
		Style reshapeStyle = new org.gwtopenmaps.openlayers.client.Style();
		reshapeStyle.setJSObject(createOpenLayersStyle(
				LineStyle.DEFAULT_NORMAL_COLOR, LineStyle.DEFAULT_THICKNESS,
				FillStyle.DEFAULT_NORMAL_COLOR, FillStyle.DEFAULT_OPACITY / 100.0, 
				null, null));
		reshapeStyle.setLabel("");
		return reshapeStyle;
	}

	private static native JSObject createOpenLayersStyle(
			String strokeColor, int strokeWidth,
			String fillColor, double fillOpacity, 
			String attributeLabel,
			String colorThemingAttribute) /*-{
		var style = new $wnd.OpenLayers.Style(
				{
					strokeColor : (colorThemingAttribute != null ? "${getColor}"
							: strokeColor),
					strokeWidth : strokeWidth,
					fillColor : (colorThemingAttribute != null ? "${getColor}"
							: fillColor),
					fillOpacity : fillOpacity,
					pointRadius : 5,
					strokeOpacity : 1.0,
					graphicName : "circle",

					fontWeight : "bold",
					labelAlign : "center",
					fontFamily : "Courier New, monospace",
					fontColor : "#FFFFFF",
					fontSize : "12px",
					label : "${getLabel}"
				},
				{
					context : {

						getLabel : function(feature) {
							if (attributeLabel == null) {
								return "";
							}

							if (feature.attributes[attributeLabel]) {
								return feature.attributes[attributeLabel];
							} else {
								return "";
							}

						},

						getColor : function(feature) {
							if (colorThemingAttribute == null) {
								return fillColor;
							}

							var themingValue = feature.attributes[colorThemingAttribute];

							return @org.geowe.client.local.style.StyleFactory::stringToColour(Ljava/lang/String;)(themingValue);
						}
					}
				});

		return style;
	}-*/;

	public static String stringToColour(String str) {
		String baseStr = str;
		if (str == null) {
			baseStr = "000000";
		}
		if (baseStr.length() < 6) {
			baseStr = getFormatedColor(baseStr);
		}

		String color = getHashColor(baseStr);

		if (color.length() > 6) {
			color = color.substring(0, 6);
		} else if (color.length() < 6) {
			color = getFormatedColor(color);
		}

		return "#" + color;
	}

	private static String getFormatedColor(String color) {
		StringBuffer formattedColor = new StringBuffer(color);
		for (int i = color.length(); i < 6; i++) {
			formattedColor.append("0");
		}
		return formattedColor.toString();
	}

	private static String getHashColor(String str) {
		int hash = str.hashCode();
		return Integer.toHexString(((hash >> 24) & 0xFF))
				+ Integer.toHexString(((hash >> 16) & 0xFF))
				+ Integer.toHexString(((hash >> 8) & 0xFF));
	}

	/**
	 * Funcion de conveniencia para exportar la funcion de transformacion de
	 * cadena a color RGB de Java a Javascript para su invocacion desde el
	 * contexto del estilo
	 */
	public static native void exportStringToRGB() /*-{
		$wnd.stringToColour = @org.geowe.client.local.style.StyleFactory::stringToColour(Ljava/lang/String;);
	}-*/;
}
