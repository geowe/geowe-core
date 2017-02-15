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


package org.geowe.client.local.model.vector.format;

import org.gwtopenmaps.openlayers.client.util.JSObject;

/**
 * Wrapper del formato GeoJSON CSS en JS
 * 
 * http://leafletjs.com/reference-1.0.2.html#path-option
 * 
 * @author jose@geowe.org
 */

public class GeoJSONCSSImpl {
	
	public static native JSObject create()
    /*-{
            return new $wnd.OpenLayers.Format.GeoJSONCSS();
    }-*/;

}
