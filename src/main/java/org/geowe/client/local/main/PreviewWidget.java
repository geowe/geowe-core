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
package org.geowe.client.local.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.layermanager.AddLayerListener;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.RemoveLayerListener;
import org.geowe.client.local.layermanager.tool.export.ExportDataTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.format.GeoJSONCSS;
import org.geowe.client.local.ui.IntegerValueComboBox;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Represents the status bar
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class PreviewWidget extends Dialog implements
		ChangeSelectedLayerListener, AddLayerListener, RemoveLayerListener {

	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private GeoMap geoMap;
	@Inject
	private ExportDataTool exportDataTool;
	private Vector selectedLayer;
	private List<Layer> vectorLayers;
	private ComboBox<VectorLayerInfo> layerCombo;
	private ListStore<VectorLayerInfo> layerStore;
	private TextField titleField;
	private TextArea descriptionField;
	private TextButton addLayerButton;
	private TextButton printButton;
	private TextButton applyButton;
	private IntegerValueComboBox zoomPageComboBox;
	private static Integer zoomLevel[] = { 25, 50, 75, 100 };
	private HtmlEditor htmlEditor;
	private static final String DEFAULT_PROJECTION ="WGS84";
	private static final String DEFAULT_FRAME_NAME = "geowe-viewer";
	private static final String DEFAULT_TEMPLATE_PATH = "preview/leaflet-map-template.html";

	public PreviewWidget() {
		super();
		setPredefinedButtons(PredefinedButton.CLOSE);
		setButtonAlign(BoxLayoutPack.CENTER);
		setBodyStyleName("pad-text");
		getBody().addClassName("pad-text");
		setHideOnButtonClick(true);
		setResizable(false);
		setPixelSize(850, 600);
		setHeadingText(UIMessages.INSTANCE.previewTitle());
		add(createPanel());
		addHandler();
	}

	public Widget createPanel() {
		addLayerButton = new TextButton(UIMessages.INSTANCE.previewAddLayer());
		printButton = new TextButton(UIMessages.INSTANCE.previewPrint());
		applyButton = new TextButton(UIMessages.INSTANCE.previewApply());

		getButtonBar().add(printButton);

		initializeLayerCombo();

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(createFrame());

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);

		zoomPageComboBox = new IntegerValueComboBox("180px");
		zoomPageComboBox.setValues(Arrays.asList(zoomLevel));

		vPanel.add(new Label(UIMessages.INSTANCE.previewZoomLevel()));
		vPanel.add(zoomPageComboBox);
		
		vPanel.add(new Label(UIMessages.INSTANCE.sbSelectLayerLabel()));
		vPanel.add(layerCombo);
		vPanel.add(addLayerButton);

		titleField = new TextField();
		titleField.setEnabled(true);
		titleField.setWidth("280px");

		vPanel.add(new Label(UIMessages.INSTANCE.projectTitle()));
		vPanel.add(titleField);

		descriptionField = new TextArea();
		descriptionField.setEnabled(true);
		descriptionField.setWidth("280px");
		descriptionField.setHeight("200px");

		vPanel.add(new Label(UIMessages.INSTANCE.projectDescription()));
		htmlEditor = new HtmlEditor();
		
		htmlEditor.setEnabled(true);
		htmlEditor.setWidth("280px");
		htmlEditor.setHeight("200px");
		
		vPanel.add(htmlEditor);		
		vPanel.add(applyButton);
		hPanel.add(vPanel);

		return hPanel;
	}

	private void addHandler() {
		addLayerButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

				GeoJSONCSS format = new GeoJSONCSS();
				VectorLayer selectedLayer = (VectorLayer) layerCombo.getValue()
						.getLayer();
				((GeoJSONCSS) format).setLayer(selectedLayer);
				String geojson = format.write(exportDataTool
						.getTransformedFeatures(selectedLayer, DEFAULT_PROJECTION));
				addGeojsonLayer(geojson, true, DEFAULT_FRAME_NAME);

			}
		});

		printButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				zoomToPage(100, DEFAULT_FRAME_NAME);
				print(DEFAULT_FRAME_NAME);
			}
		});

		applyButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				apply(titleField.getText(), htmlEditor.getValue(), DEFAULT_FRAME_NAME);
			}
		});		

		zoomPageComboBox.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				zoomPageComboBox.setValue(event.getSelectedItem(), true);
			}
		});

		zoomPageComboBox
				.addValueChangeHandler(new ValueChangeHandler<Integer>() {
					@Override
					public void onValueChange(ValueChangeEvent<Integer> event) {
						zoomToPage(event.getValue(), DEFAULT_FRAME_NAME);
					}
				});

	}

	private native void addGeojsonLayer(String geojson, boolean zoomToLayer, String frameName) /*-{
		var f = $wnd.frames[frameName];
		f.contentWindow.addLayer(geojson, zoomToLayer);

	}-*/;

	private native void print(String frameName) /*-{
		var f = $wnd.frames[frameName];
		f.contentWindow.print();

	}-*/;

	private native void apply(String title, String description, String frameName) /*-{
		var f = $wnd.frames[frameName];
		f.contentWindow.apply(title, description);

	}-*/;

	private native String getPageWidth(String frameName) /*-{
		var f = $wnd.frames[frameName];
		return f.contentWindow.getPageWidth() + "px";

	}-*/;

	private native void zoomToPage(int zoom, String frameName) /*-{
		var f = $wnd.frames[frameName];
		f.contentWindow.applyZoom(zoom);

	}-*/;

	private Frame createFrame() {
		final Frame frame = new Frame(DEFAULT_TEMPLATE_PATH);
		frame.getElement().setId(DEFAULT_FRAME_NAME);
		frame.setTitle(UIMessages.INSTANCE.previewTitle());
		frame.setWidth("500px");
		frame.setHeight("500px");
		frame.getElement().getStyle().setBackgroundColor("gray");

		frame.setVisible(true);
		RootPanel.get().add(frame);

		frame.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				frame.getElement().getStyle().setBackgroundColor("white");
				zoomPageComboBox.setValue(75);
			}

		});

		return frame;
	}

	private void initializeLayerCombo() {
		String width = "125px";
		VectorLayerProperties properties = GWT
				.create(VectorLayerProperties.class);

		layerStore = new ListStore<VectorLayerInfo>(properties.key());

		layerCombo = new ComboBox<VectorLayerInfo>(layerStore,
				properties.name());
		layerCombo.setEmptyText((UIMessages.INSTANCE.sbLayerComboEmptyText()));
		layerCombo.setTypeAhead(true);
		layerCombo.setTriggerAction(TriggerAction.ALL);
		layerCombo.setForceSelection(true);
		layerCombo.setEditable(false);
		layerCombo.enableEvents();
		layerCombo.setWidth(width);

		layerCombo.addSelectionHandler(new SelectionHandler<VectorLayerInfo>() {
			@Override
			public void onSelection(SelectionEvent<VectorLayerInfo> event) {
				layerCombo.setValue(event.getSelectedItem(), true);
			}
		});

		layerCombo
				.addValueChangeHandler(new ValueChangeHandler<VectorLayerInfo>() {
					@Override
					public void onValueChange(
							ValueChangeEvent<VectorLayerInfo> event) {
						VectorLayer layer = (VectorLayer) layerManagerWidget
								.getSelectedLayer(LayerManagerWidget.VECTOR_TAB);
						if (layer == null) {
							layer = (VectorLayer) event.getValue().getLayer();
						}
						new SelectFeature(layer).unselectAll(null);
						layerManagerWidget.setSelectedLayer(
								LayerManagerWidget.VECTOR_TAB, layerCombo
										.getValue().getLayer());
					}
				});
	}

	@Override
	public void onChange(Vector layer) {
		setSelectedLayer(layer);
	}

	@Override
	public void onAddLayer(List<Layer> allVectorLayers) {
		setVectorLayers(allVectorLayers);
	}

	@Override
	public void onRemoveLayer(List<Layer> allVectorLayers) {
		setVectorLayers(allVectorLayers);		
		layerCombo.setValue(null);
	}

	public void setVectorLayers(List<Layer> vectorLayers) {
		this.vectorLayers = vectorLayers;
		updateStatusInfo();
	}

	public void setSelectedLayer(Vector selectedLayer) {
		this.selectedLayer = selectedLayer;
		updateStatusInfo();
	}

	public void showHidePreview() {
		if (isVisible()) {			
			hide();
		} else {
			show();
		}
	}

	private void updateStatusInfo() {
		if (vectorLayers != null) {
			List<VectorLayerInfo> vectors = new ArrayList<VectorLayerInfo>();

			for (Layer layer : vectorLayers) {
				vectors.add(new VectorLayerInfo((Vector) layer));
			}

			layerStore.clear();
			layerStore.addAll(vectors);
			layerCombo.redraw();
		}

		if (selectedLayer != null) {
			layerCombo.setValue(new VectorLayerInfo(selectedLayer));
		}
	}
}
