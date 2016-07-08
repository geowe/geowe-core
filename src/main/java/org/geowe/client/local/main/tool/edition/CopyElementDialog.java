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
package org.geowe.client.local.main.tool.edition;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.VectorLayerInfo;
import org.geowe.client.local.main.VectorLayerProperties;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

/**
 * 
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class CopyElementDialog extends Dialog {

	private ComboBox<VectorLayerInfo> layerCombo1;
	private ListStore<VectorLayerInfo> layerStore1;
	
	public ComboBox<VectorLayerInfo> getLayerCombo1() {
		return layerCombo1;
	}

	public void setLayerCombo1(ComboBox<VectorLayerInfo> layerCombo1) {
		this.layerCombo1 = layerCombo1;
	}
	

	public CopyElementDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.cedHeadingText());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(300, 150);
		this.setModal(true);
		this.setHideOnButtonClick(true);
		add(createPanel());
	}
	
	
	private Widget createPanel() {
		VerticalLayoutContainer panel = new VerticalLayoutContainer();
		
		initLayerCombo1();
		
		FieldLabel layer1Label = new FieldLabel(layerCombo1,
				UIMessages.INSTANCE.layerLabelText(""));
		panel.add(layer1Label, new VerticalLayoutData(1, -1));										

		return panel;
	}	
	
	private ComboBox<VectorLayerInfo> initLayerCombo1() {
						
		VectorLayerProperties properties = GWT
				.create(VectorLayerProperties.class);

		layerStore1 = new ListStore<VectorLayerInfo>(properties.key());

		layerCombo1 = new ComboBox<VectorLayerInfo>(layerStore1,
				properties.name());
		layerCombo1.setEmptyText((UIMessages.INSTANCE.sbLayerComboEmptyText()));
		layerCombo1.setTypeAhead(true);
		layerCombo1.setTriggerAction(TriggerAction.ALL);
		layerCombo1.setForceSelection(true);
		layerCombo1.setEditable(false);
		layerCombo1.enableEvents();
		layerCombo1.setWidth(width);

		layerCombo1.addSelectionHandler(new SelectionHandler<VectorLayerInfo>() {
			@Override
			public void onSelection(SelectionEvent<VectorLayerInfo> event) {
				layerCombo1.setValue(event.getSelectedItem(), true);
			}
		});

		return layerCombo1;
	}
	
	

	public void setLayers(List<Layer> vectorLayers) {
		List<VectorLayerInfo> vectors = new ArrayList<VectorLayerInfo>();
		
		for (Layer layer : vectorLayers) {
			vectors.add(new VectorLayerInfo((Vector) layer));
		}

		layerStore1.clear();
		layerStore1.addAll(vectors);
		layerCombo1.redraw();
	}		
}
