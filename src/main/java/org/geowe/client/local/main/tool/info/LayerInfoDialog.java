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
import java.util.Arrays;
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
import org.geowe.client.local.model.vector.AttributeValueProvider;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.util.Attributes;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Layer info dialog. Responsible to show layer info and features list.
 * 
 * @author geowe.org
 * @since 15-09-2016
 * @author rafa@geoew.org changed design
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
	private ListStore<VectorFeature> featureStore;
	private Grid<VectorFeature> featureGrid;	

	private TextButton renameLayerbutton;
	private TextButton applyRenameLayerbutton;

	public LayerInfoDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.lidTitle());
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(500, 500);
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

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setPixelSize(490, 400);
		vPanel.setSpacing(5);
		vPanel.add(createTopPanel());
		vPanel.add(createBottomPanel());

		return vPanel;
	}

	private HorizontalLayoutContainer createTopPanel() {
		String fieldWidth = "225px";
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

		VerticalPanel infoPanel2 = new VerticalPanel();
		infoPanel2.setSpacing(5);
		numElementsField = new TextField();
		numElementsField.setEnabled(false);
		numElementsField.setWidth(fieldWidth);
		infoPanel2
				.add(new Label((UIMessages.INSTANCE.lidElementsCountLabel())));
		infoPanel2.add(numElementsField);

		infoPanel2.add(htmlReportLayerTool);

		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();

		hPanel.add(infoPanel);
		hPanel.add(infoPanel2);
		return hPanel;
	}

	private HorizontalLayoutContainer createBottomPanel() {
		
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();
		hPanel.setSize("490px", "200px");
		hPanel.add(createFeatureGrid());
		hPanel.add(layerInfoToolBar);
		return hPanel;
	}
	
	/**
	 * Creates an empty feature grid
	 * @return
	 */
	private Grid<VectorFeature> createFeatureGrid() {
		featureStore = new ListStore<VectorFeature>(
				new ModelKeyProvider<VectorFeature> () {
					@Override
					public String getKey(VectorFeature item) {						
						return item.getFeatureId();
					}					
				});
		
		ColumnModel<VectorFeature> columnModel = createColumnList(featureStore);
		featureGrid = new Grid<VectorFeature>(featureStore, columnModel);				
		featureGrid.setBorders(true);
		featureGrid.getView().setForceFit(true);				
		featureGrid.getView().setAutoExpandColumn(columnModel.getColumn(2));
		featureGrid.getView().setStripeRows(true);
		featureGrid.getView().setColumnLines(true);
		featureGrid.setBorders(true);
		featureGrid.setColumnReordering(true);		
		
		featureGrid.setWidth("430px");
		featureGrid.setHeight("200px");		
		
		featureGrid.getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<VectorFeature>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<VectorFeature> event) {
						setSelectedElement();
					}
				});
		
		return featureGrid;
	}	
	
	private ColumnModel<VectorFeature> createColumnList(ListStore<VectorFeature> featureStore) {
		List<ColumnConfig<VectorFeature, ?>> columns = new ArrayList<ColumnConfig<VectorFeature, ?>>();
		
		if(featureStore.size() > 0) {
			VectorFeature feature = featureStore.get(0);

			if(feature.getAttributes() != null) {
				for(String attributeName : feature.getAttributes().getAttributeNames()) {	
					AttributeValueProvider attributeProvider = new AttributeValueProvider(attributeName);
					
					ColumnConfig<VectorFeature, String> attributeColumn = new ColumnConfig<VectorFeature, String>(
							attributeProvider, 100, attributeName);
					attributeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					columns.add(attributeColumn);
				}
			}					
		}
		
		return new ColumnModel<VectorFeature>(columns);
	}

	public void setSelectedLayer(VectorLayer layer) {
		selectedLayer = layer;
		updateData();
	}

	private void updateData() {
		layerNameField.setText(selectedLayer.getName());
		layerNameField.setToolTip(selectedLayer.getName());
		projectionField.setText(selectedLayer.getProjection()
				.getProjectionCode());
		numElementsField.setText("Raster");

		if (selectedLayer instanceof Vector) {
			numElementsField.setText(Integer.toString(((Vector) selectedLayer)
					.getNumberOfFeatures()));

			setElementList(selectedLayer);
		} else {
			featureStore.clear();
		}
	}

	private void setElementList(Layer layer) {
		List<VectorFeatureInfo> featureInfoList = new ArrayList<VectorFeatureInfo>();
		VectorFeature[] vectorFeatures = ((Vector) layer).getFeatures();
		if (vectorFeatures != null) {
			featureInfoList.add(getAttributeNames(vectorFeatures[0]));
			for (VectorFeature vectorFeature : vectorFeatures) {
				featureInfoList.add(new VectorFeatureInfo(vectorFeature));
			}
		}

		featureStore.clear();
		featureStore.addAll(Arrays.asList(vectorFeatures));
		featureGrid.reconfigure(featureStore, createColumnList(featureStore));
	}

	private VectorFeatureInfo getAttributeNames(VectorFeature vectorFeature) {
		VectorFeature dummyVectorFeature = vectorFeature.clone();
		dummyVectorFeature.getGeometry().destroy();
		Attributes attrs = new Attributes();
		for (String attrName : dummyVectorFeature.getAttributes()
				.getAttributeNames()) {
			attrs.setAttribute(attrName, attrName.toUpperCase());
		}
		dummyVectorFeature.setAttributes(attrs);
		return new VectorFeatureInfo(dummyVectorFeature);
	}

	private void setSelectedElement() {
		List<VectorFeature> selectedElements =  
				featureGrid.getSelectionModel()
				.getSelectedItems();

		if (selectedElements != null && !selectedElements.isEmpty()) {
			for (FeatureTool tool : layerInfoToolBar.getTools()) {

				tool.setSelectedLayer(selectedLayer);

				if (selectedElements.size() > 1) {
					tool.setSelectedFeatures(selectedElements);
				} else {
					tool.setSelectedFeature(selectedElements.get(0));
				}
			}
		}
	}

	@Override
	public void onDeleteFeature(VectorFeature feature) {

		if (selectedLayer != null) {
			updateData();
		}
		numElementsField.setText(Integer.toString(featureStore.getAll().size()));
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
