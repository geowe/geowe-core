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
package org.geowe.client.local.main.tool;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.BasicToolBar;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.util.Attributes;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

@ApplicationScoped
public class W3wTool extends ToggleTool {

	public static final String RED = "#FF0000";
	public static final String GREEN = "#00FF00";
	public static final String W3W_LAYER_NAME = "what3words";

	private boolean isActive;

	@Inject
	private BasicToolBar basicToolBar;

	@Inject
	private LayerManagerWidget layerManagerWidget;


	@Inject
	public W3wTool(final GeoMap geoMap) {
		super(UIMessages.INSTANCE.what3Words(), ImageProvider.INSTANCE.w3w24());
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.what3Words(),
				UIMessages.INSTANCE.w3wToolTip(), Side.LEFT));
	}

	@Override
	protected ValueChangeHandler<Boolean> getSelectChangeHandler() {
		return new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					setActive(true);
					basicToolBar.setAnchorVisible(true);
					basicToolBar.setAnchorColor(GREEN);
					basicToolBar.setWhat3Words("");
					createW3wLayer();
				} else {
					setActive(false);
					basicToolBar.setAnchorVisible(false);
					basicToolBar.setAnchorColor(RED);
				}
			}
		};
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean isActive) {
		this.isActive = isActive;
	}

	public void set3Words(final String words) {
		basicToolBar.setWhat3Words(words);
	}

	private void createW3wLayer() {
		if (getW3wLayer() == null) {			
			
			final VectorLayerConfig layerConfig = new VectorLayerConfig();
			layerConfig.setLayerName(W3W_LAYER_NAME);			
			final VectorLayer layer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
			//TODO Adaptar la asignación de estilo usando el VectorStyleDef del VectorLayer,
			//     una vez que se hayan añadido a este modelo las propiedades de graphicOffset
			//     y backgroundGraphic
			layer.setStyleMap(getStyleMap());
			
			LayerLoader.load(layer);
		}
	}

	private VectorLayer getW3wLayer(){
		return (VectorLayer) layerManagerWidget.getVector(W3W_LAYER_NAME);
	}

	public void addElementToW3wLayer(final LonLat lonlat, final String words) {
		createW3wLayer();
		getW3wLayer().addFeature(createVectorFeature(lonlat, words));
	}

	private VectorFeature createVectorFeature(final LonLat lonlat, final String words) {
		final Point point = new Point(lonlat.lon(), lonlat.lat());
		final VectorFeature pointFeature = new VectorFeature(point);

		final Attributes attributes = new Attributes();
		attributes.setAttribute("X", String.valueOf(lonlat.lon()));
		attributes.setAttribute("Y", String.valueOf(lonlat.lat()));
		attributes.setAttribute("w3w", words);
		pointFeature.setAttributes(attributes);

		return pointFeature;
	}

	private StyleMap getStyleMap() {				
		final Style style = createStyle(ImageProvider.INSTANCE.w3wRed24()
				.getSafeUri().asString());

		final Style hoverStyle = createStyle(ImageProvider.INSTANCE.w3wBlue24()
				.getSafeUri().asString());

		final Style selectStyle = createStyle(ImageProvider.INSTANCE.w3wGreen24()
				.getSafeUri().asString());

		return new StyleMap(style, selectStyle, hoverStyle);
	}

	private Style createStyle(final String imageUrl) {
		final Style style = new Style();
		style.setGraphicOpacity(1);
		style.setGraphicSize(24, 30);
		style.setGraphicOffset(-12, -30);
		style.setExternalGraphic(imageUrl);

		style.setBackgroundGraphic(ImageProvider.INSTANCE.w3wShadow()
				.getSafeUri().asString());
		style.setBackgroundOffset(0, -28);
		return style;
	}

	public String getLocale() {
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(locale)) {
			locale = "en";
		}
		return locale;
	}
}
