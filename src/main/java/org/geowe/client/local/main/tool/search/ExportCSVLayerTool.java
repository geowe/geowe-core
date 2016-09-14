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
package org.geowe.client.local.main.tool.search;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.layermanager.tool.create.CSV;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.info.FeatureTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.resources.client.ImageResource;

/**
 * Herramienta para generar un csv de los elementos. seleccionados.
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class ExportCSVLayerTool extends LayerTool implements FeatureTool {

	private VectorFeature selectedFeature;
	private List<VectorFeature> selectedFeatures;
	private VectorLayer layer;

	@Inject
	public ExportCSVLayerTool(LayerManagerWidget layerManagerWidget,
			GeoMap geoMap) {
		super(layerManagerWidget, geoMap);
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.exportCsvButtonText();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.fileText24();
	}

	public VectorFeature getSelectedFeature() {
		return selectedFeature;
	}

	@Override
	public void setSelectedFeature(VectorFeature selectedFeature) {
		this.selectedFeature = selectedFeature;
		this.selectedFeatures = new ArrayList<VectorFeature>();
		this.selectedFeatures.add(selectedFeature);
		setEnabled(selectedFeature != null);
	}

	@Override
	public void setSelectedFeatures(List<VectorFeature> selectedFeatures) {
		this.selectedFeatures = selectedFeatures;
	}

	public void setLayer(VectorLayer layer) {
		this.layer = layer;
	}

	@Override
	public void setSelectedLayer(VectorLayer layer) {
		this.layer = layer;
	}

	@Override
	public void onClick() {
		if (selectedFeatures != null) {
			FileExporter.saveAs(exportCSV(selectedFeatures), layer.getName()
					+ ".csv");
		}
	}

	public String exportCSV(List<VectorFeature> selectedFeatures) {
		return new CSV(layer.getProjection().getProjectionCode()).write(selectedFeatures);
	}

}
