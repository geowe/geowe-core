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
package org.geowe.client.local.main.tool.layer;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.export.VectorFormat;
import org.geowe.client.local.layermanager.tool.export.exporter.Exporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.layermanager.tool.export.exporter.FileParameter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Responsible to save selected layer to a GeoJSON file with WGS84 Projection
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class SaveLayerTool extends ButtonTool {

	@Inject
	private Logger logger;

	@Inject
	private LayerManagerWidget layerMangerWidget;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private Exporter exporter;
	private FileParameter fileParameter;

	@Inject
	public SaveLayerTool(GeoMap geoMap) {
		super(UIMessages.INSTANCE.download(), ImageProvider.INSTANCE
				.fileDownload24());
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.save(),
				UIMessages.INSTANCE.quickSaveToolTipText(), Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {
		exporter = new FileExporter();
		fileParameter = new FileParameter();
		layer = (Vector) layerMangerWidget
				.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
		if (layer == null) {
			messageDialogBuilder.createWarning(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.edtAlertMessageBoxLabel()).show();
		}
		fileParameter.setFileName(layer.getName() + "_WGS84");
		fileParameter.setExtension(VectorFormat.GEO_JSON_FORMAT.getName()
				.toLowerCase());
		fileParameter.setContent(getContentAsGeoJSON());
		export();
	}

	private String getContentAsGeoJSON() {
		org.gwtopenmaps.openlayers.client.format.VectorFormat format = new GeoJSON();

		return format.write(getTransformedFeatures());
	}

	private VectorFeature[] getTransformedFeatures() {
		List<VectorFeature> transformedFeatures = new ArrayList<VectorFeature>();
		if (layer.getFeatures() != null) {
			logger.info("N. features de la Capa: " + layer.getFeatures().length);
			for (VectorFeature feature : layer.getFeatures()) {
				VectorFeature featureToExport = feature.clone();
				featureToExport.getGeometry().transform(
						new Projection(GeoMap.INTERNAL_EPSG),
						new Projection("WGS84"));
				transformedFeatures.add(featureToExport);
			}
		}
		VectorFeature[] transArray = new VectorFeature[transformedFeatures
				.size()];
		return transformedFeatures.toArray(transArray);
	}

	private void export() {
		taskManager.execute(new Runnable() {
			@Override
			public void run() {
				exporter.export(fileParameter);
			}
		});
	}

}
