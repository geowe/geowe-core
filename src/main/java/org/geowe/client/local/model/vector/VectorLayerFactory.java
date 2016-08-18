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
package org.geowe.client.local.model.vector;

import org.geowe.client.local.main.tool.map.catalog.model.VectorLayerDef;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.FormatOptions;
import org.gwtopenmaps.openlayers.client.format.GML;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.KML;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factoria de creacion de capas de manera centralizada en sus distintas
 * modalidades. Cuando se creen capas a partir de datos de partida, estos datos
 * se transformaran siempre al EPSG interno del GeoMap.
 * 
 * @author Atanasio Muñoz
 * @since 18-08-2016
 * @author rafa@geowe.org
 * Fixed issue #150: fail to load geojson with Z coordinate.
 */
public final class VectorLayerFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(VectorLayerFactory.class.getName());
	
	public static final String DEFAULT_KML_LAYER_EPSG = "EPSG:4326";

	private VectorLayerFactory() {
	}

	public static VectorLayer createVectorLayerFromGeoData(
			VectorLayerConfig layerConfig) {

		VectorLayer layer = null;

		switch (layerConfig.getGeoDataFormat()) {
		case (VectorLayerDef.KML):
			layer = createKmlVectorLayer(layerConfig);
			break;
		case (VectorLayerDef.GML):
			layer = createGmlVectorLayer(layerConfig);
			break;
		case (VectorLayerDef.WKT):
			layer = createWktVectorLayer(layerConfig);
			break;
		case (VectorLayerDef.GEOJSON):
			layer = createGeoJsonVectorLayer(layerConfig);
			break;
		default:
			LOG.info("GeoData Format not supported in this version");
			break;
		}

		return layer;
	}
	
	public static VectorLayer createEmptyVectorLayer(final VectorLayerConfig layerConfig) {
		final VectorOptions vectorOptions = new VectorOptions();

		vectorOptions.setStyleMap(layerConfig.getStyleMap());
		vectorOptions.setProjection(layerConfig.getEpsg());

		return new VectorLayer(layerConfig.getLayerName(), vectorOptions);
	}	

	public static VectorLayer createVectorLayer(final VectorLayerConfig layerConfig) {
		final VectorLayer layer = createEmptyVectorLayer(layerConfig);
		
		for (final VectorFeature feature : layerConfig.getFeatures()) {
			feature.getGeometry().transform(layerConfig.getProjection(),
					layerConfig.getDefaultProjection());
			layer.addFeature(feature);
		}

		return layer;
	}

	public static VectorLayer createKmlVectorLayer(final VectorLayerConfig layerConfig) {
		final KML kmlReader = new KML(createFormatOptions(layerConfig));
		
		kmlReader.setExtractStyles(true);
		kmlReader.setExtractAttributes(true);
		kmlReader.getJSObject().setProperty("kvpAttributes", true);
		kmlReader.getJSObject().setProperty("foldersName", (String) null);
		kmlReader.getJSObject().setProperty("foldersDesc", (String) null);

		final VectorFeature[] features = kmlReader.read(layerConfig.getGeoDataString());
		final VectorLayer layer = createEmptyVectorLayer(layerConfig);
		layer.addFeatures(features);

		return layer;
	}

	public static VectorLayer createGmlVectorLayer(VectorLayerConfig layerConfig) {
		GML gmlReader = new GML();
		VectorFeature[] features = gmlReader.read(layerConfig.getGeoDataString());
		layerConfig.setFeatures(features);
		
		return createVectorLayer(layerConfig);
	}

	public static VectorLayer createGeoJsonVectorLayer(VectorLayerConfig layerConfig) {
		GeoJSON geoJsonReader = new GeoJSON();
		geoJsonReader.getJSObject().setProperty("ignoreExtraDims", true);
		VectorFeature[] features = geoJsonReader.read(layerConfig.getGeoDataString());
		layerConfig.setFeatures(features);

		return createVectorLayer(layerConfig);
	}

	public static VectorLayer createWktVectorLayer(VectorLayerConfig layerConfig) {
		WKT wktReader = new WKT();
		VectorFeature[] features = wktReader.read(layerConfig.getGeoDataString());
		layerConfig.setFeatures(features);

		return createVectorLayer(layerConfig);
	}

	public static FormatOptions createFormatOptions(VectorLayerConfig layerConfig) {
		FormatOptions formatOptions = new FormatOptions();
		formatOptions
				.setInternalProjection(layerConfig.getDefaultProjection());
		formatOptions.setExternalProjection(layerConfig.getProjection());

		return formatOptions;
	}
}
