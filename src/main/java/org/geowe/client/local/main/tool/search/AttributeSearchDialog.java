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
package org.geowe.client.local.main.tool.search;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.info.FeatureTool;
import org.geowe.client.local.main.tool.search.searcher.ContainSearcher;
import org.geowe.client.local.main.tool.search.searcher.EndSearcher;
import org.geowe.client.local.main.tool.search.searcher.EqualSearcher;
import org.geowe.client.local.main.tool.search.searcher.GreaterThanSearcher;
import org.geowe.client.local.main.tool.search.searcher.Searcher;
import org.geowe.client.local.main.tool.search.searcher.SmallerThanSearcher;
import org.geowe.client.local.main.tool.search.searcher.StartSearcher;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureAttributeDef;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.FeatureGrid;
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.slf4j.Logger;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Diálogo para la búsqueda de elementos, por atributo, de la capa seleccionada
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class AttributeSearchDialog extends Dialog {

	@Inject
	private Logger logger;
	@Inject
	private LayerSearchToolBar layerSearchToolBar;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private TextField layerNameField;
	private TextField numElementsField;
	private TextField valueAttributeField;
	private TextButton searchButton;

	private VectorLayer selectedLayer;
	private FeatureGrid featureGrid;

	private ComboBox<FeatureAttributeDef> attributeCombo;

	private CheckBox isCaseSensitive;

	public AttributeSearchDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.asdTitle());
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(530, 340);
		this.setModal(false);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
		addKeyShortcuts();
	}

	public Widget createPanel() {
		String fieldWidth = "225px";
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();

		// Panel izquierdo
		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.setSpacing(5);

		layerNameField = new TextField();
		layerNameField.setEnabled(false);
		layerNameField.setWidth(fieldWidth);
		infoPanel.add(new Label(UIMessages.INSTANCE.asdLayerNameLabel()));
		infoPanel.add(layerNameField);

		infoPanel.add(new Label(UIMessages.INSTANCE.asdAttributeLabel()));
		initializeAttributeLabelCombo(fieldWidth);
		infoPanel.add(attributeCombo);

		valueAttributeField = new TextField();
		valueAttributeField.setEnabled(true);
		valueAttributeField.setWidth(fieldWidth);
		infoPanel.add(new Label(UIMessages.INSTANCE.asdAttributeValueLabel()));
		infoPanel.add(valueAttributeField);

		infoPanel.add(createSearchButtonPanel());

		numElementsField = new TextField();
		numElementsField.setEnabled(false);
		numElementsField.setWidth(fieldWidth);
		infoPanel.add(new Label(UIMessages.INSTANCE.asdElementsCountLabel()));
		infoPanel.add(numElementsField);

		// Panel derecho
		VerticalPanel listPanel = new VerticalPanel();
		listPanel.setSpacing(5);

		listPanel.add(new Label(UIMessages.INSTANCE.asdFeaturesListLabel()));
		
		featureGrid = new FeatureGrid(225, 200);
		featureGrid.getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<VectorFeature>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<VectorFeature> event) {
						setSelectedElements();
					}
				});
		
		listPanel.add(featureGrid);

		hPanel.add(infoPanel);
		hPanel.add(listPanel);
		hPanel.add(layerSearchToolBar, new HorizontalLayoutData(1, 1,
				new Margins(30, 0, 0, 0)));

		return hPanel;
	}

	private HorizontalPanel createSearchButtonPanel() {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		searchButton = getSearchMenuButton();
		hPanel.add(searchButton);

		isCaseSensitive = new CheckBox();
		isCaseSensitive.setBoxLabel(UIMessages.INSTANCE.caseSensitive());
		isCaseSensitive.setValue(true);
		hPanel.add(isCaseSensitive);
		return hPanel;
	}

	private TextButton getSearchMenuButton() {
		TextButton searchButton = new TextButton(
				UIMessages.INSTANCE.asdSearchButtonText());
		searchButton.setMenu(getSearchMenu());
		return searchButton;
	}

	private Menu getSearchMenu() {
		Menu menu = new Menu();
		menu.add(new MenuItem(UIMessages.INSTANCE.exactEqual(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new EqualSearcher());
					}
				}));

		menu.add(new MenuItem(UIMessages.INSTANCE.contains(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new ContainSearcher());
					}
				}));

		menu.add(new MenuItem(UIMessages.INSTANCE.startsWith(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new StartSearcher());
					}
				}));

		menu.add(new MenuItem(UIMessages.INSTANCE.endsWith(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new EndSearcher());
					}
				}));
		menu.add(new MenuItem(UIMessages.INSTANCE.greater(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new GreaterThanSearcher());
					}
				}));
		menu.add(new MenuItem(UIMessages.INSTANCE.smaller(),
				new SelectionHandler<MenuItem>() {
					@Override
					public void onSelection(SelectionEvent<MenuItem> event) {
						search(new SmallerThanSearcher());
					}
				}));

		return menu;
	}

	private void initializeAttributeLabelCombo(String width) {
		ListStore<FeatureAttributeDef> attributeLabelStore = new ListStore<FeatureAttributeDef>(
				new ModelKeyProvider<FeatureAttributeDef>() {
					@Override
					public String getKey(FeatureAttributeDef item) {
						return (item == null) ? null : item.getName();
					}

				});

		attributeCombo = new ComboBox<FeatureAttributeDef>(attributeLabelStore,
				new LabelProvider<FeatureAttributeDef>() {
					@Override
					public String getLabel(FeatureAttributeDef item) {
						return (item == null) ? null : item.getName();
					}
				});

		attributeCombo.setWidth(width);
		attributeCombo.setTypeAhead(true);
		attributeCombo.setEmptyText(UIMessages.INSTANCE
				.asdAttributeComboEmptyText());
		attributeCombo.setTriggerAction(TriggerAction.ALL);
		attributeCombo.setForceSelection(true);
		attributeCombo.setEditable(false);
		attributeCombo.enableEvents();
	}

	private void search(Searcher searcher) {
		String attributeName = attributeCombo.getValue().getName();
		String attributeValue = valueAttributeField.getValue();
		List<VectorFeature> filteredFeatures = null;
		try {
			List<VectorFeature> features = Arrays.asList(selectedLayer
					.getFeatures());

			filteredFeatures = searcher.search(features, attributeValue,
					attributeName, isCaseSensitive.getValue());

		} catch (Exception e) {
			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
					e.getMessage());
			logger.error(e.getMessage());
		}
		
		featureGrid.update(filteredFeatures);
		numElementsField.setText(String.valueOf(filteredFeatures.size()));
	}

	public void setSelectedLayer(VectorLayer layer) {
		selectedLayer = layer;
		layerNameField.setText(selectedLayer.getName());
				
		//Initilize the featureGrid for the new layer
		featureGrid.rebuild(layer.getFeatures());
		featureGrid.clear();
		updateLayerAttributes();
				
		numElementsField.clear();
		valueAttributeField.clear();
	}

	private void updateLayerAttributes() {
		try {
			attributeCombo.getStore().clear();
			attributeCombo.setEmptyText(UIMessages.INSTANCE
					.asdAttributeComboEmptyText());
			if (selectedLayer != null && selectedLayer.getAttributes() != null) {
				attributeCombo.getStore().addAll(selectedLayer.getAttributes());
			}
		} catch (Exception e) {
			logger.error(
					"Error reloading label atribute combo: " + e.getMessage(),
					e);
		}
	}

	private void setSelectedElements() {
		List<VectorFeature> selectedElements = featureGrid.getSelectionModel()
				.getSelectedItems();
		
		if (selectedElements == null || selectedElements.isEmpty()) {
			Info.display(UIMessages.INSTANCE.warning(),
					UIMessages.INSTANCE.selectAtLeast(1));
		} else {
			for (FeatureTool tool : layerSearchToolBar.getTools()) {
				setSelectedElement(selectedElements, tool);
			}
		}
	}

	private void setSelectedElement(List<VectorFeature> selectedElements, FeatureTool tool) {		
		tool.setSelectedLayer(selectedLayer);
		
		if (selectedElements.size() > 1) {			
			tool.setSelectedFeatures(selectedElements);
		} else {
			tool.setSelectedFeature(selectedElements.get(0));
		}
	}

	private void addKeyShortcuts() {
		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(searchButton,
				KeyCodes.KEY_ENTER);

		layerNameField.addKeyDownHandler(keyShortcut);
		numElementsField.addKeyDownHandler(keyShortcut);
		valueAttributeField.addKeyDownHandler(keyShortcut);
		attributeCombo.addKeyDownHandler(keyShortcut);
	}
}
