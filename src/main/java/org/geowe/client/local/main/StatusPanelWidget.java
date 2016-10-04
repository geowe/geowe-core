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

import org.geowe.client.local.initializer.GeoMapInitializer;
import org.geowe.client.local.layermanager.AddLayerListener;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.RemoveLayerListener;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.ProjectionComboBox;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * Represents the status bar
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class StatusPanelWidget implements IsWidget,
		ChangeSelectedLayerListener, AddLayerListener, RemoveLayerListener {
	public static final String LON_LABEL = "X/Lon";
	public static final String LAT_LABEL = "Y/Lat";
	public static final String SCALE_LABEL = UIMessages.INSTANCE.sbScaleLabel();
	public static final String CLICK_COORD_X = UIMessages.INSTANCE.coordX();
	public static final String CLICK_COORD_Y = UIMessages.INSTANCE.coordY();

	@Inject
	LayerManagerWidget layerManagerWidget;

	@Inject
	GeoMap geoMap;

	private Vector selectedLayer;
	private List<Layer> vectorLayers;

	private HorizontalPanel widget;
	private VerticalPanel statusPanel;
	private ProjectionComboBox epsgCombo;
	private ComboBox<VectorLayerInfo> layerCombo;
	private ListStore<VectorLayerInfo> layerStore;
	private Grid<StatusItem> statusGrid;
	private ListStore<StatusItem> statusStore;

	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new HorizontalPanel();

			int barHeight = 210;
			int topPadding = 50;
			int barWidth = 250;
			int right = 5;

			widget.getElement().getStyle().setPosition(Position.FIXED);
			widget.getElement().getStyle().setRight(right, Unit.PX);
			widget.getElement().getStyle().setTop(topPadding, Unit.PX);
			widget.getElement().getStyle().setHeight(barHeight, Unit.PX);
			widget.getElement().getStyle().setWidth(barWidth, Unit.PX);
			widget.getElement().getStyle()
					.setVerticalAlign(VerticalAlign.MIDDLE);

			widget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			widget.setVisible(false);
			initializeStatusPanel();
			widget.add(statusPanel);
		}
		return widget;
	}

	private void initializeStatusPanel() {
		String comboWidth = "125px";
		statusPanel = new VerticalPanel();
		StyleInjector.inject(".statusPanelStyle { " + "background: #E0ECF8;"
				+ "border-radius: 5px 10px;" + "opacity: 0.8}");
		statusPanel.setStyleName("statusPanelStyle");

		statusPanel.setSpacing(5);
		statusPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		initializeLayerCombo(comboWidth);
		initializeEpsgCombo(comboWidth);
		initializeStatusGrid();

		statusPanel.add(new FieldLabel(layerCombo, UIMessages.INSTANCE
				.sbSelectLayerLabel()));
		statusPanel.add(new FieldLabel(epsgCombo, UIMessages.INSTANCE
				.sbEpsgLabel()));
		statusPanel.add(statusGrid);

		statusPanel.setVisible(false);
	}

	private void initializeLayerCombo(String width) {
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

	private void initializeEpsgCombo(String width) {
		epsgCombo = new ProjectionComboBox(width);
		epsgCombo.setValue(GeoMap.INTERNAL_EPSG);

		epsgCombo.addSelectionHandler(new SelectionHandler<String>() {
			@Override
			public void onSelection(SelectionEvent<String> event) {
				epsgCombo.setValue(event.getSelectedItem(), true);
			}
		});

		epsgCombo.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				/*
				 * Actualizamos el DisplayProjection para que las coordenadas de
				 * la barra de estado se muestren en el nuevo EPSG seleccionado
				 */
				geoMap.configure(epsgCombo.getValue(),
						GeoMapInitializer.MAX_NUM_ZOOM_LEVEL,
						GeoMapInitializer.DEFAUL_MAP_UNITS);
			}
		});
	}

	private void initializeStatusGrid() {
		// Creacion del Grid Base
		StatusItemProperties props = GWT.create(StatusItemProperties.class);
		statusStore = new ListStore<StatusItem>(props.key());
		statusStore.setAutoCommit(true);

		ColumnConfig<StatusItem, String> nameCol = new ColumnConfig<StatusItem, String>(
				props.name(), 75, UIMessages.INSTANCE.fedColumnNameText());

		ColumnConfig<StatusItem, String> valueCol = new ColumnConfig<StatusItem, String>(
				props.value(), 150,
				UIMessages.INSTANCE.fedNewAttributeDefaultNameText());

		List<ColumnConfig<StatusItem, ?>> columns = new ArrayList<ColumnConfig<StatusItem, ?>>();
		columns.add(nameCol);
		columns.add(valueCol);

		statusGrid = new Grid<StatusItem>(statusStore,
				new ColumnModel<StatusItem>(columns));
		statusGrid.setSelectionModel(new CellSelectionModel<StatusItem>());
		statusGrid.getColumnModel().getColumn(1).setHideable(false);
		statusGrid.setHideHeaders(true);
		statusGrid.setAllowTextSelection(true);
		statusGrid.getView().setStripeRows(true);
		statusGrid.getView().setColumnLines(true);
		statusGrid.setBorders(false);

		statusStore.add(new StatusItem(LON_LABEL, null));
		statusStore.add(new StatusItem(LAT_LABEL, null));
		statusStore.add(new StatusItem(SCALE_LABEL, null));
		statusStore.add(new StatusItem(CLICK_COORD_X, null));
		statusStore.add(new StatusItem(CLICK_COORD_Y, null));
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

	public void setCurrentCoordinate(LonLat currentCoordinate) {
		StatusItem lonItem = getStatusItem(LON_LABEL);
		lonItem.setValue(Double.toString(currentCoordinate.lon()));
		StatusItem latItem = getStatusItem(LAT_LABEL);
		latItem.setValue(Double.toString(currentCoordinate.lat()));

		statusStore.update(lonItem);
		statusStore.update(latItem);
	}

	public void setVectorLayers(List<Layer> vectorLayers) {
		this.vectorLayers = vectorLayers;
		updateStatusInfo();
	}

	public void setSelectedLayer(Vector selectedLayer) {
		this.selectedLayer = selectedLayer;
		updateStatusInfo();
	}

	public void setScale(String scale) {
		StatusItem scaleItem = getStatusItem(SCALE_LABEL);
		scaleItem.setValue(scale);

		statusStore.update(scaleItem);
	}

	public void setClickedCoordinates(double x, double y) {
		StatusItem messageItemX = getStatusItem(CLICK_COORD_X);
		messageItemX.setValue(String.valueOf(x));
		statusStore.update(messageItemX);

		StatusItem messageItemY = getStatusItem(CLICK_COORD_Y);
		messageItemY.setValue(String.valueOf(y));
		statusStore.update(messageItemY);
	}

	public void showHideStatusBar() {
		if (statusPanel.isVisible()) {
			statusPanel.getElement().<FxElement> cast().slideOut(Direction.UP);
			widget.setVisible(false);
		} else {
			widget.setVisible(true);
			statusPanel.getElement().<FxElement> cast().slideIn(Direction.DOWN);
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

	private StatusItem getStatusItem(String name) {
		for (StatusItem item : statusStore.getAll()) {
			if (item.getName().equals(name)) {
				return item;
			}
		}

		return null;
	}

	/**
	 * Clase para representar cada uno de los datos a mostrar en la barra de
	 * estado de la aplicacion.
	 * 
	 * @author atamunoz
	 *
	 */
	public class StatusItem {
		String name;
		String value;

		public StatusItem(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StatusItem other = (StatusItem) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private StatusPanelWidget getOuterType() {
			return StatusPanelWidget.this;
		}
	}

	public interface StatusItemProperties extends PropertyAccess<StatusItem> {
		@Path("name")
		ModelKeyProvider<StatusItem> key();

		@Path("name")
		LabelProvider<StatusItem> nameLabel();

		ValueProvider<StatusItem, String> name();

		ValueProvider<StatusItem, String> value();
	}
}
