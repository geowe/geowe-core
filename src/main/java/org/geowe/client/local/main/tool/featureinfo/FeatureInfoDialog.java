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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.FeatureSchema;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.FeatureGridCellRenderer;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
/**
* Diálogo de edición de atributos alfanuméricos
* 
* @since 30/08/2016
* @author rafa@geowe.org
* 
* Se resuelve el bug #153
* 
* **/
@ApplicationScoped
public class FeatureInfoDialog extends Dialog {
	
	private ListStore<FeatureAttributeBean> featureAttributes;	
	private VectorFeature vectorFeature;

	private final FeatureInfoToolBar featureInfoToolBar;

	@Inject
	public FeatureInfoDialog(final FeatureInfoToolBar featureInfoToolBar) {
		super();
		this.featureInfoToolBar = featureInfoToolBar;
		setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setResizable(true);
		setMaximizable(true);
		setWidth(400);
		setHeight(300);

		add(createPanel());
		addButtonHandlers();
	}

	private Widget createPanel() {
		final VerticalLayoutContainer vPanel = new VerticalLayoutContainer();
		vPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		final FeatureAttributeBeanProperties props = GWT
				.create(FeatureAttributeBeanProperties.class);
		featureAttributes = new ListStore<FeatureAttributeBean>(props.key());

		final ColumnConfig<FeatureAttributeBean, String> nameCol = new ColumnConfig<FeatureAttributeBean, String>(
				props.attributeName(), 190, UIMessages.INSTANCE.fidNameCol());
		final ColumnConfig<FeatureAttributeBean, String> valueCol = new ColumnConfig<FeatureAttributeBean, String>(
				props.attributeValue(), 200, UIMessages.INSTANCE.fidValueCol());

		valueCol.setCell(new FeatureGridCellRenderer());

		final List<ColumnConfig<FeatureAttributeBean, ?>> columns = new ArrayList<ColumnConfig<FeatureAttributeBean, ?>>();
		columns.add(nameCol);
		columns.add(valueCol);

		final ColumnModel<FeatureAttributeBean> columModel = new ColumnModel<FeatureAttributeBean>(
				columns);

		final Grid<FeatureAttributeBean> grid = new Grid<FeatureAttributeBean>(
				featureAttributes, columModel);
		grid.setSelectionModel(new CellSelectionModel<FeatureAttributeBean>());
		grid.getColumnModel().getColumn(0).setHideable(false);
		grid.setAllowTextSelection(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(false);
		setGridDragable(grid);

		// Definicion del Grid Editable
		final GridEditing<FeatureAttributeBean> gridEditing = new GridInlineEditing<FeatureAttributeBean>(grid);
		gridEditing.addEditor(valueCol, new TextField());

		featureInfoToolBar.initialize(gridEditing, featureAttributes);

		vPanel.add(featureInfoToolBar, new VerticalLayoutData(1, -1,
				new Margins(5, 0, 0, 0)));
		vPanel.add(grid, new VerticalLayoutData(1, 1, new Margins(5, 0, 0, 0)));

		final ToolButton tButton = new ToolButton(ToolButton.QUESTION);
		setHelpToolTip(tButton);
		addHelpSelectHandler(tButton);

		getHeader().addTool(tButton);

		return vPanel;
	}

	private void setGridDragable(final Grid<FeatureAttributeBean> grid) {
		new GridDragSource<FeatureAttributeBean>(grid);

		final GridDropTarget<FeatureAttributeBean> gridDrop = new GridDropTarget<FeatureAttributeBean>(
				grid);
		gridDrop.setAllowSelfAsSource(true);
		gridDrop.setFeedback(Feedback.BOTH);

		gridDrop.addDragEnterHandler(new DndDragEnterHandler() {
			@Override
			public void onDragEnter(final DndDragEnterEvent event) {
				Info.display(UIMessages.INSTANCE.warning(),
						UIMessages.INSTANCE.changesLost());
			}
		});
	}

	private void addHelpSelectHandler(final ToolButton tButton) {
		tButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				tButton.getToolTip().show();
			}
		});
	}

	private void setHelpToolTip(final ToolButton tButton) {
		final ToolTipConfig helpToolTip = new ToolTipConfig();
		helpToolTip.setTitleText(HelpMessages.INSTANCE.help());
		helpToolTip.setBodyHtml(HelpMessages.INSTANCE
				.featureInfoDialogContent());
		helpToolTip.setCloseable(true);
		tButton.setToolTipConfig(helpToolTip);
	}

	private List<FeatureAttributeBean> getElements(final VectorFeature vectorFeature) {
		final List<FeatureAttributeBean> info = new ArrayList<FeatureAttributeBean>();
		final List<String> names = vectorFeature.getAttributes().getAttributeNames();

		for (final String name : names) {
			if (name != null && !name.isEmpty()) {
				final String value = "" + vectorFeature.getAttributes()
						.getAttributeAsString(name);
				info.add(createFeatureAttributeBean(name, value));
			}
		}
		return info;
	}
	
	private FeatureAttributeBean createFeatureAttributeBean(final String name, final String value) {
		return new FeatureAttributeBean(name, value);
	}

	private void updateDataGrid() {
		featureAttributes.clear();
		featureAttributes.addAll(getElements(vectorFeature));
		setHeadingText(UIMessages.INSTANCE.fidHeadText(FeatureSchema
				.getFeatureId(vectorFeature)));
	}

	public VectorFeature getVectorFeature() {
		return vectorFeature;
	}

	public void setVectorFeature(final VectorLayer vectorLayer,
			final VectorFeature vectorFeature) {
		this.vectorFeature = vectorFeature;
		featureInfoToolBar.setVectorFeature(vectorFeature);
		updateDataGrid();
	}

	public List<FeatureAttributeBean> getFeatureAttributes() {
		return featureAttributes.getAll();
	}

	private void addButtonHandlers() {
		this.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						featureAttributes.commitChanges();
					}
				});

		this.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						featureAttributes.rejectChanges();
					}
				});
	}
}