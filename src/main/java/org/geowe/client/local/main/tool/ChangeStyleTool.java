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
import org.geowe.client.local.model.style.VectorStyleAssistant;
import org.geowe.client.local.model.style.VectorStyleDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.SimpleThemingVerticalLegend;
import org.geowe.client.local.style.StyleFactory;
import org.geowe.client.local.style.VectorLayerStyleWidget;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
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
	@Inject
	private VectorStyleAssistant vectorStyleAssistant;
	@Inject
	private MessageDialogBuilder dialogBuilder;	
	@Inject
	private GeoMap geoMap;

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
			vectorLayerStyleWidget.setSelectedLayer((VectorLayer) layer);
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
		if(checkStyleSettings()) {
			if(!isFeatureStyleMode() && isFeatureStyleApplied()) {
				clearFeatureStyleConfirmRequest();
			} else {
				applyStyleSettings(false);
			}
		}
	}
	
	private boolean checkStyleSettings() {
		if(vectorLayerStyleWidget.isEnableLabeling()) {
			if(vectorLayerStyleWidget.getAttributeLabel() == null
					|| vectorLayerStyleWidget.getFontSize() == null) {				
				dialogBuilder.createError(UIMessages.INSTANCE.vlswErrorDialogTitle(), 
						UIMessages.INSTANCE.vlswRequiredLabelDataText()).show();			
				return false;
			}
		}
		
		if(vectorLayerStyleWidget.isEnableTheming()) {
			if(vectorLayerStyleWidget.getAttributeTheming() == null) {
				dialogBuilder.createError(UIMessages.INSTANCE.vlswErrorDialogTitle(), 
						UIMessages.INSTANCE.vlswRequiredThemingDataText()).show();
				return false;
			}
		}
		
		return true;
	}
	
	private void clearFeatureStyleConfirmRequest() {
		ConfirmMessageBox messageBox = dialogBuilder
				.createConfirm(UIMessages.INSTANCE.vlswHeading(), 
						UIMessages.INSTANCE.vlswOverrideFeatureStyle(), 
						ImageProvider.INSTANCE.layerIcon());
								
		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						applyStyleSettings(true);							
					}
				});
		
		messageBox.getButton(PredefinedButton.NO).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {							
						applyStyleSettings(false);
					}
				});

		messageBox.show();
	}
	
	private void applyStyleSettings(final boolean clearFeatureStyle) {
		final VectorLayer selectedLayer = vectorLayerStyleWidget
				.getSelectedLayer();
		
		final ProgressBarDialog progressDialog = new ProgressBarDialog(true,
				UIMessages.INSTANCE.vlswProgressBoxLabel(),
				UIMessages.INSTANCE.vlswProgressText());
		progressDialog.show();

		taskManager.execute(new Runnable() {
			@Override
			public void run() {				
				final VectorFeature[] selectedFeatures = selectedLayer
						.getSelectedFeatures();
						
				if(selectedFeatures != null && selectedFeatures.length > 0) {
					vectorStyleAssistant.applyFeatureStyle(selectedFeatures, selectedLayer);
					unSelectFeatures(selectedFeatures);
				} else {
					if(clearFeatureStyle) {
						clearFeatureStyles();
					}					
					vectorStyleAssistant.applyLayerStyle(selectedLayer);
					
					if(selectedLayer.getVectorStyle().isColorThemingEnabled()
							|| selectedLayer.getVectorStyle().getLabel().isEnabled()) {						
						applyLengendVisibility(selectedLayer);
					} else {
						hideLegendPanel();
					}
				}
								
				selectedLayer.redraw();
				progressDialog.hide();
				
				vectorLayerStyleWidget.asWidget().getElement().<FxElement> cast()
					.fadeToggle();							
			}
		});				
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
	
	private boolean isFeatureStyleMode() {			
		final VectorFeature[] selectedFeatures = vectorLayerStyleWidget
				.getSelectedLayer().getSelectedFeatures();
		
		return selectedFeatures != null && selectedFeatures.length > 0;
	}
		
	private boolean isFeatureStyleApplied() {
		VectorLayer selectedLayer = vectorLayerStyleWidget.getSelectedLayer();
		
		for(VectorFeature feature : selectedLayer.getFeatures()) {
			if(feature.getStyle() != null) {
				return true;
			}
		}
		
		return false;
	}
	
	private void clearFeatureStyles() {
		VectorLayer selectedLayer = vectorLayerStyleWidget.getSelectedLayer();
		for(VectorFeature feature : selectedLayer.getFeatures()) {
			feature.getJSObject().unsetProperty("style");			
		}
	}
	
	private void unSelectFeatures(VectorFeature[] selectedFeatures) {
		SelectFeature selectFeatureControl = new SelectFeature(
				(Vector)vectorLayerStyleWidget.getSelectedLayer());
		geoMap.addControl(selectFeatureControl);
		selectFeatureControl.activate();		
		
		for(VectorFeature feature : selectedFeatures) {
			selectFeatureControl.unSelect(feature);
		}
		
		selectFeatureControl.deactivate();
		geoMap.getMap().removeControl(selectFeatureControl);
	}
}
