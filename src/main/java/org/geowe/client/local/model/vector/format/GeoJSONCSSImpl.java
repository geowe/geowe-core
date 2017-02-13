package org.geowe.client.local.model.vector.format;

import org.gwtopenmaps.openlayers.client.util.JSObject;

public class GeoJSONCSSImpl {
	
	public static native JSObject create()
    /*-{
            return new $wnd.OpenLayers.Format.GeoJSONCSS();
    }-*/;

}
