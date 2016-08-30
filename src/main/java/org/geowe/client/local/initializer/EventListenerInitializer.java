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
package org.geowe.client.local.initializer;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.export.ExportDataTool;
import org.geowe.client.local.main.StatusPanelWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.edition.CopyElementTool;
import org.geowe.client.local.main.tool.edition.UnionTool;
import org.geowe.client.local.main.tool.info.LayerInfoDialog;
import org.geowe.client.local.main.tool.info.RasterInfoDialog;
import org.geowe.client.local.main.tool.info.WmsGetInfoTool;
import org.geowe.client.local.main.tool.layer.LayerInfoTool;
import org.geowe.client.local.main.tool.layer.SearchAttributeTool;
import org.geowe.client.local.main.tool.spatial.BufferTool;
import org.geowe.client.local.main.tool.spatial.CentroidTool;
import org.geowe.client.local.main.tool.spatial.EnvelopeTool;
import org.geowe.client.local.main.tool.spatial.GeometryValidationTool;
import org.geowe.client.local.main.tool.spatial.GeoprocessingTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.style.VectorLayerStyleWidget;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.inject.Inject;

@ApplicationScoped
public class EventListenerInitializer {

	@Inject
	private GeoMap geoMap;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private VectorLayerStyleWidget vectorLayerStyleWidget;
	@Inject
	private StatusPanelWidget stateBarWidget;
	@Inject
	private LayerInfoTool layerInfoTool;
	@Inject
	private UnionTool unionTool;
	@Inject
	private SearchAttributeTool searchAttributeTool;
	@Inject
	private GeoprocessingTool geometricAnalisysTool;
	@Inject
	private CopyElementTool copyElementTool;
	@Inject
	private ExportDataTool exportDataTool;
	@Inject
	private EnvelopeTool envelopeTool;
	@Inject
	private CentroidTool centroidTool;
	@Inject
	private BufferTool bufferTool;
	@Inject
	private GeometryValidationTool geometricValidationTool;
	@Inject
	private LayerInfoDialog layerInfoDialog;
	@Inject
	private RasterInfoDialog rasterInfoDialog;
	@Inject
	private WmsGetInfoTool wmsGetInfoTool;

	public void initialize() {

		addChangelayerListener();

		addRemoveLayerListener();

		addAddLayerListener();
		
		addChangeSelectedWMSLayerListener();

		Window.addWindowClosingHandler(new Window.ClosingHandler() {
			@Override
			public void onWindowClosing(final ClosingEvent event) {
				event.setMessage(UIMessages.INSTANCE.leaveWebText());
			}
		});
	}

	private void addChangelayerListener() {
		layerManagerWidget.addChangeLayerListener(stateBarWidget);
		layerManagerWidget.addChangeLayerListener(vectorLayerStyleWidget);
		layerManagerWidget.addChangeLayerListener(rasterInfoDialog);
		layerManagerWidget.addChangeLayerListener(layerInfoDialog);
		layerManagerWidget.addChangeLayerListener(geoMap);
		layerManagerWidget.addChangeLayerListener(layerInfoTool);
		layerManagerWidget.addChangeLayerListener(unionTool);
		layerManagerWidget.addChangeLayerListener(searchAttributeTool);
		layerManagerWidget.addChangeLayerListener(geometricAnalisysTool);
		layerManagerWidget.addChangeLayerListener(copyElementTool);
		layerManagerWidget.addChangeLayerListener(exportDataTool);
		layerManagerWidget.addChangeLayerListener(envelopeTool);
		layerManagerWidget.addChangeLayerListener(centroidTool);
		layerManagerWidget.addChangeLayerListener(bufferTool);
		layerManagerWidget.addChangeLayerListener(geometricValidationTool);						
	}
	
	private void addChangeSelectedWMSLayerListener() {
		layerManagerWidget.addChangeWMSLayerListener(wmsGetInfoTool);
	}

	private void addRemoveLayerListener() {
		layerManagerWidget.addRemoveLayerListener(stateBarWidget);
		layerManagerWidget.addRemoveLayerListener(layerInfoDialog);
		layerManagerWidget.addRemoveLayerListener(rasterInfoDialog);
	}

	private void addAddLayerListener() {
		layerManagerWidget.addAddLayerListener(stateBarWidget);
	}
}