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
package org.geowe.client.local.main.tool.info;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.RemoveLayerListener;
import org.geowe.client.local.layermanager.tool.CopyLayerTool;
import org.geowe.client.local.main.tool.edition.DeleteFeatureListenerManager;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * 
 * @author geowe.org
 *
 */
@ApplicationScoped
public class LayerInfoDialog extends Dialog implements DeleteFeatureListener,
		RemoveLayerListener, ChangeSelectedLayerListener {

	@Inject
	private LayerInfoToolBar layerInfoToolBar;
	@Inject
	private DeleteFeatureListenerManager deleteFeatureListenerManager;
	@Inject
	private HtmlReportLayerTool htmlReportLayerTool;
	@Inject
	private CopyLayerTool copyLayerTool;
	@Inject
	private LayerManagerWidget layerManagerWidget;

	private TextField layerNameField;
	private TextField projectionField;
	private TextField numElementsField;

	private VectorLayer selectedLayer;
	private ListStore<VectorFeatureInfo> layerStore;
	private ListView<VectorFeatureInfo, String> listView;

	private TextButton renameLayerbutton;
	private TextButton applyRenameLayerbutton;

	public LayerInfoDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.lidTitle());
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(530, 320);
		this.setModal(false);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
		deleteFeatureListenerManager.addDeleteFeatureListener(this);
	}

	private Widget createPanel() {
		String fieldWidth = "225px";
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();

		// Panel izquierdo
		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.setSpacing(5);

		layerNameField = new TextField();
		layerNameField.setEnabled(false);
		layerNameField.setWidth(fieldWidth);
		infoPanel.add(new Label((UIMessages.INSTANCE.lidLayerNameLabel())));
		infoPanel.add(layerNameField);

		renameLayerbutton = new TextButton(UIMessages.INSTANCE.rename());
		renameLayerbutton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				layerNameField.setEnabled(true);
				layerNameField.focus();
				renameLayerbutton.hide();
				applyRenameLayerbutton.setVisible(true);
			}
		});
		infoPanel.add(renameLayerbutton);

		applyRenameLayerbutton = new TextButton(
				UIMessages.INSTANCE.vlswApplyButtonText());
		applyRenameLayerbutton.setVisible(false);
		applyRenameLayerbutton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				// selectedLayer.setName(layerNameField.getText());
				layerNameField.setEnabled(false);
				copyLayerTool.duplicate(selectedLayer, layerNameField.getText());
				VectorLayer layer = copyLayerTool.duplicate(selectedLayer,
						layerNameField.getText());
				
				layerManagerWidget.addVector(layer);
				layerManagerWidget.removeLayer(LayerManagerWidget.VECTOR_TAB,
						selectedLayer);
				setSelectedLayer(layer);
				applyRenameLayerbutton.hide();
				renameLayerbutton.show();
			}
		});
		infoPanel.add(applyRenameLayerbutton);

		projectionField = new TextField();
		projectionField.setEnabled(false);
		projectionField.setWidth(fieldWidth);
		infoPanel.add(new Label((UIMessages.INSTANCE.lidProjectionLabel())));
		infoPanel.add(projectionField);

		numElementsField = new TextField();
		numElementsField.setEnabled(false);
		numElementsField.setWidth(fieldWidth);
		infoPanel.add(new Label((UIMessages.INSTANCE.lidElementsCountLabel())));
		infoPanel.add(numElementsField);

		infoPanel.add(htmlReportLayerTool);

		// Panel derecho
		VerticalPanel listPanel = new VerticalPanel();
		listPanel.setSpacing(5);

		listPanel.add(new Label((UIMessages.INSTANCE.lidFeaturesListLabel())));
		listPanel.add(createListView());

		hPanel.add(infoPanel);
		hPanel.add(listPanel);
		hPanel.add(layerInfoToolBar, new HorizontalLayoutData(1, 1,
				new Margins(30, 0, 0, 0)));

		return hPanel;
	}

	private ListView<VectorFeatureInfo, String> createListView() {
		VectorFeatureProperties properties = GWT
				.create(VectorFeatureProperties.class);

		listView = new ListView<VectorFeatureInfo, String>(
				new ListStore<VectorFeatureInfo>(properties.key()),
				properties.attributeBrief());
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		listView.setWidth("225px");
		listView.setHeight("200px");

		layerStore = new ListStore<VectorFeatureInfo>(properties.key());
		listView.setStore(layerStore);

		listView.getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<VectorFeatureInfo>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<VectorFeatureInfo> event) {
						setSelectedElement();
					}
				});

		return listView;
	}

	public void setSelectedLayer(VectorLayer layer) {
		selectedLayer = layer;
		updateData();
	}

	private void updateData() {
		layerNameField.setText(selectedLayer.getName());
		projectionField.setText(selectedLayer.getProjection()
				.getProjectionCode());
		numElementsField.setText("Raster");

		if (selectedLayer instanceof Vector) {
			numElementsField.setText(Integer.toString(((Vector) selectedLayer)
					.getNumberOfFeatures()));

			setElementList(selectedLayer);
		} else {
			layerStore.clear();
		}
	}

	private void setElementList(Layer layer) {
		List<VectorFeatureInfo> featureInfoList = new ArrayList<VectorFeatureInfo>();

		if (((Vector) layer).getFeatures() != null) {
			for (VectorFeature vectorFeature : ((Vector) layer).getFeatures()) {
				featureInfoList.add(new VectorFeatureInfo(vectorFeature));
			}
		}
		layerStore.clear();
		layerStore.addAll(featureInfoList);
	}

	private void setSelectedElement() {
		List<VectorFeatureInfo> selectedElements = listView.getSelectionModel()
				.getSelectedItems();

		if (selectedElements != null && !selectedElements.isEmpty()) {
			for (FeatureTool tool : layerInfoToolBar.getTools()) {

				tool.setSelectedLayer(selectedLayer);

				if (selectedElements.size() > 1) {
					List<VectorFeature> features = new ArrayList<VectorFeature>();
					for (VectorFeatureInfo vfi : selectedElements) {
						features.add(vfi.getFeature());
					}
					tool.setSelectedFeatures(features);
				} else {
					tool.setSelectedFeature(selectedElements.get(0)
							.getFeature());
				}
			}
		}
	}

	@Override
	public void onDeleteFeature(VectorFeature feature) {

		if (selectedLayer != null) {
			updateData();
		}
		numElementsField.setText(Integer.toString(layerStore.getAll().size()));
	}

	@Override
	public void onRemoveLayer(List<Layer> allVectorLayers) {
		if (!allVectorLayers.contains(selectedLayer)) {
			this.hide();
		}
	}

	@Override
	public void onChange(Vector layer) {
		setSelectedLayer((VectorLayer) layer);
	}

}
