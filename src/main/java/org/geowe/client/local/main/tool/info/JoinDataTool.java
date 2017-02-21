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
import org.geowe.client.shared.rest.URLFileRestService;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
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
	private JoinDataDialog joinDataDialog;

	private ProgressBarDialog autoMessageBox;

	protected List<CsvItem> csvItems;
	private String[] csvAttrNames;

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
								if (joinDataDialog.getActiveTab().equals(
										UIMessages.INSTANCE.file())) {
									if (joinDataDialog
											.isFileFieldCorrectFilled()) {
										joinDataDialog.getUploadFormPanel()
												.submit();
									} else {

										showAlert(UIMessages.INSTANCE
												.lrasterdAlertMessageBoxLabel(UIMessages.INSTANCE
														.file()));
									}
								} else {
									if (joinDataDialog
											.isUrlFieldCorrectFilled()) {
										lodaDataFromURL(joinDataDialog.getUrl());
									} else {
										showAlert(UIMessages.INSTANCE
												.lrasterdAlertMessageBoxLabel(UIMessages.INSTANCE
														.url()));
									}
								}
								autoMessageBox.hide();
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
					parseCsvData(csvData);
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

	private void parseCsvData(final String csvData) {
		CSV csv = new CSV();
		csvAttrNames = csv.readAttributeNames(csvData,
				joinDataDialog.getSeparator());

		joinDataDialog.getCsvAttributeCombo().getStore().clear();

		List<String> bindableAttributes = Arrays.asList(csvAttrNames);
		if (bindableAttributes.size() == 0) {
			showAlert(UIMessages.INSTANCE.joinAttributeNotExist());
		} else {
			joinDataDialog.getCsvAttributeCombo().getStore()
					.addAll(bindableAttributes);

			joinDataDialog.getCsvAttributeCombo().enable();

			joinDataDialog.getLayerAttributeCombo().loadValues(
					getSelectedVectorLayer());
			joinDataDialog.showPanels();
			csvItems = csv.getItems(csvData, joinDataDialog.getSeparator());
		}
	}

	private void lodaDataFromURL(String url) {
		String URL_BASE = GWT.getHostPageBaseURL() + "gwtOpenLayersProxy";
		try {
			autoMessageBox.show();
			RestClient.create(URLFileRestService.class, URL_BASE,
					new RemoteCallback<String>() {
						@Override
						public void callback(String response) {
							parseCsvData(response);
							autoMessageBox.hide();
						}
					}, new RestErrorCallback() {
						@Override
						public boolean error(Request message,
								Throwable throwable) {
							autoMessageBox.hide();

							showAlert("Error"
									+ UIMessages.INSTANCE.unexpectedError());
							return false;
						}
					}, Response.SC_OK).getContent(url);
		} catch (Exception e) {
			autoMessageBox.hide();
			showAlert("Error loading data..." + e.getMessage());
		}
	}

	private void addOkDialogListener() {
		joinDataDialog.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								if (joinDataDialog.getCsvAttributeCombo()
										.getValue() == null
										|| joinDataDialog
												.getLayerAttributeCombo()
												.getValue() == null) {
									showAlert(UIMessages.INSTANCE.joinAttributeMustSelect());
								}else{
									autoMessageBox = new ProgressBarDialog(false,
											UIMessages.INSTANCE.processing());
									autoMessageBox.center();
									autoMessageBox.show();
									String selectedCsvAttribute = joinDataDialog
											.getCsvAttributeCombo().getValue();
									String selectedLayerAttribute = joinDataDialog
											.getLayerAttributeCombo()
											.getValue().getName();

									addAttributesToLayer(selectedCsvAttribute);

									addValuesToLayer(selectedCsvAttribute,
											selectedLayerAttribute);

									layerManagerWidget.setSelectedLayer(
											LayerManagerWidget.VECTOR_TAB,
											getSelectedVectorLayer());

									autoMessageBox.hide();
									joinDataDialog.hide();									
								}
							}

						});
					}
				});
	}

	private void addAttributesToLayer(String bindableAttr) {
		for (String attrName : csvAttrNames) {
			if (!attrName.equals(bindableAttr)
					&& getSelectedVectorLayer().getAttribute(attrName) == null) {
				getSelectedVectorLayer().addAttribute(attrName, false);
			}
		}
	}

	private void addValuesToLayer(String csvAttrToBind, String layerAttrToBind) {
		for (CsvItem csvItem : csvItems) {
			addAttributesToFeatures(csvAttrToBind, layerAttrToBind, csvItem);
		}
	}

	private void addAttributesToFeatures(String csvAttrToBind,
			String layerAttrToBind, CsvItem csvItem) {
		for (VectorFeature feature : getSelectedVectorLayer().getFeatures()) {
			if (csvItem.getValue(csvAttrToBind).equals(""
							+ feature.getAttributes().getAttributeAsString(
									layerAttrToBind))) {
				addAttributesToFeature(csvItem, feature, csvAttrToBind);
				break;
			}
		}
	}

	private void addAttributesToFeature(CsvItem csvItem, VectorFeature feature,
			String csvAttrToBind) {
		for (String attrName : csvItem.getAttributeNames()) {
			if (!csvAttrToBind.equals(attrName)) {
				feature.getAttributes().setAttribute(attrName,
						csvItem.getValue(attrName));
			}
		}
	}

	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}

}
