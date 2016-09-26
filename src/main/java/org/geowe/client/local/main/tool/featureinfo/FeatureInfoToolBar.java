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
package org.geowe.client.local.main.tool.featureinfo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.export.exporter.FileExporter;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.edition.DeleteFeatureListenerManager;
import org.geowe.client.local.main.tool.info.DeleteFeatureListener;
import org.geowe.client.local.main.tool.spatial.GeometryValidator;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;

@ApplicationScoped
public class FeatureInfoToolBar extends ContentPanel {

	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private DeleteFeatureListenerManager deleteFeatureListenerManager;
	@Inject
	private GeometryValidator geometryValidator;


	private GridEditing<FeatureAttributeBean> gridEditing;
	private ListStore<FeatureAttributeBean> featureAttributes;

	private VectorFeature vectorFeature;

	public void initialize(GridEditing<FeatureAttributeBean> gridEditing,
			ListStore<FeatureAttributeBean> featureAttributes) {
		this.gridEditing = gridEditing;
		this.featureAttributes = featureAttributes;
		setHeaderVisible(false);
		addStyleName(ThemeStyles.get().style().borderBottom());
		HorizontalPanel horizontalGroup = new HorizontalPanel();
		horizontalGroup.setSpacing(5);
		horizontalGroup.add(getEditButton());
		horizontalGroup.add(getClearButton());
		horizontalGroup.add(getWKTButton());
		horizontalGroup.add(getExportCSVButton());
		setWidget(horizontalGroup);
	}

	private TextButton getEditButton() {
		TextButton editButton = new TextButton(
				UIMessages.INSTANCE.fidEditButtonText());
		editButton.setToolTip(UIMessages.INSTANCE.fidEditButtonTooltip());
		editButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				gridEditing.cancelEditing();
				gridEditing.startEditing(new GridCell(0, 1));
			}
		});
		return editButton;
	}

	private TextButton getClearButton() {
		TextButton clearButton = new TextButton(
				UIMessages.INSTANCE.clearButtonText());
		clearButton.setToolTip(UIMessages.INSTANCE.clearButtonTooltip());
		clearButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				gridEditing.cancelEditing();

				for (FeatureAttributeBean featureAttributeBean : featureAttributes
						.getAll()) {
					featureAttributeBean.setAttributeValue("");
					featureAttributes.update(featureAttributeBean);
				}
			}
		});
		return clearButton;
	}

	private TextButton getExportCSVButton() {
		TextButton exportButton = new TextButton(
				UIMessages.INSTANCE.exportCsvButtonText());
		exportButton.setToolTip(UIMessages.INSTANCE.exportCsvButtonTooltip());
		exportButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

				StringBuffer csv = new StringBuffer();

				for (FeatureAttributeBean featureAttributeBean : featureAttributes
						.getAll()) {
					csv.append(featureAttributeBean.getAttributeName() + ","
							+ featureAttributeBean.getAttributeValue() + "\n");
				}

				FileExporter.saveAs(csv.toString(), "geowe-feature-"
						+ vectorFeature.getFeatureId() + ".csv");
			}
		});
		return exportButton;
	}

	private TextButton getWKTButton() {
		TextButton wktButton = new TextButton("WKT");
		wktButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				new WktEditor((VectorLayer) layerManagerWidget
						.getSelectedLayer(LayerManagerWidget.VECTOR_TAB),
						vectorFeature).show();
			}
		});
		return wktButton;
	}

	public VectorFeature getVectorFeature() {
		return vectorFeature;
	}

	public void setVectorFeature(VectorFeature vectorFeature) {
		this.vectorFeature = vectorFeature;
	}

	private void notifyListener(VectorFeature vectorFeature) {
		for (DeleteFeatureListener listener : deleteFeatureListenerManager
				.getlisteners()) {
			listener.onDeleteFeature(vectorFeature);
		}
	}

	/**
	 * WKT Editor Dialog
	 * 
	 * @author rltorres
	 *
	 */
	public class WktEditor extends Dialog {

		private VectorFeature vectorFeature;
		private final VectorLayer vectorLayer;
		private TextArea wktTextArea;
		private TextButton validateLayerbutton;

		public WktEditor(VectorLayer vectorLayer, VectorFeature vectorFeature) {
			this.vectorLayer = vectorLayer;
			this.vectorFeature = vectorFeature;
			createDialog();
		}

		public VectorFeature getVectorFeature() {
			return vectorFeature;
		}

		public void setVectorFeature(VectorFeature vectorFeature) {
			this.vectorFeature = vectorFeature;
		}

		private void createDialog() {
			setHideOnButtonClick(true);
			setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
			validateLayerbutton = new TextButton(UIMessages.INSTANCE.validationToolText(), ImageProvider.INSTANCE
					.validation16());
			this.getButtonBar().add(validateLayerbutton);
			setButtonAlign(BoxLayoutPack.CENTER);
			addButtonHandlers();
				
			
			setResizable(true);
			setWidth(335);
			setHeight(350);
			setHeadingHtml("WKT: " + getVectorFeature().getFeatureId() + " - "
					+ GeoMap.INTERNAL_EPSG);
			add(createPanel());
		}
		
		public TextButton getValidateLayerbutton() {
			return this.validateLayerbutton;
		}

		private Widget createPanel() {
			HorizontalLayoutContainer container = new HorizontalLayoutContainer();
			container.setScrollMode(ScrollMode.AUTO);
			container.setSize("300px", "260px");

			container.add(getTextPanel());

			return container;
		}

		private TextArea getTextPanel() {
			wktTextArea = new TextArea();
			wktTextArea.setBorders(true);

			wktTextArea.setSize("300px", "250px");
			wktTextArea.setText(getWkt());

			return wktTextArea;
		}

		private String getWkt() {
			WKT wktFormat = new WKT();
			return wktFormat.write(getVectorFeature());
		}

		private void addButtonHandlers() {
			this.getButton(PredefinedButton.OK).addSelectHandler(
					new SelectHandler() {
						@Override
						public void onSelect(SelectEvent event) {

							Geometry geom = Geometry.fromWKT(wktTextArea
									.getText());

							VectorFeature newFeature = vectorFeature.clone();

							newFeature.getJSObject().setProperty("geometry",
									geom.getJSObject());

							vectorLayer.addFeature(newFeature);

							// TODO: ¿se quiere eliminar el lemento anterior?
							vectorFeature.destroy();
							notifyListener(vectorFeature);
						}
					});
			
			getValidateLayerbutton().addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {					
					geometryValidator.requestValidate(wktTextArea
							.getText(), "wkt-feature-" + getVectorFeature().getFeatureId(), layerManagerWidget);
				}
			});
		}
	}

}
