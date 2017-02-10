package org.geowe.client.local.model.vector;

import org.geowe.client.local.layermanager.tool.create.vector.source.LayerVectorSource;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

/**
 * Parametros de configuracion para la creacion de una capa vectorial en 
 * cualquiera de sus combinaciones: capa vacia, capa a partir de geodatos,
 * capa a partir de features ya creados, etc.
 * 
 * @author Atanasio Muñoz
 *
 */
public class VectorLayerConfig {
	private static final Projection DEFAUL_PROJECTION = new Projection(GeoMap.INTERNAL_EPSG);
	
	private String layerName;
	private String epsg;
	private String geoDataString;
	private String geoDataFormat;
	private VectorFeature[] features;
	private LayerVectorSource source;

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getEpsg() {
		String projectionName = epsg;
		
		if (epsg == null || epsg.isEmpty()) {
			projectionName = GeoMap.INTERNAL_EPSG;
		}
		
		return projectionName;
	}

	public void setEpsg(String epsg) {
		this.epsg = epsg;
	}
	
	public Projection getProjection() {		
		return new Projection(getEpsg());
	}
	
	public Projection getDefaultProjection() {
		return DEFAUL_PROJECTION;
	}

	public String getGeoDataString() {
		return geoDataString;
	}

	public void setGeoDataString(String geoDataString) {
		this.geoDataString = geoDataString;
	}

	public String getGeoDataFormat() {
		return geoDataFormat;
	}

	public void setGeoDataFormat(String geoDataFormat) {
		this.geoDataFormat = geoDataFormat;
	}

	public VectorFeature[] getFeatures() {
		return features;
	}

	public void setFeatures(VectorFeature[] features) {
		this.features = features;
	}
	
	public void setSource(LayerVectorSource source) {
		this.source = source;
	}
	
	public LayerVectorSource getSource() {
		return source;
	}

	@Override
	public String toString() {
		return "VectorLayerConfig [layerName=" + layerName + ", epsg=" + epsg  
				+ ", geoDataString=" + geoDataString + ", geoDataFormat=" + geoDataFormat + ", numFeatures="
				+ (features == null ? "null" : features.length) + "]";
	}
}
