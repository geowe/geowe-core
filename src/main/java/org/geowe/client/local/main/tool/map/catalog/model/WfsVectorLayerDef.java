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
package org.geowe.client.local.main.tool.map.catalog.model;

import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.style.StyleFactory;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocol;
import org.gwtopenmaps.openlayers.client.protocol.WFSProtocolOptions;
import org.gwtopenmaps.openlayers.client.strategy.BBoxStrategy;
import org.gwtopenmaps.openlayers.client.strategy.Strategy;

/**
 * Definicion de una capa vectorial de tipo WFS, es decir, que se 
 * construye en base a una llamada a un servidor WFS.
 * 
 * @author Atanasio Muñoz
 *
 */
public class WfsVectorLayerDef extends VectorLayerDef {
	private static final long serialVersionUID = -5511682522394517996L;

	private String url;
	private String nameSpace;
	private String featureType; //WFS laye name
	private String geometryColumn;
	private String version;
	
	@Override
	public Layer getLayer() {
				
		WFSProtocolOptions wfsProtocolOptions = new WFSProtocolOptions();
		wfsProtocolOptions.setUrl(url);
		wfsProtocolOptions.setFeatureType(featureType);
		wfsProtocolOptions.setFeatureNameSpace(nameSpace);				
		wfsProtocolOptions.setSrsName(getEpsg());
		
		if(geometryColumn != null) {
			wfsProtocolOptions.setGeometryName(geometryColumn);
		}
		if(version != null) {
			wfsProtocolOptions.setVersion(version);
		}			
		WFSProtocol wfsProtocol = new WFSProtocol(wfsProtocolOptions);
		
		VectorOptions vectorOptions = new VectorOptions();
        vectorOptions.setProtocol(wfsProtocol);
        vectorOptions.setStrategies(new Strategy[]{new BBoxStrategy()});
        vectorOptions.setProjection(getEpsg());
        vectorOptions.setStyleMap(StyleFactory.createDefaultStyleMap());
                
        final VectorLayer layer = new VectorLayer(getName(), vectorOptions);        
        		
		return layer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getGeometryColumn() {
		return geometryColumn;
	}

	public void setGeometryColumn(String geometryColumn) {
		this.geometryColumn = geometryColumn;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}