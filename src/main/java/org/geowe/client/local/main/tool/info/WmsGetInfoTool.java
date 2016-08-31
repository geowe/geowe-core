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
package org.geowe.client.local.main.tool.info;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.ChangeSelectedWMSLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfo;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfoOptions;
import org.gwtopenmaps.openlayers.client.event.GetFeatureInfoListener;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.WMS;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

/**
 * Get WMS layer Info
 * 
 * @author geowe
 *  
 * @since 30/08/2016
 * @author jose@geowe.org
 * Se crea el control WmsGetInfo en función de capa WMS seleccionada en el árbol de capas
 * 
 */
@ApplicationScoped
public class WmsGetInfoTool extends ToggleTool implements ChangeSelectedWMSLayerListener{
	
	@Inject
	public WmsGetInfoTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.wmsInfo(),
				ImageProvider.INSTANCE.layerIcon(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.wmsInfo(),
				UIMessages.INSTANCE.wmsInfoToolTip(), Side.LEFT));
	}

	@PostConstruct
	private void initialize() {		
		add(WMSGetFeatureInfo(null));		
	}

	private GetFeatureInfoListener getFeatureInfoListener() {

		return new GetFeatureInfoListener() {
			@Override
			public void onGetFeatureInfo(final GetFeatureInfoEvent eventObject) {

				final ProgressBarDialog progressBar = new ProgressBarDialog(
						false, UIMessages.INSTANCE.processing());
				progressBar.show();
				final HTML html = new HTML(eventObject.getText());
				progressBar.hide();
				showDialog(html);
			}
		};
	}
	
	
	public Control WMSGetFeatureInfo(final WMS layer) {
				
		final WMSGetFeatureInfoOptions wmsGetFeatureInfoOptions = new WMSGetFeatureInfoOptions();
		wmsGetFeatureInfoOptions.setMaxFeaturess(50);		
		wmsGetFeatureInfoOptions.setDrillDown(true);
		if(layer != null) {
			final List<WMS> layers = new ArrayList<WMS>();
			layers.add(layer);
			wmsGetFeatureInfoOptions.setTitle(layer.getName());			
			wmsGetFeatureInfoOptions.setLayers(layers.toArray(new WMS[]{}));					
		}
		
		
		final WMSGetFeatureInfo wmsGetFeatureInfo = new WMSGetFeatureInfo(
				wmsGetFeatureInfoOptions);

		wmsGetFeatureInfo.addGetFeatureListener(getFeatureInfoListener());
		return wmsGetFeatureInfo;
		
	}	

	private void showDialog(final HTML info) {
		final Dialog simple = new Dialog();
		simple.setHeadingText(UIMessages.INSTANCE.wmsInfo());
		HorizontalLayoutContainer container = new HorizontalLayoutContainer();
		container.setScrollMode(ScrollMode.AUTO);
		container.setSize("280px", "180px");

		container.add(replaceHref(info));
		simple.setSize("300px", "200px");
		simple.setResizable(true);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.CLOSE);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");

		simple.add(container);

		simple.show();
	}

	private HTML replaceHref(final HTML html) {
		return new HTML(html.getHTML().replace("<a", "<a target=\"_blank\" "));
	}
	
	@Override
	public void setLayer(final Layer layer) {
		//Se anula para esta herramienta
	}
	

	@Override
	public void onChange(final WMS layer) {
		setWMSLayer(layer);		
	}
}