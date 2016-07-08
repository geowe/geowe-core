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
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.StyleFactory;
import org.geowe.client.local.style.VectorLayerStyleWidget;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.util.JSObject;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * 
 * @author geowe
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
			.slideOut(Direction.LEFT);
		} else {
			vectorLayerStyleWidget.asWidget().setVisible(true);
			vectorLayerStyleWidget.asWidget().getElement().<FxElement> cast()
					.slideIn(Direction.RIGHT);
			
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

				if (vectorLayerStyleWidget.isEnableTheming()) {
					for (final VectorFeature feature : selectedLayer.getFeatures()) {
						final String attValue = feature.getAttributes()
								.getAttributeAsString(
										vectorLayerStyleWidget
												.getAttributeTheming());
					}
				}

				/**
				 * Antes de nada, si se ha activado el color tematico, se debe
				 * aplicar este como primera operacion, ya que conlleva la
				 * creacion de un nuevo StyleMap
				 */
				applyColorThemingStyle(selectedLayer);
				applyLineAndColorStyle(selectedLayer);
				applyLabelAndVertexStyle(selectedLayer);

				selectedLayer.redraw();
				progressDialog.hide();
			}
		});
		
	}
		
	private void applyColorThemingStyle(final VectorLayer selectedLayer) {
		if(vectorLayerStyleWidget.isEnableTheming()
			 || vectorLayerStyleWidget.isEnableLabeling()) {
			selectedLayer.setStyleMap(
					StyleFactory.createStyleMap(
							StyleFactory.DEFAULT_NORMAL_COLOR, 
							StyleFactory.DEFAULT_SELECTED_COLOR, 
							StyleFactory.DEFAULT_HIGHLIGHTED_COLOR, 
							vectorLayerStyleWidget.isEnableLabeling() ? vectorLayerStyleWidget.getAttributeLabel() : null, 
							vectorLayerStyleWidget.isEnableTheming() ? vectorLayerStyleWidget.getAttributeTheming() : null));
		}
	}
	
	private void applyLineAndColorStyle(final VectorLayer selectedLayer) {
		// Estilo compuesto (StyleMap)
		if (selectedLayer.getStyle() == null) {
			final JSObject defaultStyle = getDefaultStyle(selectedLayer);

			// Solo se aplica el FillColor si NO se ha activado el color tematico
			if (!vectorLayerStyleWidget.isEnableTheming()) {
				defaultStyle.setProperty("fillColor",
						vectorLayerStyleWidget.getFillColor());
			}
			defaultStyle.setProperty("fillOpacity",
					vectorLayerStyleWidget.getFillOpacity() / 100.0);
			defaultStyle.setProperty("strokeColor",
					vectorLayerStyleWidget.getStrokeColor());
			defaultStyle.setProperty("strokeWidth",
					vectorLayerStyleWidget.getStrokeWidth());

			// Estilo simple
		} else {
			//Solo se aplica el FillColor si NO se ha activado el color tematico
			if(!vectorLayerStyleWidget.isEnableTheming()) {
				selectedLayer.getStyle().setFillColor(
						vectorLayerStyleWidget.getFillColor());
			}
			
			selectedLayer.getStyle().setFillOpacity(
					vectorLayerStyleWidget.getFillOpacity() / 100.0);
			selectedLayer.getStyle().setStrokeColor(
					vectorLayerStyleWidget.getStrokeColor());
			selectedLayer.getStyle().setStrokeWidth(
					vectorLayerStyleWidget.getStrokeWidth().doubleValue());									
		}
	}
	
	private void applyLabelAndVertexStyle(final VectorLayer selectedLayer) {
		// Estilo compuesto (StyleMap)
		if (selectedLayer.getStyle() == null) {	
			final JSObject defaultStyle = getDefaultStyle(selectedLayer);
//			defaultStyle.unsetProperty("label");
			if (vectorLayerStyleWidget.isEnableLabeling()) {
//				defaultStyle.setProperty("label",
//						"${" + vectorLayerStyleWidget.getAttributeLabel() + "}");
				defaultStyle.setProperty("fontSize", 
						vectorLayerStyleWidget.getFontSize() + "px");
				defaultStyle.setProperty("fontWeight", 
						vectorLayerStyleWidget.isUseBoldLabel() ? "bold" : "regular");
				
				final boolean labelBackgroung = vectorLayerStyleWidget.getBackgroundColor().length() > 0;				
				defaultStyle.setProperty("labelOutlineWidth", (labelBackgroung ? 10 : 0));
				defaultStyle.setProperty("labelOutlineColor", labelBackgroung ?
						vectorLayerStyleWidget.getBackgroundColor() : "");				
			} 
			
			defaultStyle.setProperty("graphicName", vectorLayerStyleWidget.getVertexStyle());

			// Estilo simple
		} else {
			
			selectedLayer.getStyle().setLabel("");
			if (vectorLayerStyleWidget.isEnableLabeling()) {
				selectedLayer.getStyle().setLabel(
						"${" + vectorLayerStyleWidget.getAttributeLabel() + "}");
				selectedLayer.getStyle().setFontSize(
						vectorLayerStyleWidget.getFontSize() + "px");								
				selectedLayer.getStyle().setFontWeight(
						vectorLayerStyleWidget.isUseBoldLabel() ? "bold" : "regular");									
			} 
			
			selectedLayer.getStyle().setGraphicName(vectorLayerStyleWidget.getVertexStyle());			
		}	
	}
	
	private JSObject getDefaultStyle(final VectorLayer layer) {
		return layer.getStyleMap().getJSObject().getProperty("styles")
				.getProperty("default").getProperty("defaultStyle");
	}
}
