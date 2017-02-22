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
import com.sencha.gxt.widget.core.client.form.FieldLabel;

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
	

	public PreviewWidget() {
		super();
		setPredefinedButtons(PredefinedButton.CLOSE);
		setButtonAlign(BoxLayoutPack.CENTER);
		setBodyStyleName("pad-text");
		getBody().addClassName("pad-text");
		setHideOnButtonClick(true);
		setResizable(false);
		setPixelSize(900, 600);
		setHeadingText("GeoWE Viewer!");
		add(createPanel());
		
	}
	
	
	
	public Widget createPanel() {
		TextButton addButton = new TextButton("Add");
		TextButton printButton = new TextButton("Print");
		
		getButtonBar().add(addButton);
		getButtonBar().add(printButton);
			
			initializeLayerCombo();
			
			
			
			
			
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setSpacing(5);			
			hPanel.add(createFrame());			
			
			
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.add(new FieldLabel(layerCombo, UIMessages.INSTANCE
					.sbSelectLayerLabel()));
			
			
			
			hPanel.add(vPanel);
			
						
			addButton.addSelectHandler(
					new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event) {
							
							GeoJSONCSS format = new GeoJSONCSS();
							VectorLayer selectedLayer = (VectorLayer)layerCombo.getValue().getLayer();
							((GeoJSONCSS) format).setLayer(selectedLayer);
							String geojson = format.write(exportDataTool.getTransformedFeatures(selectedLayer, "WGS84"));
							addGeojsonLayer(geojson, true);
							
						}
					});
			
			printButton.addSelectHandler(
					new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event) {														
							print();							
						}
					});
			
			
		
			return hPanel;
	}
	
	
	
	private native void addGeojsonLayer(String geojson, boolean zoomToLayer) /*-{
		var f = $wnd.frames["geowe-viewer"];		
		f.contentWindow.addLayer(geojson, zoomToLayer);
		
	}-*/;
	
	private native void print() /*-{
		var f = $wnd.frames["geowe-viewer"];		
		f.contentWindow.print();
		
	}-*/;
	
	private native int getWidth() /*-{
	var f = $wnd.frames["geowe-viewer"];
	aler("Ancho: " + f.contentWindow.width);		
	return f.contentWindow.width;
	
}-*/;
	
	
	private Frame createFrame() {
		final Frame frame = new Frame( "viewer/leaflet.html");
		frame.getElement().setId("geowe-viewer");
		frame.setTitle("Viewer");						
		frame.setWidth("500px");
		frame.setHeight("500px");
		frame.getElement().getStyle().setBackgroundColor("gray");
		
		frame.setVisible(true);
		RootPanel.get().add(frame);
		
		frame.addLoadHandler(new LoadHandler() {
		    

			@Override
			public void onLoad(LoadEvent event) {
				//habilitar botones
				frame.getElement().getStyle().setBackgroundColor("white");
				
				frame.setWidth("" + getWidth());
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

		layerCombo.addValueChangeHandler(new ValueChangeHandler<VectorLayerInfo>() {
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
		/*
		 * Puesto que al eliminar una capa, ninguna de las restantes queda
		 * seleccionada en el LayerManager, se limpia la capa seleccionada de la
		 * combo de capas
		 */
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
			//widget.getElement().<FxElement> cast().slideOut(Direction.UP);
			hide();
		} else {
			show();
			//widget.getElement().<FxElement> cast().slideIn(Direction.DOWN);
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
