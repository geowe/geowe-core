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

import org.geowe.client.local.initializer.GeoMapInitializer;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.TMS;
import org.gwtopenmaps.openlayers.client.layer.TMSOptions;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Definicion de una capa raster TMS
 * 
 * @author rafa@geowe.org
 * 
 * Se modifica la creación de la capa TMS. Se añade proyección, niveles de zoom, se cambia la función nativa para obtener la url, se especifica de
 * manera dinámica el formato, etc...La url del servicio TMS y el nombre de la capa se indican en el constructor de la capa TMS.
 * @since 30/08/2016
 * @author jose@geowe.org
 */
public class TmsLayerDef extends WmsLayerDef {
	private static final long serialVersionUID = 2520310257932517715L;

	@Override
	public Layer getLayer() {

		TMSOptions tmsOptions = new TMSOptions();
		tmsOptions.setDisplayOutsideMaxExtent(true);
		tmsOptions.setNumZoomLevels(GeoMapInitializer.MAX_NUM_ZOOM_LEVEL);
		tmsOptions.setProjection(GeoMap.INTERNAL_EPSG);
		tmsOptions.setTransitionEffect(TransitionEffect.RESIZE);
		tmsOptions.setIsBaseLayer(false);
		tmsOptions.setType(getFormat());
		tmsOptions.setGetURL(getMyUrl());

		TMS tms = new TMS(getName(), getUrl(), tmsOptions);

		return tms;

	}

	private static native JSObject getMyUrl() /*-{
		function get_my_url(bounds) {
			var res = this.map.getResolution();
			var x = Math.round((bounds.left - this.maxExtent.left)
					/ (res * this.tileSize.w));
			var y = Math.round((this.maxExtent.top - bounds.top)
					/ (res * this.tileSize.h));
			var z = this.map.getZoom();
			var limit = Math.pow(2, z);
			if (y < 0 || y >= limit) {
				return null;
			} else {
				x = ((x % limit) + limit) % limit;
				url = this.url;
				path = z + "/" + x + "/" + y + "." + this.type;
				if (url instanceof Array) {
					url = this.selectUrl(path, url);
				}
				return url + path;
			}
		}

		return get_my_url;
	}-*/;
}