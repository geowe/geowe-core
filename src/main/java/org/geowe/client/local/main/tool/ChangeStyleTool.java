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
package org.geowe.client.local.main.tool;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.style.PointStyle;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.SimpleThemingVerticalLegend;
import org.geowe.client.local.style.StyleFactory;
import org.geowe.client.local.style.VectorLayerStyleWidget;
import org.geowe.client.local.style.VertexStyles;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Herramienta de modificación de estilos de una capa vectorial. Muestra y 
 * oculta el diálogo de estilos, e implementa la lógica de aplicación del
 * estilo a la capa.
 * 
 * @author Atanasio Muñoz (ata@geowe.org)
 *
 */
@ApplicationScoped
public class ChangeStyleTool extends ButtonTool {
	@Inject
	private VectorLayerStyleWidget vectorLayerStyleWidget;
	@Inject
	private LayerManagerWidget layerTreeWidget;
	@Inject
	private ClientTaskManager taskManager;

	private SimpleThemingVerticalLegend legendPanel;

	@Inject
	public ChangeStyleTool(final GeoMap geoMap) {
		super(UIMessages.INSTANCE.changeStyleToolText(), ImageProvider.INSTANCE
				.style32());
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.changeStyleToolText(),
				UIMessages.INSTANCE.changeStyleToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		if (vectorLayerStyleWidget.asWidget().isVisible()) {
			vectorLayerStyleWidget.asWidget().getElement().<FxElement> cast()
					.fadeToggle();
		} else {
			vectorLayerStyleWidget.asWidget().getElement().<FxElement> cast()
					.fadeToggle();
			vectorLayerStyleWidget.asWidget().setVisible(true);			
		}
	}

	@PostConstruct
	private void registerListeners() {
		layerTreeWidget.addChangeLayerListener(this);
		
		vectorLayerStyleWidget.asWidget();
		vectorLayerStyleWidget.getApplyButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						onApplyButtonSelected(event);
					}
				});
		
		//Exportacion de la funcion stringToRGB al inicio para su
		//posterior uso al crear los estilos de color tematico
		StyleFactory.exportStringToRGB();
	}
	
	private void onApplyButtonSelected(final SelectEvent event) {

		final ProgressBarDialog progressDialog = new ProgressBarDialog(true,
				UIMessages.INSTANCE.vlswProgressBoxLabel(),
				UIMessages.INSTANCE.vlswProgressText());
		progressDialog.show();

		taskManager.execute(new Runnable() {
			@Override
			public void run() {
				final VectorLayer selectedLayer = vectorLayerStyleWidget
						.getSelectedLayer();

				applyChanges(selectedLayer);
				
				if(selectedLayer.getVectorStyle().isColorThemingEnabled()
						|| selectedLayer.getVectorStyle().getLabel().isEnabled()) {						
					applyLengendVisibility(selectedLayer);
				} else {
					hideLegendPanel();
				}

				selectedLayer.redraw();
				progressDialog.hide();
			}
		});		
	}
		
	private void applyChanges(VectorLayer selectedLayer) {
		VectorStyleDef style = selectedLayer.getVectorStyle();
		
		style.getFill().setNormalColor(vectorLayerStyleWidget.getFillColor());
		style.getFill().setOpacity(vectorLayerStyleWidget.getFillOpacity());
		style.getLine().setNormalColor(vectorLayerStyleWidget.getStrokeColor());
		style.getLine().setThickness(vectorLayerStyleWidget.getStrokeWidth());
		
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
		
		if(vectorLayerStyleWidget.isEnableLabeling()) {
			style.getLabel().setAttribute(
					selectedLayer.getAttribute(vectorLayerStyleWidget.getAttributeLabel()));
			style.getLabel().setFontSize(vectorLayerStyleWidget.getFontSize());
			style.getLabel().setBoldStyle(vectorLayerStyleWidget.isUseBoldLabel());
			style.getLabel().setBackgroundColor(vectorLayerStyleWidget.getBackgroundColor());
		} else {
			style.getLabel().setAttribute(null);
		}
		
		if(vectorLayerStyleWidget.isEnableTheming()) {
			style.setColorThemingAttribute(
					selectedLayer.getAttribute(vectorLayerStyleWidget.getAttributeTheming()));
			style.setEnableLegend(vectorLayerStyleWidget.isEnableLegend());
		} else {
			style.setColorThemingAttribute(null);
			style.setEnableLegend(false);
		}			
	}
	
	private void applyLengendVisibility(VectorLayer selectedLayer) {
		VectorStyleDef currentStyle = selectedLayer.getVectorStyle();
		
		if (currentStyle.isEnableLegend()) {
			if (legendPanel != null) {
				RootPanel.get().remove(legendPanel);				
			}
			
			legendPanel = new SimpleThemingVerticalLegend(selectedLayer,
					currentStyle.getColorThemingAttribute().getName());

			RootPanel.get().add(legendPanel);
			legendPanel.getElement().<FxElement> cast().fadeToggle();
			legendPanel.setVisible(true);
		} else {			
			hideLegendPanel();
		}
	}

	private void hideLegendPanel() {
		if (legendPanel != null && legendPanel.isVisible()) {
			legendPanel.getElement().<FxElement> cast().fadeToggle();
		}
	}			
}
