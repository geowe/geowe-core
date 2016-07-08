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
package org.geowe.client.local.main.tool.edition;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.map.MapControlFactory;
import org.geowe.client.local.main.tool.ToggleTool;
import org.geowe.client.local.main.tool.info.DeleteFeatureListener;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.UnselectFeatureListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.slf4j.Logger;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Delete element tool
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class DeleteTool extends ToggleTool {

	@Inject
	private Logger logger;
	@Inject
	private MapControlFactory mapControlFactory;
	@Inject
	private DeleteFeatureListenerManager deleteFeatureListenerManager;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	private final ProgressBarDialog autoMessageBox = new ProgressBarDialog(
			false, UIMessages.INSTANCE.processing());
	private SelectFeature clickSelectFeature;

	@Inject
	public DeleteTool(GeoMap geoMap, LayerManagerWidget layerManager) {
		super(UIMessages.INSTANCE.deleteToolText(), ImageProvider.INSTANCE
				.deleteVector(), geoMap, layerManager);
		setToolTipConfig(createTooltipConfig(
				UIMessages.INSTANCE.deleteToolText(),
				UIMessages.INSTANCE.deleteToolTip(), Side.LEFT));
		setEnabled(false);
	}

	@PostConstruct
	private void initialize() {
		this.clickSelectFeature = mapControlFactory.createClickControl(
				new Vector("Empty"), getUnselectListener(), getClickEvent());
		add(mapControlFactory.createSelectHover(new Vector("Empty")));
		add(clickSelectFeature);
		addValueChangeHandler(getMultiDeletionHandler());
	}

	private ValueChangeHandler<Boolean> getMultiDeletionHandler() {
		return new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					VectorLayer layer = (VectorLayer) getLayer();

					if (layer.getSelectedFeatures() != null) {
						confirm(Arrays.asList(layer.getSelectedFeatures()));
					}
				}
			}
		};
	}

	private UnselectFeatureListener getUnselectListener() {
		return new UnselectFeatureListener() {
			@Override
			public void onFeatureUnselected(VectorFeature vectorFeature) {
				logger.info("Feature unselected");
			}
		};
	}

	private ClickFeatureListener getClickEvent() {
		return new ClickFeatureListener() {
			@Override
			public void onFeatureClicked(VectorFeature vectorFeature) {
				clickSelectFeature.select(vectorFeature);
				confirm(Arrays.asList(vectorFeature));
			}
		};
	}

	private void confirm(final List<VectorFeature> vectorFeatures) {
		ConfirmMessageBox messageBox = messageDialogBuilder.createConfirm(
				UIMessages.INSTANCE.dtMessageBoxTitle(),
				UIMessages.INSTANCE.dtMessageBoxLabel(),
				ImageProvider.INSTANCE.deleteVector24());

		messageBox.getButton(PredefinedButton.YES).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {

						autoMessageBox.show();
						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								for (VectorFeature vectorFeature : vectorFeatures) {
									vectorFeature.destroy();
									notifyListener(vectorFeature);
								}
								autoMessageBox.hide();
							}
						});
					}

					private void notifyListener(VectorFeature vectorFeature) {
						for (DeleteFeatureListener listener : deleteFeatureListenerManager
								.getlisteners()) {
							listener.onDeleteFeature(vectorFeature);
						}
					}
				});
		messageBox.show();
	}
}
