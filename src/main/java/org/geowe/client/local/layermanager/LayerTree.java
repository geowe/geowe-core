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

import java.util.List;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.map.GeoMap;
import org.gwtopenmaps.openlayers.client.layer.EmptyLayer;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.dnd.core.client.TreeDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

/**
 * LayerTree Representa a un árbol de capas y es responsable del almacenamiento y acceso a
 * cada una de las capas que forman el árbol
 * 
 * @author geowe
 *
 */
public class LayerTree {
	
	private ContentPanel toolbar;
	private Tree<Layer, String> sourceTree;
	private final TreeStore<Layer> sourceStore = new TreeStore<Layer>(new KeyProvider());
	private Layer rootLayer;
	private List<Layer> layers;
	private GeoMap geoMap;		
	private DndDropHandler dndDropHandler;

	public Layer getRootLayer() {
		return rootLayer;
	}

	class KeyProvider implements ModelKeyProvider<Layer> {

		public String getKey(final Layer item) {
			return item.getName();
		}
	}

	public Tree<Layer, String> getTree() {
		return sourceTree;
	}

	public void add(final Layer layer) {				
		geoMap.getMap().addLayer(layer);
		sourceStore.add(rootLayer, layer);
		sourceTree.setChecked(layer, CheckState.CHECKED);
		sourceTree.setExpanded(rootLayer, true);
		layers.add(layer);		
	}
	
	public void addRaster(final Layer layer) {
		add(layer);
		geoMap.getMap().setLayerIndex(layer,getCount()-1);				
	}

	public Tree<Layer, String> createTree(final List<Layer> layers,
			final String rootName) {

		this.layers = layers;
		rootLayer = new EmptyLayer(rootName, new EmptyLayer.Options());
		sourceStore.add(rootLayer);
		sourceStore.add(rootLayer, layers);
		sourceTree = new Tree<Layer, String>(sourceStore,
				new ValueProvider<Layer, String>() {

					public String getValue(final Layer object) {
						return object.getName();
					}

					public void setValue(final Layer object, final String value) {
						throw new RuntimeException("Not implemented yet");
					}

					public String getPath() {
						return "name";
					}
				});
		
		sourceTree.setIconProvider(new IconProvider<Layer>() {
			public ImageResource getIcon(final Layer model) {
				if (model.isBaseLayer()) {
					return ImageProvider.INSTANCE.baseLayer();
				} else {
					return ImageProvider.INSTANCE.layer16();
				}
			}
		});
		sourceTree.setWidth(280);
		sourceTree.setHeight(200);
		sourceTree.setCheckable(true);
		sourceTree.setCheckStyle(CheckCascade.NONE);
		sourceTree.setAutoLoad(true);		
		sourceTree.setCheckNodes(CheckNodes.LEAF);
		sourceTree.addCheckChangedHandler(new CheckChangedHandler<Layer>() {
			@Override
			public void onCheckChanged(final CheckChangedEvent<Layer> event) {
				for (final Layer layer : layers) {
					layer.setIsVisible(false);
				}
				for (final Layer layer : sourceTree.getCheckedSelection()){
					layer.setIsVisible(true);
				}
			}
		});		

		final TreeDragSource<Layer> treeDragSource = new TreeDragSource<Layer>(
				sourceTree);
		treeDragSource.addDragStartHandler(new DndDragStartHandler() {

			public void onDragStart(final DndDragStartEvent event) {
				@SuppressWarnings("unchecked")
				final List<TreeNode<?>> draggingSelection = (List<TreeNode<?>>) event
						.getData();
				final Layer firstItemInStore = sourceStore.getChild(0);

				if (draggingSelection != null) {
					Layer layer = null;
					for (final TreeNode<?> node : draggingSelection) {
						layer = (Layer) node.getData();

						if (layer.equals(firstItemInStore) || layer.isBaseLayer()) {
							event.setCancelled(true);
							event.getStatusProxy().setStatus(false);
							new AlertMessageBox("Alert", "Field incorrect: "+ "You can't move root or Base layer").show();
							return;
						}
					}
				}
			}
		});

		treeDragSource.addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(final DndDropEvent event) {
				registerDndDropHandler(event);
			}

		});

		final TreeDropTarget<Layer> target = new TreeDropTarget<Layer>(sourceTree);
		target.setAllowSelfAsSource(true);
		target.setFeedback(Feedback.BOTH);
		
		return sourceTree;
	}

	private void registerDndDropHandler(final DndDropEvent event) {

		if (dndDropHandler == null) {

			this.dndDropHandler = new DndDropHandler() {

				@Override
				public void onDrop(final DndDropEvent event) {

					final List<TreeNode<Layer>> draggingSelection = (List<TreeNode<Layer>>) event
							.getData();
					if (draggingSelection != null) {
						Layer layer = null;
						for (final TreeNode<Layer> node : draggingSelection) {

							layer = node.getData();

							for (final Layer layerModel : sourceStore
									.getAllChildren(sourceStore.getChild(0))) {
								final int index = sourceStore.indexOf(layerModel);

								if (layerModel.getName()
										.equals(layer.getName())) {
									
									if (layer instanceof Vector) {										
										geoMap.getMap().setLayerIndex(layer,
												index + getCount());
												//index + geoMap.getRasterLayerCount());
									} else {
										geoMap.getMap().setLayerIndex(layer,
												index);
									}
									sourceTree.setChecked(layer, CheckState.CHECKED);
								} 
							}
						}
					}
				}
			};
			event.getDropTarget().addDropHandler(dndDropHandler);
		}
	}
		
	public void checkAllLayers(final boolean state) {
		for (final Layer layer : layers) {
			checkLayer(layer, state);
		}
	}

	public void checkLayer(final Layer layer, final boolean state) {
		if (state) {
			sourceTree.setChecked(layer, CheckState.CHECKED);
		} else {
			sourceTree.setChecked(layer, CheckState.UNCHECKED);
		}
	}

	public Layer getSelectedItem() {
		return sourceTree.getSelectionModel().getSelectedItem();
	}
	
	public void setSelectedItem(final Layer layer) {
		sourceTree.getSelectionModel().select(layer, false);
	}

	/*
	 * Elimina la capa. No se puede eliminar la raiz del árbol
	 */
	public boolean remove(final Layer layer) {
		boolean haSidoBorrada = false;
		if (!layer.equals(rootLayer)) {
			haSidoBorrada = sourceTree.getStore().remove(layer);
			geoMap.getMap().removeLayer(layer);
			// Map refresh
			geoMap.getMap().zoomTo(geoMap.getMap().getZoom());
			layers.remove(layer);
		}
		return haSidoBorrada;
	}

	public void setCell(final SimpleSafeHtmlCell<String> vectorialTreeClickEvent) {
		sourceTree.setCell(vectorialTreeClickEvent);
	}

	public void setMap(final GeoMap geoMap) {
		this.geoMap = geoMap;
	}
	
	public ContentPanel getToolbar() {
		return toolbar;
	}

	public void setToolbar(final ContentPanel toolbar) {
		this.toolbar = toolbar;
	}
	
	public List<Layer> getSelectedLayers() {
		return sourceTree.getSelectionModel().getSelectedItems();
	}
	
	public List<Layer> getLayers() {
		return layers;
	}
	
	public Layer getLayer(final String layerName) {
		Layer layer = null;
		
		for(final Layer treeLayer : layers) {
			if(treeLayer.getName().equals(layerName)) {
				layer = treeLayer;
				break;
			}
		}
		
		return layer;
	}
	
	public int getCount() {
		return layers.size();
	}
}