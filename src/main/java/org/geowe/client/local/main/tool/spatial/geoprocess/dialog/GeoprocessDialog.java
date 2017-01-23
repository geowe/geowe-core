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
package org.geowe.client.local.main.tool.spatial.geoprocess.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.VectorLayerInfo;
import org.geowe.client.local.main.VectorLayerProperties;
import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.main.tool.spatial.geoprocess.BufferGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.Geoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.IGeoprocess;
import org.geowe.client.local.main.tool.spatial.geoprocess.IInputGeoprocess;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * GeoprocessDialog representa al diálogo de análisis espacial encargado de
 * realizar las operaciones geoprocesamiento.
 * 
 * @author jose@geowe.org
 * @author rafa@geowe.org
 * @since 19-08-2016 fixed issue #127:
 *        https://github.com/geowe/geowe-core/issues/127
 */
@ApplicationScoped
public class GeoprocessDialog extends Dialog {
	private static final VectorLayerProperties LAYER_PROPERTIES = GWT
			.create(VectorLayerProperties.class);	
	private static final ListStore<VectorLayerInfo> LAYER_STORE_1 = new ListStore<VectorLayerInfo>(
			LAYER_PROPERTIES.key());
	private static final ComboBox<VectorLayerInfo> LAYER_COMBO_1 = new ComboBox<VectorLayerInfo>(
			LAYER_STORE_1, LAYER_PROPERTIES.name());
	private static final ListStore<VectorLayerInfo> LAYER_STORE_2 = new ListStore<VectorLayerInfo>(
			LAYER_PROPERTIES.key());
	private static final ComboBox<VectorLayerInfo> LAYER_COMBO_2 = new ComboBox<VectorLayerInfo>(
			LAYER_STORE_2, LAYER_PROPERTIES.name());
	private static final TextButton VALIDATE_LAYER_1_BUTTON = new TextButton(
			UIMessages.INSTANCE.validationToolText(),
			ImageProvider.INSTANCE.validation16());
	private static final TextButton VALIDATE_LAYER_2_BUTTON = new TextButton(
			UIMessages.INSTANCE.validationToolText(),
			ImageProvider.INSTANCE.validation16());
	private static final String WIDTH_COMBO_LAYER = "250px";
	private static final String WIDTH_DISTANCE_FIELD = "120px";
	private static final String WIDTH_SPATIAL_OPERATION_COMBO = "140px";
	private static final String COMBO1_ID = "1";
	private static final String COMBO2_ID = "2";
	private VerticalLayoutContainer panel;
	private GeoprocessComboBox spatialOperationComboBox;
	private TextField distanceTextField;
	private List<VectorLayerInfo> layers;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private IInputGeoprocess inputGeoprocess;
	@Inject
	private GeoprocessHelpDialog geoprocessHelpDialog;

	@Inject
	public GeoprocessDialog(final Geoprocesses spatialOperation) {
		super();
		this.setHeadingText(UIMessages.INSTANCE.sodHeadingText());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(500, 370);
		this.setModal(false);
		this.setResizable(false);
		add(createPanel(spatialOperation));
		this.spatialOperationComboBox.setValue(null);
		getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						GeoprocessDialog.this.hide();
						geoprocessHelpDialog.hide();
					}
				});
		
		final ToolButton helpToolButton = new ToolButton(ToolButton.QUESTION);
		setHelpToolTip(helpToolButton);
		addHelpSelectHandler(helpToolButton);

		getHeader().addTool(helpToolButton);

	}
	
	private void setHelpToolTip(final ToolButton tButton) {
		final ToolTipConfig helpToolTip = new ToolTipConfig();
		helpToolTip.setTitleText(HelpMessages.INSTANCE.help());
		tButton.setToolTipConfig(helpToolTip);
	}
	
	private void addHelpSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				geoprocessHelpDialog.show();
			}
		});
	}
	
	public VectorLayer getLayer1() {
		VectorLayer layer = null;
		if (LAYER_COMBO_1.getValue() != null) {
			layer = (VectorLayer) LAYER_COMBO_1.getValue().getLayer();
		}

		return layer;
	}
	
	public VectorLayer getLayer2() {
		VectorLayer layer = null;
		if (LAYER_COMBO_2.getValue() != null) {
			layer = (VectorLayer) LAYER_COMBO_2.getValue().getLayer();
		}

		return layer;
	}

	public boolean hasGeoprocessSelected() {
		boolean isSelected = true;
		if (this.spatialOperationComboBox.getValue() == null) {
			isSelected = false;
		}
		return isSelected;
	}

	public IGeoprocess getGeoProcess() {
		inputGeoprocess.setLayerManager(layerManagerWidget);
		inputGeoprocess.setInputLayer(getLayer1());

		final IGeoprocess geoprocess = this.spatialOperationComboBox.getValue();

		if (geoprocess.isBufferGeoprocess()) {
			inputGeoprocess.setDistance(getDistance());
		} else {
			inputGeoprocess.setOverlayLayer(getLayer2());
		}
		geoprocess.setInputGeoprocess(inputGeoprocess);

		return geoprocess;
	}
	
	public double getDistance() {

		if (distanceTextField.getText().trim().isEmpty()) {
			return 0;
		} else {
			return Double.parseDouble(distanceTextField.getText());
		}
	}

	private Widget createPanel(final Geoprocesses spatialOperation) {
		panel = new VerticalLayoutContainer();
		panel.add(new Label(UIMessages.INSTANCE.descAnalysisText()));
		panel.add(new HTML("<HR>"));
		panel.add(new HTML("<BR>"));
		createGeoprocessAndDistanceField(spatialOperation);
		createComboLayer(LAYER_COMBO_1, COMBO1_ID, VALIDATE_LAYER_1_BUTTON);
		createComboLayer(LAYER_COMBO_2, COMBO2_ID, VALIDATE_LAYER_2_BUTTON);
		panel.add(new HTML("<BR>"));
		panel.add(new HTML("<HR>"));
		panel.add(new Label(UIMessages.INSTANCE.sodInfoText()));
		return panel;
	}
	
	private HorizontalPanel getHorizontalPanel() {
		final HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.getElement().getStyle()
				.setVerticalAlign(VerticalAlign.MIDDLE);
		return horizontalGroup;
	}

	private void initDistanceTextField() {		
		distanceTextField = new TextField();
		distanceTextField.setTitle("Distance");
		distanceTextField.setWidth(WIDTH_DISTANCE_FIELD);	
		clearDistanceTextField();			
	}

	private void createComboLayer(final ComboBox<VectorLayerInfo> combo,
			final String numCombo, final TextButton validateButton) {
		clearLayerComboBox(combo);
		combo.setTypeAhead(true);
		combo.setTriggerAction(TriggerAction.ALL);
		combo.setForceSelection(true);
		combo.setEditable(false);
		combo.enableEvents();
		combo.setWidth(WIDTH_COMBO_LAYER);
		combo.setId(numCombo);

		combo.addSelectionHandler(new SelectionHandler<VectorLayerInfo>() {
			@Override
			public void onSelection(SelectionEvent<VectorLayerInfo> event) {
				combo.setValue(event.getSelectedItem(), true);
				List<VectorLayerInfo> updatedLayers = removeLayer(event
						.getSelectedItem());
				if (COMBO1_ID.equals(combo.getId())) {
					updateCombo(LAYER_COMBO_2, LAYER_STORE_2, updatedLayers);
				} else {
					updateCombo(LAYER_COMBO_1, LAYER_STORE_1, updatedLayers);
				}
			}
		});
		
		validateButton.setToolTip(UIMessages.INSTANCE
				.descriptionValidationToolText());

		final HorizontalPanel horizontalGroup = getHorizontalPanel();
		horizontalGroup.add(combo);
		horizontalGroup.add(validateButton);

		panel.add(new Label(UIMessages.INSTANCE.layerLabelText(numCombo)));
		panel.add(horizontalGroup);
	}

	private List<VectorLayerInfo> removeLayer(VectorLayerInfo vectorLayerInfo) {
		List<VectorLayerInfo> newLayers = new ArrayList<VectorLayerInfo>();
		for (VectorLayerInfo layerInfo : layers) {
			if (!layerInfo.getName().equals(vectorLayerInfo.getName())) {
				newLayers.add(layerInfo);
			}
		}
		return newLayers;
	}

	private void updateCombo(ComboBox<VectorLayerInfo> combo,
			ListStore<VectorLayerInfo> store,
			List<VectorLayerInfo> updatedLayers) {
		store.clear();
		store.addAll(updatedLayers);
		combo.redraw();
	}


	private void createGeoprocessAndDistanceField(final Geoprocesses spatialOperation) {
		spatialOperationComboBox = new GeoprocessComboBox(
				spatialOperation.getAll());
		spatialOperationComboBox.setWidth(WIDTH_SPATIAL_OPERATION_COMBO);
		clearSpatialOperationComboBox();				
		spatialOperationComboBox
				.addSelectionHandler(new SelectionHandler<Geoprocess>() {
					@Override
					public void onSelection(SelectionEvent<Geoprocess> event) {
						spatialOperationComboBox.setValue(
								event.getSelectedItem(), true);

						if (event.getSelectedItem() instanceof BufferGeoprocess) {
							LAYER_COMBO_2.setValue(null);
							LAYER_COMBO_2.setEnabled(false);
							distanceTextField.setText("0");
							distanceTextField.setEnabled(true);
							distanceTextField.setVisible(true);
						} else {							
							LAYER_COMBO_2.setEnabled(true);
							distanceTextField.setText("");
							distanceTextField.setEnabled(false);
							distanceTextField.setVisible(false);
						}
					}
				});

		initDistanceTextField();
		final HorizontalPanel horizontalGroup = getHorizontalPanel();
		horizontalGroup.add(spatialOperationComboBox);
		horizontalGroup.add(distanceTextField);
		panel.add(horizontalGroup);
	}

	public TextButton getValidateLayer1button() {
		return VALIDATE_LAYER_1_BUTTON;
	}

	public TextButton getValidateLayer2button() {
		return VALIDATE_LAYER_2_BUTTON;
	}

	public void setLayers(final List<Layer> vectorLayers) {
		layers = new ArrayList<VectorLayerInfo>();

		for (final Layer layer : vectorLayers) {
			final VectorLayerInfo layerInfo = new VectorLayerInfo((Vector) layer);
			layers.add(layerInfo);
		}

		updateCombo(LAYER_COMBO_1, LAYER_STORE_1, layers);
		updateCombo(LAYER_COMBO_2, LAYER_STORE_2, layers);
	}

	public void clearFields() {
		clearLayerComboBox(LAYER_COMBO_1);
		clearLayerComboBox(LAYER_COMBO_2);		
		clearSpatialOperationComboBox();
		clearDistanceTextField();
	}
	
	private void clearSpatialOperationComboBox() {
		spatialOperationComboBox.setEmptyText("");
		spatialOperationComboBox.setValue(null);
		spatialOperationComboBox.setEmptyText(UIMessages.INSTANCE
				.sodOperationLabelText());
	}
	
	private void clearLayerComboBox(final ComboBox<VectorLayerInfo> layerCombo) {
		layerCombo.setEmptyText((UIMessages.INSTANCE.sbLayerComboEmptyText()));
		layerCombo.setValue(null);
	}
	
	private void clearDistanceTextField() {
		distanceTextField.setText("");
		distanceTextField.setEnabled(false);
		distanceTextField.setEmptyText(UIMessages.INSTANCE.distance());
		distanceTextField.setVisible(false);
	}
}
