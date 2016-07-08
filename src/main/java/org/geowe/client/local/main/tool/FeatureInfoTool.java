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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.edition.DeleteFeatureListenerManager;
import org.geowe.client.local.main.tool.featureinfo.FeatureAttributeBean;
import org.geowe.client.local.main.tool.featureinfo.FeatureInfoDialog;
import org.geowe.client.local.main.tool.info.DeleteFeatureListener;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.UnselectFeatureListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class FeatureInfoTool extends ToggleTool implements
		DeleteFeatureListener {

	@Inject
	private MapControlFactory mapControlFactory;
	@Inject
	private FeatureInfoDialog infoDialog;
	@Inject
	private DeleteFeatureListenerManager deleteFeatureListenerManager;

	private SelectFeature clickSelectFeature;

	@Inject
	public FeatureInfoTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.featureInfoToolText(), ImageProvider.INSTANCE
				.edit32(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.featureInfoToolText(),
				UIMessages.INSTANCE.featureInfoToolTip(), Side.TOP));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {
		this.clickSelectFeature = mapControlFactory.createClickControl(
				new Vector("Empty"), getUnselectListener(), getClickEvent());
		add(mapControlFactory.createSelectHover(new Vector("Empty")));
		add(clickSelectFeature);
		addDialogButtonHandler();
		deleteFeatureListenerManager.addDeleteFeatureListener(this);
	}

	private void addDialogButtonHandler() {
		infoDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						editFeature(infoDialog.getVectorFeature(),
								infoDialog.getFeatureAttributes());
					}
				});
	}

	private void editFeature(VectorFeature feature,
			List<FeatureAttributeBean> attributes) {
		
		for (FeatureAttributeBean attribute : attributes) {
			feature.getAttributes().setAttribute(
					attribute.getAttributeName(), attribute.getAttributeValue());			
		}
	}

	private UnselectFeatureListener getUnselectListener() {
		return new UnselectFeatureListener() {

			@Override
			public void onFeatureUnselected(VectorFeature vectorFeature) {
				if (infoDialog != null && infoDialog.isVisible()) {
					infoDialog.hide();
				}
			}
		};
	}

	private ClickFeatureListener getClickEvent() {
		return new ClickFeatureListener() {

			@Override
			public void onFeatureClicked(VectorFeature vectorFeature) {
				clickSelectFeature.select(vectorFeature);
				clickSelectFeature.unselectAll(vectorFeature);

				infoDialog.setVectorFeature((VectorLayer) getLayer(),
						vectorFeature);
				infoDialog.show();
			}
		};
	}

	@Override
	public void onDeleteFeature(VectorFeature vectorFeature) {
		if (infoDialog != null && infoDialog.isVisible()) {
			infoDialog.hide();
		}
	}
}