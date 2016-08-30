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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.draw.DrawTool;
import org.geowe.client.local.main.tool.info.WmsGetInfoTool;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.control.Control;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.DrawFeature;
import org.gwtopenmaps.openlayers.client.control.ModifyFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.Snapping;
import org.gwtopenmaps.openlayers.client.control.TransformFeature;
import org.gwtopenmaps.openlayers.client.control.WMSGetFeatureInfo;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.WMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public abstract class ToggleTool extends ToggleButton implements
		ChangeSelectedLayerListener {

	private static Logger lOGGER = LoggerFactory.getLogger(ToggleTool.class);

	private final List<Control> controls = new ArrayList<Control>();
	protected static final String WIDTH = "80px";
	protected static final String HEIGHT = "80px";
	private Layer layer;
	private GeoMap geoMap;

	public ToggleTool(final String label, final ImageResource icon, final GeoMap geoMap,
			final LayerManagerWidget layerManager) {
		super();
		this.geoMap = geoMap;
		initialize(label, icon);
		layerManager.addChangeLayerListener(this);
	}

	public ToggleTool(final String label, final ImageResource icon, final GeoMap geoMap) {
		super();
		this.geoMap = geoMap;
		initialize(label, icon);
	}

	public ToggleTool(final String label, final ImageResource icon) {
		super();
		initialize(label, icon);
	}

	private void initialize(final String label, final ImageResource icon) {
		setText(label);
		setIconAlign(IconAlign.TOP);
		setSize(WIDTH, HEIGHT);
		setIcon(icon);
	}
	
	@PostConstruct
	private void registerValueChangeHandler() {
		addValueChangeHandler(getSelectChangeHandler());
	}
	
	protected ValueChangeHandler<Boolean> getSelectChangeHandler() {
		return new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(final ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					activeControls(controls);
				} else {
					deactiveControls(controls);
				}
			}
		};
	}

	public void setCancelable() {
		RootPanel.get().addDomHandler(setEscapeHandler(), KeyUpEvent.getType());
	}

	private KeyUpHandler setEscapeHandler() {
		KeyUpHandler handler = new KeyUpHandler() {
			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
					for (Control control : controls) {
						try {
							((DrawFeature) control).cancel();
						} catch (Exception e) {
							lOGGER.error("ACTION CANCELED: "
									+ control.getControlId());
						}
					}
				}
			}
		};
		return handler;
	}

	public void setUndoadable() {
		RootPanel.get().addDomHandler(setUndoHandler(), KeyUpEvent.getType());
	}

	private KeyUpHandler setUndoHandler() {
		final KeyUpHandler handler = new KeyUpHandler() {
			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (KeyCodes.KEY_U == event.getNativeEvent().getKeyCode()) {
					for (final Control control : controls) {
						try {
							((DrawFeature) control).undo();
						} catch (Exception e) {
							lOGGER.error(
									"ACTION UNDO: " + control.getControlId(), e);
						}
					}
				}
			}
		};
		return handler;
	}

	public void setRedoadable() {
		RootPanel.get().addDomHandler(setRedoHandler(), KeyUpEvent.getType());
	}

	private KeyUpHandler setRedoHandler() {
		final KeyUpHandler handler = new KeyUpHandler() {
			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (KeyCodes.KEY_R == event.getNativeEvent().getKeyCode()) {
					for (final Control control : controls) {
						try {
							((DrawFeature) control).redo();
						} catch (Exception e) {
							lOGGER.error("ACTION REDO: "
									+ control.getControlId());
						}
					}
				}
			}
		};
		return handler;
	}

	private void activeControls(final List<Control> controls) {
		for (final Control control : controls) {
			geoMap.addControl(control);
			control.activate();
		}
	}

	private void deactiveControls(final List<Control> controls) {
		for (final Control control : controls) {
			try {
				control.deactivate();
				geoMap.getMap().removeControl(control);
			} catch (Exception e) {
				lOGGER.info("con el DragTool hay un bug. Propiedad no establecida");
			}
		}
	}

	public Layer getLayer() {
		return layer;
	}

	@Override
	public void onChange(final Vector layer) {
		setLayer(layer);
	}			

	/**
	 * Se establece la nueva capa a la herramienta en cuestión. Esto implica
	 * rehacer/reconstruir todos los nuevos controles que requieran para
	 * trabajar con la nueva capa. Este método al ser invocado por el contexto
	 * de errai al cambiar la selección en el layermanager, se aplicará de
	 * manera automática a todas las herramientas, por tanto, hay que tener
	 * especial cuidado en, dejarlas preparadas y en concreto, la que se
	 * encuentre activada en el momento del cambio de capa, deberá de aplicarse
	 * los cambios oportunos de activación y deactivación dentro del mapa,
	 * haciendo así efectivo el cambio en el mismo instante y no requeriendo
	 * tocar el botón para su actualización/refresco de trabajo con la nueva
	 * capa.
	 * 
	 * @param layer
	 */
	public void setLayer(final Layer layer) {
		this.layer = layer;
		setEnabled(true);

		final List<Control> toDelete = new ArrayList<Control>();
		final List<Control> toInsert = new ArrayList<Control>();

		for (final Control control : controls) {
			if (control instanceof SelectFeature) {
				Vector[] vectors = Collections.singleton(layer).toArray(
						new Vector[] {});
				((SelectFeature) control).setLayers(vectors);
			} else if (control instanceof DrawFeature
					|| control instanceof ModifyFeature
					|| control instanceof DragFeature
					|| control instanceof TransformFeature) {
				toDelete.add(control);
				final Control newControl = ((DrawTool) this).createDrawTool(layer);
				toInsert.add(newControl);
			} else if (control instanceof Snapping) {
				((Snapping) control).setLayer((Vector) layer);
				((Snapping) control).setTargetLayer((Vector) layer);
			} 			
			else {
				Info.display(UIMessages.INSTANCE.warning(),
						"No implemented yet!!! " + control.getClassName());
			}
		}

		// Importante solo aplicar a las herramientas que son DrawTool
		deactiveControls(toDelete);
		controls.removeAll(toDelete);
		controls.addAll(toInsert);

		// Si el boton estuviera habilitado, automaticamente se actualizan los
		// controles para la nueva capa
		if (getValue()) {
			activeControls(toInsert);
		}
	}
	
	public void setWMSLayer(final WMS layer) {
		this.layer = layer;
		setEnabled(true);

		final List<Control> toDelete = new ArrayList<Control>();
		final List<Control> toInsert = new ArrayList<Control>();

		for (final Control control : controls) {
			
			if (control instanceof WMSGetFeatureInfo) {
				toDelete.add(control);
				final Control newControl =((WmsGetInfoTool) this).WMSGetFeatureInfo(layer);
				toInsert.add(newControl);
			}
			else {
				Info.display(UIMessages.INSTANCE.warning(),
						"No implemented yet!!! " + control.getClassName());
			}
		}
		
		deactiveControls(toDelete);
		controls.removeAll(toDelete);
		controls.addAll(toInsert);
		
		if (getValue()) {
			activeControls(toInsert);
		}
	}

	public void add(final Control control) {
		controls.add(control);
	}

	public GeoMap getGeoMap() {
		return geoMap;
	}

	public void setGeoMap(final GeoMap geoMap) {
		this.geoMap = geoMap;
	}

	protected ToolTipConfig createTooltipConfig(final String title, final String body,
			Side position) {
		final ToolTipConfig toolTipconfig = new ToolTipConfig();
		toolTipconfig.setTitleHtml(title);
		toolTipconfig.setBodyHtml(body);
		toolTipconfig.setMouseOffsetX(0);
		toolTipconfig.setMouseOffsetY(0);
		toolTipconfig.setAnchor(position);

		return toolTipconfig;
	}
}