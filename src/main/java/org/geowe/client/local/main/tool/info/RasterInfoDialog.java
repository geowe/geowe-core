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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.ChangeSelectedLayerListener;
import org.geowe.client.local.layermanager.RemoveLayerListener;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * @author geowe.org
 *
 */
@ApplicationScoped
public class RasterInfoDialog extends Dialog implements RemoveLayerListener,
		ChangeSelectedLayerListener {

	private TextField layerNameField;
	private TextField projectionField;
	private TextField baseLayerField;

	private Layer selectedLayer;
	private TextField layerZoomField;

	private TextArea layerScalesTextArea;
	private TextArea layerResolutionsTextArea;

	public RasterInfoDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.lidTitle());
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(530, 350);
		this.setModal(false);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
	}

	private Widget createPanel() {
		String fieldWidth = "225px";
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(10);
		// Panel izquierdo
		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setSpacing(5);

		layerNameField = new TextField();
		layerNameField.setEnabled(false);
		layerNameField.setWidth(fieldWidth);
		leftPanel.add(new Label((UIMessages.INSTANCE.lidLayerNameLabel())));
		leftPanel.add(layerNameField);

		projectionField = new TextField();
		projectionField.setEnabled(false);
		projectionField.setWidth(fieldWidth);
		leftPanel.add(new Label((UIMessages.INSTANCE.lidProjectionLabel())));
		leftPanel.add(projectionField);

		baseLayerField = new TextField();
		baseLayerField.setEnabled(false);
		baseLayerField.setWidth(fieldWidth);
		leftPanel.add(new Label((UIMessages.INSTANCE.baseLayer())));
		leftPanel.add(baseLayerField);

		layerZoomField = new TextField();
		layerZoomField.setEnabled(false);
		layerZoomField.setWidth(fieldWidth);
		leftPanel.add(new Label(
				(UIMessages.INSTANCE.mpZoomToolsLabel() + "(min/max)")));
		leftPanel.add(layerZoomField);

		// Panel derecho
		VerticalPanel rightPanel = new VerticalPanel();
		leftPanel.setSpacing(5);

		layerScalesTextArea = new TextArea();
		layerScalesTextArea.setSize(fieldWidth, "100px");
		layerScalesTextArea.setEnabled(false);
		rightPanel.add(new Label((UIMessages.INSTANCE.scales())));
		rightPanel.add(layerScalesTextArea);
		layerScalesTextArea.setToolTip(UIMessages.INSTANCE.scalesDescription());

		layerResolutionsTextArea = new TextArea();
		layerResolutionsTextArea.setSize(fieldWidth, "100px");
		layerResolutionsTextArea.setEnabled(false);
		rightPanel.add(new Label((UIMessages.INSTANCE.resolution())));
		rightPanel.add(layerResolutionsTextArea);
		layerResolutionsTextArea.setToolTip(UIMessages.INSTANCE
				.resolutionsDescription());

		hPanel.add(leftPanel);
		hPanel.add(rightPanel);
		return hPanel;
	}

	public void setSelectedLayer(Layer layer) {
		selectedLayer = layer;
		updateData();
	}

	private void updateData() {
		layerNameField.setText(selectedLayer.getName());
		projectionField.setText(selectedLayer.getProjection()
				.getProjectionCode());
		baseLayerField.setText(Boolean.toString(selectedLayer.isBaseLayer()));
		layerZoomField.setText(selectedLayer.getMinZoomLevel() + "/"
				+ selectedLayer.getMaxZoomLevel());
		layerScalesTextArea.setText(asString(selectedLayer.getScales()));
		layerResolutionsTextArea.setText(asString(selectedLayer
				.getResolutions()));
	}

	private String asString(double... values) {
		StringBuilder stringValues = new StringBuilder();
		if (values != null) {
			for (Double scale : values) {
				stringValues.append(scale);
				stringValues.append(" - ");
			}
		}
		return stringValues.toString();
	}

	@Override
	public void onRemoveLayer(List<Layer> allLayers) {
		this.hide();
	}

	@Override
	public void onChange(Vector layer) {
		if (layer instanceof Layer) {
			setSelectedLayer((Layer) layer);
		}
	}

}
