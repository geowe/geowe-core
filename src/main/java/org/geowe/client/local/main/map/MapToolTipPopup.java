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
package org.geowe.client.local.main.map;

import org.geowe.client.local.main.AnchorBuilder;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.model.vector.FeatureSchema;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.popup.FramedCloud;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;

/**
 * Pop-up que se muestra al activar la opción de vista rápida (o Map Tooltip), cuando se desplaza
 * el puntero sobre una feature de la capa seleccionada (evento hover). En este pop-up se muestran
 * aquellos atributos configurados para ello en el dialogo de edición del esquema de la capa con
 * la propiedad showInTooltip = true.
 * 
 * @author Atanasio Muñoz
 *
 */
public class MapToolTipPopup extends FramedCloud {
	private static final int MAX_VALUE_LENGHT = 35;

	private VectorLayer layer;
	private VectorFeature feature;
	
	public MapToolTipPopup(final VectorLayer layer, final VectorFeature feature) {
		super(FeatureSchema.getFeatureId(feature), feature.getCenterLonLat(), 
				null, null, null, false);
		
		this.layer = layer;
		this.feature = feature;
		updateContent();	
		setPanMapIfOutOfView(true);
		setAutoSize(true);
		setOpacity(0.8);
	}
	
	public void setFeature(final VectorLayer layer, final VectorFeature feature) {
		this.layer = layer;
		this.feature = feature;
		updateContent();
	}
	
	private void updateContent() {
		setLonLat(feature.getCenterLonLat());
		setContentHTML(getHtml());
	}

	private String getHtml() {
		final StringBuffer htmlString = new StringBuffer();
		final String title = this.layer.getName(); 
		final String subtitle = FeatureSchema.getFeatureId(feature);
				
		htmlString.append("<H1>" + title + "</H1>");
		htmlString.append("<BR>");
		htmlString.append("<H2>" + subtitle + "</H2>");
		htmlString.append("<BR><BR>");
				
		for (final FeatureAttributeDef attribute : layer.getSchema().getAttributes()) {
			if (attribute.isShowInTooltip()) {
				addAttribute(htmlString, attribute);
			}
		}
				
		return htmlString.toString();
	}
	
	private void addAttribute(final StringBuffer htmlString, final FeatureAttributeDef attribute) {
		htmlString.append("<b>" + attribute.getName() + ":</b>&nbsp");
		final String attributeValue = feature.getAttributes()
				.getAttributeAsString(attribute.getName());
	
		if (attributeValue != null) {
			if (attributeValue.startsWith("img:")) {
				addImageAttribute(htmlString, attributeValue);
			} else if (attributeValue.startsWith("http")) {
				addHyperlinkAttribute(htmlString, attributeValue);
			} else {
				addPlainAttribute(htmlString, attributeValue);
			}	
		}
		
		htmlString.append("<BR>");
	}
	
	private void addPlainAttribute(final StringBuffer htmlString, final String attributeValue) {
		htmlString.append(getShortenedValue(attributeValue));
	}
	
	private void addHyperlinkAttribute(final StringBuffer htmlString, final String attributeValue) {
		final Anchor anchor = new AnchorBuilder()
				.setText(getShortenedValue(attributeValue))
				.setHref(attributeValue)
				.setTitle(UIMessages.INSTANCE.openInNewWindow()).build();

		htmlString.append(anchor.getElement().getString());
	}
	
	private void addImageAttribute(final StringBuffer htmlString, final String attributeValue) {
		final String url = attributeValue.replace("img:", "");
		final Image image = new Image(url);
		image.setSize("48px", "48px");
		
		final Anchor anchor = new AnchorBuilder().setHref(url)
				.setTitle(UIMessages.INSTANCE.openInNewWindow())
				.setImage(image).build();

		htmlString.append(anchor.getElement().getString());
	}
	
	private String getShortenedValue(final String attributeValue) {
		if(attributeValue.length() > MAX_VALUE_LENGHT) {
			return attributeValue.substring(0, (MAX_VALUE_LENGHT - 1)) + "...";
		} else {
			return attributeValue;
		}		
	}
}