package org.geowe.client.local.model.vector.format;

import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.format.VectorFormat;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class GeoJSONCSS extends VectorFormat{

	public GeoJSONCSS() {
		super(null);
	}
	
	protected GeoJSONCSS(JSObject element) {
		super(element);
		// TODO Auto-generated constructor stub
	}
	
	
	public String write(VectorFeature[] vectorFeatures)
    {
		GeoJSON geoJSONObject = new GeoJSON();
		String geojson = geoJSONObject.write(vectorFeatures);	
		
		final JSONValue jsonValue = JSONParser.parseLenient(geojson);
		final JSONObject geoJSONCssObject = jsonValue.isObject();	
		
		JSONObject styleObject = new JSONObject();	
		styleObject.put("fill", new JSONString("red"));
		styleObject.put("stroke-width", new JSONNumber(6));		
		styleObject.put("stroke", new JSONString("blue"));
		styleObject.put("fill-opacity", new JSONNumber(0.6));
				
		geoJSONCssObject.put("style", styleObject);
		return geoJSONCssObject.toString();
        

        
    }
}
