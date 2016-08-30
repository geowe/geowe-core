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
package org.geowe.client.local.layermanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.edition.DivideTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.GoogleV3;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.jboss.errai.ioc.client.container.IOC;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Represents the Layer Manager. It is responsible for managing map layers
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class LayerManagerWidget implements IsWidget {
	public static final String VECTOR_TAB = "Vector";
	public static final String RASTER_TAB = "Raster";
	private FramedPanel panel;
	private final Map<String, LayerTree> layerTrees = new HashMap<String, LayerTree>();
	private PlainTabPanel tabPanel;
	private final List<ChangeSelectedLayerListener> events = new ArrayList<ChangeSelectedLayerListener>();
	private final List<AddLayerListener> addLayerListeners = new ArrayList<AddLayerListener>();
	private final List<RemoveLayerListener> removeLayerListeners = new ArrayList<RemoveLayerListener>();
	private final Label statusBar = new Label(
			UIMessages.INSTANCE.lmStatusBarInitLabel());
	private final Slider slider = new Slider();

	public void add(final String titleTab, final LayerTree layerTree) {
		layerTrees.put(titleTab, layerTree);
	}

	public Widget asWidget() {

		if (panel == null) {

			panel = new FramedPanel();
			panel.setAnimCollapse(true);

			panel.setPixelSize(300, 350);
			panel.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
			panel.setHeadingText(UIMessages.INSTANCE.lmHeadingText());
			panel.setAllowTextSelection(false);

			panel.setPosition(300, 50);
			panel.getElement().getStyle().setPosition(Position.ABSOLUTE);
			panel.getElement().getStyle().setBorderWidth(1.0, Unit.PX);

			tabPanel = new PlainTabPanel();
			tabPanel.setPixelSize(300, 250);
			for (final String titleTab : layerTrees.keySet()) {				
				tabPanel.add(createContainerTab(titleTab), createTab(titleTab));
			}
			
			final VerticalLayoutContainer panelLayout = new VerticalLayoutContainer();
			panelLayout.add(tabPanel);
			panelLayout.add(getFoot());
			panel.add(panelLayout);
			panel.setVisible(false);

			new Draggable(panel, panel.getHeader());
			final ToolButton tButton = new ToolButton(ToolButton.CLOSE);
			addCloseSelectHandler(tButton);
			panel.getHeader().addTool(tButton);
			panel.setCollapsible(true);

		}

		return panel;
	}
	
	private VerticalLayoutContainer createContainerTab(final String titleTab) {
		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.add(layerTrees.get(titleTab).getToolbar(),
				new VerticalLayoutData());

		container.add(layerTrees.get(titleTab).getTree());
		return container;
	}
	
	private TabItemConfig createTab(final String titleTab) {
		final TabItemConfig iconTab = new TabItemConfig();
		iconTab.setIcon(ImageProvider.INSTANCE.layer16());
		iconTab.setText(titleTab);
		return iconTab;
	}

	private void addCloseSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				panel.setVisible(false);
			}

		});
	}

	private VerticalPanel getFoot() {
		slider.setWidth("290px");
		slider.disable();
		slider.setIncrement(1);

		slider.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(final ValueChangeEvent<Integer> event) {
				final Layer layer = getSelectedLayer(getSelectedTabName());
				layer.setOpacity((float) (event.getValue().intValue() / 100.0));
			}

		});

		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("300px");
		panel.addStyleName(ThemeStyles.get().style().borderTop());
		panel.add(new HTML(UIMessages.INSTANCE.lmOpacityHtmlLabel()));
		panel.add(slider);

		panel.add(statusBar);
		return panel;
	}

	public LayerTree getLayerTree(final String tabName) {
		return layerTrees.get(tabName);
	}

	public Layer getLayer(final String tabName, final String layerName) {
		return layerTrees.get(tabName).getLayer(layerName);
	}

	public Layer getVector(final String layerName) {
		return getLayer(VECTOR_TAB, layerName);
	}

	public Layer getRaster(final String layerName) {
		return getLayer(RASTER_TAB, layerName);
	}

	public void updateStatusBar() {
		statusBar.setText(UIMessages.INSTANCE.lmStaturBarText(
				getItemsCount(LayerManagerWidget.RASTER_TAB),
				getItemsCount(LayerManagerWidget.VECTOR_TAB)));
	}

	public Layer getSelectedLayer(final String tabName) {
		return layerTrees.get(tabName).getSelectedItem();
	}

	public void setSelectedLayer(final String tabName, final Layer selectedLayer) {
		layerTrees.get(tabName).setSelectedItem(selectedLayer);

		if (tabName.equals(LayerManagerWidget.VECTOR_TAB)) {
			final Layer layer = getSelectedLayer(tabName);

			if (layer instanceof Vector
					&& !layer.equals(getRoot(LayerManagerWidget.VECTOR_TAB))) {
				for (final ChangeSelectedLayerListener changeEvent : events) {
					changeEvent.onChange((Vector) layer);
				}
			}			
		}
				
		if (tabName.equals(LayerManagerWidget.RASTER_TAB)) {
			final Layer layer = getSelectedLayer(tabName);
			
			if (layer instanceof WMS
					&& !layer.equals(getRoot(LayerManagerWidget.RASTER_TAB))) {
				for (final ChangeSelectedLayerListener changeEvent : events) {
					changeEvent.onChange((Vector) layer);
				}
			}
		}				
	}

	public void removeLayer(final String tabName, final Layer layer) {
		layerTrees.get(tabName).remove(layer);
		updateStatusBar();
		
		for (final RemoveLayerListener listener : removeLayerListeners) {
			listener.onRemoveLayer(layerTrees.get(tabName).getLayers());
		}		
	}

	public void removeLayer(final Layer layer) {
		removeLayer(getSelectedTabName(), layer);
	}
	
	public void addRaster(final Layer layer) {
		layerTrees.get(LayerManagerWidget.RASTER_TAB).addRaster(layer);
		updateStatusBar();
	}

	public void addVector(final Layer layer) {
		final DivideTool divideTool = IOC.getBeanManager()
				.lookupBean(DivideTool.class).getInstance();
		((Vector) layer).addVectorFeatureAddedListener(divideTool
				.getFeatureAddedListener((Vector) layer));
		layerTrees.get(LayerManagerWidget.VECTOR_TAB).add(layer);
		updateStatusBar();

		for (final AddLayerListener listener : addLayerListeners) {
			listener.onAddLayer(layerTrees.get(VECTOR_TAB).getLayers());
		}
	}

	private int getItemsCount(final String tabName) {
		return layerTrees.get(tabName).getCount();
	}

	public Layer getRoot(final String tabName) {
		return layerTrees.get(tabName).getRootLayer();
	}

	public String getSelectedTabName() {
		return tabPanel.getConfig(tabPanel.getActiveWidget()).getText();
	}

	public SimpleSafeHtmlCell<String> getTreeClickEvent() {

		return new SimpleSafeHtmlCell<String>(
				SimpleSafeHtmlRenderer.getInstance(), "click") {
			@Override
			public void onBrowserEvent(final Context context, final Element parent,
					final String value, final NativeEvent event,
					final ValueUpdater<String> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event,
						valueUpdater);

				if ("click".equals(event.getType())) {
					final Layer layer = getSelectedLayer(getSelectedTabName());

					if (layer instanceof Vector
							&& !layer
									.equals(getRoot(LayerManagerWidget.VECTOR_TAB))) {
						for (final ChangeSelectedLayerListener changeEvent : events) {
							changeEvent.onChange((Vector) layer);
						}
					}
					
					if (layer instanceof WMS
							&& !layer
									.equals(getRoot(LayerManagerWidget.RASTER_TAB))) {
						for (final ChangeSelectedLayerListener changeEvent : events) {
							//changeEvent.onChange((WMS) layer);
						}
					}

					if ((layer instanceof GoogleV3)) {
						slider.disable();
						slider.setValue(100);
					} else {
						slider.setValue((int) (layer.getOpacity() * 100));
						slider.enable();
					}
				}
			}
		};
	}

	public void addChangeLayerListener(final ChangeSelectedLayerListener event) {
		events.add(event);
	}

	public void addAddLayerListener(final AddLayerListener listener) {
		addLayerListeners.add(listener);
	}

	public void addRemoveLayerListener(final RemoveLayerListener listener) {
		removeLayerListeners.add(listener);
	}

	public List<Layer> getSelectedLayers() {
		return layerTrees.get(getSelectedTabName()).getSelectedLayers();
	}

	public void setRasterVisible(final Layer layer, final boolean isVisible) {
		layerTrees.get(LayerManagerWidget.VECTOR_TAB).checkLayer(layer,
				isVisible);
	}
}