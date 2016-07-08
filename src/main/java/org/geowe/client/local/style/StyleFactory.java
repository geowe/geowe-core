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

import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factoria de creacion de estilos y mapas de estilos para las capas 
 * vectoriales de manera centralizada y en todas sus combinaciones.
 * 
 * @author Atanasio Muñoz
 *
 */
public final class StyleFactory {

	private static final Logger LOG = LoggerFactory
			.getLogger(StyleFactory.class.getName());

	public static final String DEFAULT_NORMAL_COLOR = "#FF0000";
	public static final String DEFAULT_SELECTED_COLOR = "#00FF00";
	public static final String DEFAULT_HIGHLIGHTED_COLOR = "#0000FF";
	public static final String DEFAULT_STROKE_COLOR = "#000000";
	public static final String DEFAULT_SELECTED_STROKE_COLOR = "#ffff00";

	private StyleFactory() {
	}

	public static StyleMap createDefaultStyleMap() {
		return createStyleMap(DEFAULT_NORMAL_COLOR, DEFAULT_SELECTED_COLOR,
				DEFAULT_HIGHLIGHTED_COLOR, null, null);
	}

	public static StyleMap createStyleMap(String normalColor,
			String selectedColor, String highlightedColor,
			String attributeLabel, String colorThemingAttribute) {

		Style defaultStyle = new org.gwtopenmaps.openlayers.client.Style();
		Style selectStyle = new org.gwtopenmaps.openlayers.client.Style();
		Style temporaryStyle = new org.gwtopenmaps.openlayers.client.Style();

		defaultStyle.setJSObject(createOpenLayersStyle(DEFAULT_STROKE_COLOR,
				normalColor, attributeLabel, colorThemingAttribute));
		selectStyle.setJSObject(createOpenLayersStyle(
				DEFAULT_SELECTED_STROKE_COLOR, selectedColor, attributeLabel,
				null));
		temporaryStyle.setJSObject(createOpenLayersStyle(
				DEFAULT_SELECTED_STROKE_COLOR, highlightedColor,
				attributeLabel, null));

		StyleMap styleMap = new StyleMap(defaultStyle, selectStyle,
				temporaryStyle);

		return styleMap;
	}

	public static Style createStyle(String strokeColor, String fillColor,
			String attributeLabel, String colorThemingAttribute) {

		Style style = new org.gwtopenmaps.openlayers.client.Style();
		style.setJSObject(createOpenLayersStyle(strokeColor, fillColor,
				attributeLabel, colorThemingAttribute));

		return style;
	}

	public static Style createReshapeStyle() {

		Style reshapeStyle = new org.gwtopenmaps.openlayers.client.Style();
		reshapeStyle.setJSObject(createOpenLayersStyle(DEFAULT_STROKE_COLOR,
				DEFAULT_NORMAL_COLOR, null, null));
		reshapeStyle.setLabel("");
		return reshapeStyle;
	}

	private static native JSObject createOpenLayersStyle(String strokeColor,
			String fillColor, String attributeLabel,
			String colorThemingAttribute) /*-{
		var style = new $wnd.OpenLayers.Style(
				{
					strokeColor : (colorThemingAttribute != null ? "${getColor}"
							: strokeColor),
					strokeWidth : 3,
					fillColor : (colorThemingAttribute != null ? "${getColor}"
							: fillColor),
					fillOpacity : 0.5,
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
