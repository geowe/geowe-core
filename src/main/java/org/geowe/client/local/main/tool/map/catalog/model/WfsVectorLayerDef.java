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

import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.Projection;
import org.jboss.errai.ioc.client.container.IOC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gwt.http.client.URL;
/**
 * Definicion de una capa vectorial de tipo WFS, es decir, que se construye en
 * base a una llamada a un servidor WFS.
 * 
 * @author Atanasio Muñoz
 * @since 18-10-2016
 * @author rafa@geowe.org
 *
 */
public class WfsVectorLayerDef extends URLVectorLayerDef {
	private static final Logger LOG = LoggerFactory
			.getLogger(WfsVectorLayerDef.class.getName());

	private static final long serialVersionUID = -7573399082120677088L;

	private String serviceUrl;
	private String nameSpaceFeatureType;
	private String version;
	private int maxFeatures = 0;
	private Bounds bbox;
	private boolean queryBbox = false;
	private String cql;
	
	@Override
	public void load() {
		setUrl(createWfsUrl());
		createLayerFromURL();
	}

	private String createWfsUrl() {
		StringBuffer url = new StringBuffer(serviceUrl);
		url.append("?request=GetFeature");
		url.append("&service=WFS");
		url.append("&version=" + version);
		url.append("&typeName=" + nameSpaceFeatureType);
		if (maxFeatures != 0) {
			url.append(getMaxFeaturesLimit());
		}
		if (!getFormat().isEmpty()) {
			url.append(getOutputFormat());
		}
		if (queryBbox) {
			url.append("&srsName=" + getEpsg());
			url.append("&bbox=" + bbox.getLowerLeftX() + ","
					+ bbox.getLowerLeftY() + "," + bbox.getUpperRightX() + ","
					+ bbox.getUpperRightY());
		} else {
			url.append("&CQL_FILTER=" + URL.encodeQueryString(cql));
		}
		return url.toString();
	}

	private String getMaxFeaturesLimit() {
		String maxFeaturesParam = "";

		if ("2.0.0".equals(version)) {
			maxFeaturesParam = "&count=" + maxFeatures;
		} else {
			maxFeaturesParam = "&maxFeatures=" + maxFeatures;
		}
		return maxFeaturesParam;
	}

	private String getOutputFormat() {
		String outputFormat = "";

		switch (getFormat()) {
		case GEOJSON:
			outputFormat = "&outputFormat=application/json";
			break;
		case KML:
			outputFormat = "&outputFormat=application/vnd.google-earth.kml+xml";
			break;
		default:
			break;
		}
		LOG.info(getFormat() + ": " + outputFormat);
		return outputFormat;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String url) {
		this.serviceUrl = url;
	}

	public String getNameSpaceFeatureType() {
		return nameSpaceFeatureType;
	}

	public void setNameSpaceFeatureType(String nameSpaceFeatureType) {
		this.nameSpaceFeatureType = nameSpaceFeatureType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getMaxFeatures() {
		return maxFeatures;
	}

	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	public Bounds getBbox() {
		return bbox;
	}

	public void setBbox(Bounds bbox) {
		this.bbox = bbox;
	}

	public void generateBbox() {
		queryBbox = true;
		GeoMap geoMap = IOC.getBeanManager().lookupBean(GeoMap.class)
				.getInstance();
		if (GeoMap.INTERNAL_EPSG.equals(getEpsg())) {
			this.bbox = geoMap.getMap().getExtent();
		} else {
			this.bbox = geoMap.getMap().getExtent()
					.transform(new Projection(GeoMap.INTERNAL_EPSG),
							new Projection(getEpsg()));
		}
	}

	public String getCql() {
		return cql;
	}

	public void setCql(String cql) {
		this.cql = cql;
	}
}
