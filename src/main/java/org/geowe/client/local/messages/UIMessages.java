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
package org.geowe.client.local.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface UIMessages extends Messages {

	UIMessages INSTANCE = GWT.create(UIMessages.class);

	// ActionBar
	String abMenu();

	// GeocodingPanelWidget
	String gcProgressMessageBoxTitle();

	String gcProgressMessageBoxMsg();

	String gcProgressMessageBoxProgressMsg();

	String gclocationButtonToolTip();

	String gcW3WlocationButtonToolTip();

	String gcAddressTextField();

	String gcSearchButtonToolTip();

	String gcBadCoord();

	String gcCurrentLotationButtonText();

	String gcCoordinateLocationbuttonText();

	String gcBadWords();

	// MenuPanelWidget
	String mpMapLabel();

	String mpLayersLabel();

	String mpMeasureToolsLabel();

	String mpZoomToolsLabel();

	String mpDrawToolsLabel();

	String mpEditionToolsLabel();

	// StateBar
	String sbLayerComboEmptyText();

	String sbSelectLayerLabel();

	String sbEpsgLabel();

	String sbScaleLabel();

	// MainTools
	String changeStyleToolText();

	String featureInfoToolText();

	String measure();

	String measureAreaToolText();

	String matDialogLabel();

	String measureElementToolText();

	String metDialogLabel(double value, String type, String Unit);

	String measureToolText();

	String mtDialogLabel(double value, String unit);

	String panToolText();

	String selectionToolText();

	// Drawtools
	String circleToolText();

	String holePolygonToolText();

	String drawPolygonToolTip();

	String lineStringToolText();

	String drawLineToolTip();

	String pointToolText();

	String polygonToolText();

	String regularPolygonToolText();

	// EditionTools
	String bufferToolText();

	String validationToolText();

	// descripcion del boton de validacion en la herramienta de
	// geoprocesamiento, indica recomendacion
	String descriptionValidationToolText();

	// descripcion del boton de la validacion de la herramienta de validación
	// geométrica
	String descValidationToolText();

	String confirmValidationTool(String layerName);

	String msgValidEmptyLayer();

	String msgNoErrors(String layerName);

	String msgValidationErrorsDetected(String layerName, int errors);

	String btMessageBoxPromptTitle();

	String btMessageBoxPromptLabel();

	String deleteToolText();

	String dtMessageBoxTitle();

	String dtMessageBoxLabel();

	String divideToolText();

	String dtProgressMessageBoxTitle();

	String dtProgressMessageBoxLabel();

	String processing();

	String dragToolText();

	String reshapeToolText();

	String resizeToolText();

	String rotateToolText();

	String snappingToolText();

	String unionToolText();

	String utAlertMessageBoxTitle();

	String utAlertMessageBoxLabel();

	// LayerTools
	String layerCatalogToolText();

	String layerInfoToolText();

	String layerManagerToolText();

	// MapTools
	String graticuleToolText();

	String mapToolTipToolText();

	String printToolText();

	String zoomToFullExtendToolText();

	// ZoomTools
	String searchAttributeToolText();

	String zoomBoxToolText();

	String zoomInToolText();

	String zoomOutToolText();

	String zoomNextToolText();

	String zoomPreviousToolText();

	String zoomToSeletionToolText();

	// FeatureInfoDialog
	String fidHeadText(String featureId);

	String fidNameCol();

	String fidValueCol();

	String fidEditButtonText();

	String fidEditButtonTooltip();

	// FeatureInfoDialogTools
	String deleteVectorFeatureToolText();

	String selectVectorFeatureToolText();

	String singleFeatureInfoToolText();

	String zoomToVectorFeatureToolText();

	// LayerInfoDialog
	String lidTitle();

	String lidLayerNameLabel();

	String lidProjectionLabel();

	String lidElementsCountLabel();

	String lidFeaturesListLabel();

	// AttributeSearchDialog
	String asdTitle();

	String asdLayerNameLabel();

	String asdAttributeLabel();

	String asdAttributeComboEmptyText();

	String asdAttributeValueLabel();

	String asdElementsCountLabel();

	String asdSearchButtonText();

	String asdFeaturesListLabel();

	// FeatureSchemaDialog
	String fedColumnNameText();

	String fedColumnTypeText();

	String fedAddButtonText();

	String fedRemoveButtonText();

	String fedHeadingText(String vectorlayerName);

	String fedNewAttributeDefaultNameText();

	// LayerManagerWidget
	String lmHeadingText();

	String lmStatusBarInitLabel();

	String lmOpacityHtmlLabel();

	String lmStaturBarText(int rasterNum, int vectorNum);

	// LayerManager Tools
	String copyLayerToolText();

	String cltMessageBoxTitle();

	String cltMessageBoxLabel();

	String deleteLayerToolText();

	String dltAlertMessageBoxTitle();

	String dltAlertMessageBoxLabel();

	String dltConfirMessageBoxTitle();

	String dltConfirMessageBoxLabel(@PluralCount int itemCount);

	String exportDataToolText();

	String edtAlertMessageBoxTitle();

	String edtAlertMessageBoxLabel();

	String download();

	String infoLayerToolText();

	String layerSchemaToolText();

	String zoomToVectorLayerToolText();

	String addVectorLayerToolText();

	String avltDialogTitle();

	String addRasterLayerToolText();

	String aRasterltAlertMessageBoxTitle();

	String aRasterltAlertMessageBoxLabel();

	String createEmptyLayerToolText();

	String celtPromptTitle();

	String celtPromptLabel();

	// GeoDataImportDialog
	String geoDataImportDialogTitle();

	String gdidLayerNameLabel();

	String gdidProjectionLabel();

	String gdidDataFormatLabel();

	String gdidDataFormatComboEmptyText();

	String gdidTextAreaEmptyText();

	String gdidTextAreaTitleLabel();

	String gdidFileUploadFieldText();

	String gdidWfsUrlField();

	String gdidWfsLayerField();

	String gdidWfsLayerFieldStakeholder();

	String gdidWfsGeomColumnField();

	String gdidWfsVersionField();

	String geoDataImportToolProgressMessageBoxTitle();

	String geoDataImportToolProgressMessageBoxLabel();

	String geoDataImportToolProgressText();

	// ExportDataTool
	String exportDataToolDialogTitle(String layerName);

	String exportingLayer();

	String downloadfileText();

	String edtAlertDialogTitle();

	String edtAlertDialogLabel();

	// LoadRasterLayerDialog
	String lrasterdTitle();

	String lrasterdUrlField();

	String lrasterdLayerNameField();

	String lrasterdImageFormatField();

	String lrasterdAlertMessageBoxTitle();

	String lrasterdAlertMessageBoxLabel(String wrongfield);
	
	String lrasterdMatrixSetField();

	// CatalogDialog
	String catalogTitle();

	String catalogGridToolTip();

	String catalogAvailableLayers();

	String catalogLayerAddedLabel();

	String catalogLayersCurrentMapLabel();

	String createNewLayerTool();

	// VectorLayerStyleWidget
	String vlswHeading();

	String vlswApplyButtonText();

	String vlswCancelButtonText();

	String vlswFillcolor();

	String vlswFillopacity();

	String vlswStrokeColor();

	String vlswStrokeWidth();

	String vlswVertexStyle();

	String vlswLabelAttribute();

	String vlswProgressBoxTitle();

	String vlswProgressBoxLabel();

	String vlswProgressText();

	String vlsLineAndColor();

	String vlsColorTheming();

	String vlsLabel();

	String vlsFontSize();

	String vlsFontBold();

	String enableLabelStyle();

	String exportCsvButtonText();

	String exportCsvButtonTooltip();

	String clearButtonText();

	String clearButtonTooltip();

	String wfs();

	String file();

	String url();

	String text();

	String empty();

	String warning();

	String gditAlertMessage();

	String elements();

	String comboEmptyText();

	String openInNewWindow();

	String htmlReportButtonText();

	String layerReportHeader();

	String progressBarDialogTitle();

	String mpSpatialToolsLabel();

	String envelopeToolText();

	String centroidToolText();

	String analysisToolText();

	String coordX();

	String coordY();

	String what3Words();

	String maxFileSizeText();

	String w3wErrorText();

	String fail();

	String leaveWebText();

	String sodHeadingText();

	String layerLabelText(String string);

	String sodOperationLabelText();

	String distance();

	String descAnalysisText();

	String sodInfoText();

	String cedHeadingText();

	String viewText();

	String basicToolBarToolText();

	String selectAtLeast(int minimumNumber);

	String envelopToolConfirmText();

	String edtConfirmDownload();

	String intersection();

	String intersects();

	String difference();

	String buffer();

	String union();

	String add();

	String defaultLayerName();

	String exactEqual();

	String contains();

	String startsWith();

	String endsWith();

	String centroidToolConfirmText();

	String transformToolText();

	String wmsInfo();

	String urlToShareButtonText();

	String seeOtherWindow(String name);

	String mapToolTipToolInfoText();

	String merge();

	String gdidWfsNameSpaceField();

	String caseSensitive();

	String symdifference();

	String layerAlreadyExist(String layerName);

	String basicToolBarToolTip();

	String changeStyleToolTip();

	String featureInfoToolTip();

	String selectionToolTip();

	String w3wToolTip();

	String drawCircleToolTip();

	String drawHolePolygonToolTip();

	String drawPointToolTip();

	String drawRegularPolygonToolTip();

	String notSupported();

	String geolocationNotSupported();

	String copyToolText();

	String copyToolTip();

	String deleteToolTip();

	String showCapabilitiesText();

	String layerManagerToolTip();

	String panToolTip();

	String graticuleToolTip();

	String divideToolTip();

	String dragToolTip();

	String layerCatalogToolTip();

	String noSelectedLayer();

	String background();

	String enableColorTheming();

	String reshapeToolTip();

	String resizeToolTip();

	String rotateToolTip();

	String snappingToolTip();

	String unionToolTip();

	String wmsInfoToolTip();

	String addLayerToolTip();

	String layerInfoToolTip();

	String zoomToFullExtendToolTip();

	String measureAreaToolTip();

	String measureElementToolTip();

	String measureToolTip();

	String centroidToolTip();

	String envelopeToolTip();

	String analysisToolTip();

	String searchAttributeToolTip();

	String zoomBoxToolTip();

	String zoomInToolTip();

	String zoomOutToolTip();

	String zoomNextToolTip();

	String zoomPreviousToolTextTip();

	String zoomToSeletionToolTip();

	String bufferToolTip();

	String bufferToolConfirmText();

	String baseLayer();

	String rename();

	String scales();

	String resolution();

	String resolutionsDescription();

	String scalesDescription();

	String layerNotVisible(String name);

	String attributionEmptyLayer();

	String changesLost();

	String celtAddAttributesLabel();

	String coordWGS84();

	String latitude();

	String longitude();

	String noSelectedElements();

	String noIntersectPolygon();

	String errorLoadingLayer(String layerName);

	String greater();

	String smaller();
	
	String noGeoprocessSpecify();
	
	String noVectorLayerSpecify();
	
	String emptyVectorLayer();
	
	String noInputDataSpecified();
	
	String zoomWarning(int zoomLevel, String googleSatellite);
	
	String zoomLevelText(int zoomLevel);
	
	String currentZoomLevelText();
	
	String gitHubSavedSucsessfully(String link);
	
	String gitHubResponseTitle();
	
	String gitHubExportDialogTitle();
}
