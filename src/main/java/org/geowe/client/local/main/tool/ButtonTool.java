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

import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Abstract Class for Button Tools
 * @author geowe.org
 *
 */
public abstract class ButtonTool extends TextButton implements ChangeSelectedLayerListener{

	protected static final String width = "80px";
	protected static final String height = "80px";
	protected Vector layer;
	
	public ButtonTool(String label, ImageResource icon) {
		super();
		setText(label);
		setIconAlign(IconAlign.TOP);
		setSize(width, height);
		setIcon(icon);		
		registerSelectHandler();		
	}
	
	public ButtonTool(ImageResource icon) {
		super();				
		setIcon(icon);		
		registerSelectHandler();
	}
	
	public ButtonTool(String label, ImageResource icon,
			LayerManagerWidget layerManager) {
		super();
		setText(label);
		setIconAlign(IconAlign.TOP);
		setSize(width, height);
		setIcon(icon);		
		registerSelectHandler();	
		layerManager.addChangeLayerListener(this);
	}

	protected abstract void onRelease();
	
	
	private void registerSelectHandler() {
		addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				onRelease();
			}
		});
	}
	@Override
	public void onChange(Vector layer) {
		setEnabled(true);
		this.layer = layer;
	}

	protected ToolTipConfig createTooltipConfig(String title, String body,
			Side position) {
		ToolTipConfig toolTipconfig = new ToolTipConfig();
		toolTipconfig.setTitleHtml(title);
		toolTipconfig.setBodyHtml(body);
		toolTipconfig.setMouseOffsetX(0);
		toolTipconfig.setMouseOffsetY(0);
		toolTipconfig.setAnchor(position);

		return toolTipconfig;
	}
	
	protected void addKeyHandler(KeyUpHandler handler){
		RootPanel.get().addDomHandler(handler, KeyUpEvent.getType());
	}
	
}
