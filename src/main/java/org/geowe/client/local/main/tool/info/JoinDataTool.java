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

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.layermanager.tool.create.CSV;
import org.geowe.client.local.layermanager.tool.create.CSV.CsvItem;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;

/**
 * Herramienta para generar informe html de una capa
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class JoinDataTool extends LayerTool {

	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	@Inject
	private JoinDataDialog joinDataDialog;

	private ProgressBarDialog autoMessageBox;

	protected List<CsvItem> csvItems;

	@Inject
	public JoinDataTool(LayerManagerWidget layerManagerWidget, GeoMap geoMap) {
		super(layerManagerWidget, geoMap);
		setText(UIMessages.INSTANCE.joinData());
	}

	@Override
	public String getName() {
		return UIMessages.INSTANCE.joinData();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.attributeList24();
	}

	@PostConstruct
	private void initialize() {
		addLoadButtonListener();
		addOkDialogListener();
		joinDataDialog.getUploadFormPanel().addSubmitCompleteHandler(
				getSubmitCompleteHandler());
	}

	@Override
	public void onClick() {
		joinDataDialog.init();
		joinDataDialog.show();
	}


	private void addLoadButtonListener() {
		joinDataDialog.getLoadFileButton().addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								autoMessageBox = new ProgressBarDialog(false,
										UIMessages.INSTANCE.processing());
								autoMessageBox.center();
								autoMessageBox.show();

								joinDataDialog.getUploadFormPanel().submit();
							}
						});
					}
				});
	}

	private SubmitCompleteHandler getSubmitCompleteHandler() {
		return new SubmitCompleteHandler() {
			public void onSubmitComplete(final SubmitCompleteEvent event) {

				final Element label = DOM.createLabel();
				label.setInnerHTML(event.getResults());

				final String csvData = label.getInnerText();
				if (!hasError(csvData)) {
					CSV csv = new CSV();
					String[] attrNames = csv.readAttributeNames(csvData);

					joinDataDialog.getAttributeCombo().getStore().clear();
					joinDataDialog.getAttributeCombo().getStore()
							.addAll(Arrays.asList(attrNames));
					joinDataDialog.getAttributeCombo().enable();
					joinDataDialog.getAttributeCombo().setVisible(true);
					csvItems = csv.getItems(csvData);
					autoMessageBox.hide();
				} else {
					showAlert("Error: " + csvData);
				}
			}

			private boolean hasError(final String contentFile) {
				return contentFile.startsWith("413")
						|| contentFile.startsWith("500");
			}
		};
	}

	private void addOkDialogListener() {
		joinDataDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								autoMessageBox = new ProgressBarDialog(false,
										UIMessages.INSTANCE.processing());
								autoMessageBox.center();
								autoMessageBox.show();
								String selectedAttribute = joinDataDialog
										.getAttributeCombo().getValue();

								if (existAttributeInLayer(selectedAttribute)) {

									addAttributesToLayer(joinDataDialog
											.getAttributeCombo().getStore()
											.getAll());

									addValuesToLayer(selectedAttribute);

									layerManagerWidget.setSelectedLayer(
											LayerManagerWidget.VECTOR_TAB,
											getSelectedVectorLayer());

									autoMessageBox.hide();
									joinDataDialog.hide();
								} else {
									autoMessageBox.hide();
									showAlert(UIMessages.INSTANCE
											.joinAttributeNotExist(selectedAttribute));
								}
							}

							private boolean existAttributeInLayer(
									String attrName) {
								return (getSelectedVectorLayer().getAttribute(
										attrName) != null);
							}
						});
					}
				});
	}

	private void addAttributesToLayer(List<String> attrNames) {
		for (String attrName : attrNames) {
			if (getSelectedVectorLayer().getAttribute(attrName) == null) {
				getSelectedVectorLayer().addAttribute(attrName, false);
			}
		}
	}

	private void addValuesToLayer(String attrNameId) {
		for (CsvItem csvItem : csvItems) {
			addAttributesToFeatures(attrNameId, csvItem);
		}
	}

	private void addAttributesToFeatures(String attrNameId, CsvItem csvItem) {
		for (VectorFeature feature : getSelectedVectorLayer().getFeatures()) {
			if (csvItem.getValue(attrNameId).equals(
					feature.getAttributes().getAttributeAsString(attrNameId))) {
				addAttributesToFeature(csvItem, feature);
				break;
			}
		}
	}

	private void addAttributesToFeature(CsvItem csvItem, VectorFeature feature) {
		for (String attrName : csvItem.getAttributeNames()) {
			feature.getAttributes().setAttribute(attrName,
					csvItem.getValue(attrName));
		}
	}

	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}

}
