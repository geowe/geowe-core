package org.geowe.client.local.model.vector.format;

import org.geowe.client.local.main.tool.project.StyleProjectLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.GeoJSON;
import org.gwtopenmaps.openlayers.client.util.JSObject;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;



//http://leafletjs.com/reference-1.0.2.html#path-option
public class GeoJSONCSS extends GeoJSON {
	private StyleProjectLayer style;

	public void setStyle(StyleProjectLayer style) {
		this.style = style;
	}

	public GeoJSONCSS() {
		super(null);
	}

	protected GeoJSONCSS(JSObject element) {
		super(element);
	}

	public String write(VectorFeature[] vectorFeatures) {
		GeoJSON geoJSONObject = new GeoJSON();
		String geojson = geoJSONObject.write(vectorFeatures);

		final JSONValue jsonValue = JSONParser.parseLenient(geojson);
		final JSONObject geoJSONCssObject = jsonValue.isObject();

		geoJSONCssObject.put("style", style.getLeafletJSONObject());
		return geoJSONCssObject.toString();
	}

	public StyleProjectLayer getStyle(String geoDataString) {
		final JSONValue jsonValue = JSONParser.parseLenient(geoDataString);
		final JSONObject geoJSONCssObject = jsonValue.isObject();
		JSONObject styleObject = geoJSONCssObject.get("style").isObject();

		String fillColor = styleObject.get("fillColor").isString()
				.stringValue();
		Double fillOpacity = styleObject.get("fill-opacity").isNumber()
				.doubleValue();
		String strokeColor = styleObject.get("color").isString().stringValue();
		Double strokeWidth = styleObject.get("weight").isNumber().doubleValue();
		return new StyleProjectLayer(fillColor, fillOpacity, strokeColor,
				strokeWidth);
	}
}
