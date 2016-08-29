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

import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.TMS;
import org.gwtopenmaps.openlayers.client.layer.TMSOptions;
import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Definicion de una capa raster TMS
 * 
 * @author rafa@geowe.org
 *
 */
public class TmsLayerDef extends WmsLayerDef {
	private static final long serialVersionUID = 2520310257932517715L;

	@Override
	public Layer getLayer() {
		// Create TMS layer

		TMSOptions tmsOptions = new TMSOptions();
		tmsOptions.setDisplayOutsideMaxExtent(false);
		tmsOptions.setIsBaseLayer(true);
		tmsOptions.setType("jpg");
		tmsOptions.setGetURL(getMyUrl(getUrl()));

		TMS tms = new TMS(getName(), "", tmsOptions);

		return tms;

	}

	private static native JSObject getMyUrl(String urlBase) /*-{
		function get_my_url(bounds) {
			var res = this.map.getResolution();
			var x = Math.round((bounds.left - this.maxExtent.left)
					/ (res * this.tileSize.w));
			var y = Math.round((this.maxExtent.top - bounds.top)
					/ (res * this.tileSize.h));
			var z = this.map.getZoom();
			var limit = 100000000;
			var i = 0;
			var dir_x = x;
			var dir_y = y;
			for (i = z; i > 9; i--) {
				dir_x = (Math.floor(dir_x / 2.0));
				dir_y = (Math.floor(dir_y / 2.0));
			}
			var path = "9_" + dir_x + "_" + dir_y + "/jpg";
			if (y < 0 || y >= limit) {
				return "http://imageatlas.digitalglobe.com/ia-webapp/img/noImage.gif"
			} else {
				limit = Math.pow(2, z);
				x = ((x % limit) + limit) % limit;
				y = ((y % limit) + limit) % limit;
//				var url = "http://a.tile.opencyclemap.org/cycle/" + z + "/" + x
//						+ "/" + y + ".png";
				var url = urlbase + z + "/" + x + "/" + y + "." + getFormat();
				return url;
			}
		}
		return get_my_url;
	}-*/;

}
