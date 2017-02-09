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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.VectorLayerStyleWidget;
import org.geowe.client.local.style.VertexStyles;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

/**
 * Asistente que implementa la lógica de aplicación de los estilos, tanto a nivel
 * de capa como a nivel de feature, a partir de la configuración obtenida del 
 * diálogo de gestión de estilos.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
@ApplicationScoped
public class VectorStyleAssistant {	
	@Inject
	private VectorLayerStyleWidget vectorLayerStyleWidget;
	@Inject
	private MessageDialogBuilder dialogBuilder;
	
	public void applyLayerStyle(final VectorLayer selectedLayer) {
		VectorStyleDef style = selectedLayer.getVectorStyle();
		applyStyle(style, selectedLayer);						
	}
	
	public void applyFeatureStyle(VectorFeature[] features, VectorLayer layer) {
		for(VectorFeature feature : features) {
			applyFeatureStyle(feature, layer);						
		}		
	}
	
	public void applyFeatureStyle(VectorFeature feature, VectorLayer layer) {
		VectorFeatureStyleDef featureStyle = new VectorFeatureStyleDef(feature, layer);
		applyStyle(featureStyle, layer);
		feature.setStyle(featureStyle.toStyle(feature));
	}
	
	private void applyStyle(VectorStyleDef style, VectorLayer selectedLayer) {
		applyColorStyle(style);
		applyVertextStyle(style);
		applyLabelStyle(style, selectedLayer);
		applyColorThemingStyle(style, selectedLayer);
	}
	
	private void applyColorStyle(VectorStyleDef style) {
		style.getFill().setNormalColor(vectorLayerStyleWidget.getFillColor());
		style.getFill().setOpacity(vectorLayerStyleWidget.getFillOpacity());
		style.getLine().setNormalColor(vectorLayerStyleWidget.getStrokeColor());
		style.getLine().setThickness(vectorLayerStyleWidget.getStrokeWidth());
	}
	
	private void applyVertextStyle(VectorStyleDef style) {
		if(vectorLayerStyleWidget.isBasicVertexStyle()) { 
			style.getPoint().setVertexStyle(VertexStyles.getByStyleName(
					vectorLayerStyleWidget.getVertexStyle()));
			style.getPoint().setExternalGraphic(null);
		} else {
			int graphicWidth = vectorLayerStyleWidget.getGraphicWidth();
			int graphicHeight = vectorLayerStyleWidget.getGraphicHeight();
			
			style.getPoint().setExternalGraphic(
					vectorLayerStyleWidget.getExternalGraphic());
			style.getPoint().setGraphicWidth(
					graphicWidth > 0 ? graphicWidth : PointStyle.DEFAULT_GRAPHIC_WIDTH);
			style.getPoint().setGraphicHeight(
					graphicHeight > 0 ? graphicHeight : PointStyle.DEFAULT_GRAPHIC_HEIGHT);
		}				
	}
	
	private void applyLabelStyle(VectorStyleDef style, VectorLayer selectedLayer) {
		if(vectorLayerStyleWidget.isEnableLabeling()) {
			if(vectorLayerStyleWidget.getAttributeLabel() != null
					&& vectorLayerStyleWidget.getFontSize() != null) {			
				style.getLabel().setAttribute(
					selectedLayer.getAttribute(vectorLayerStyleWidget.getAttributeLabel()));
				style.getLabel().setFontSize(vectorLayerStyleWidget.getFontSize());
			} else {
				dialogBuilder.createError(UIMessages.INSTANCE.vlswErrorDialogTitle(), 
						UIMessages.INSTANCE.vlswRequiredLabelDataText()).show();
			}
			style.getLabel().setBoldStyle(vectorLayerStyleWidget.isUseBoldLabel());
			style.getLabel().setBackgroundColor(vectorLayerStyleWidget.getBackgroundColor());
		} else {
			style.getLabel().setAttribute(null);
		}
	}
	
	private void applyColorThemingStyle(VectorStyleDef style, VectorLayer selectedLayer) {
		if(vectorLayerStyleWidget.isEnableTheming()) {
			if(vectorLayerStyleWidget.getAttributeTheming() != null) {
				style.setColorThemingAttribute(
					selectedLayer.getAttribute(vectorLayerStyleWidget.getAttributeTheming()));
				style.setEnableLegend(vectorLayerStyleWidget.isEnableLegend());
			} else {
				dialogBuilder.createError(UIMessages.INSTANCE.vlswErrorDialogTitle(), 
						UIMessages.INSTANCE.vlswRequiredThemingDataText()).show();
				style.setEnableLegend(false);
			}			
		} else {
			style.setColorThemingAttribute(null);
			style.setEnableLegend(false);
		}			
	}	
}
