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
package org.geowe.client.local.layermanager.tool.export;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.layermanager.tool.export.exporter.github.GitHubGetRepositoriesRequest;
import org.geowe.client.local.layermanager.tool.export.exporter.github.GitHubListEventListener;
import org.geowe.client.local.layermanager.tool.export.github.GitHubRepositoryAttributeBeanProperties;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.shared.rest.github.response.GitHubRepositoryAttributeBean;
import org.slf4j.Logger;

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
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
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
public class GitHubRepositoryListDialog extends Dialog implements GitHubListEventListener<GitHubRepositoryAttributeBean> {
	@Inject
	private Logger log;
	@Inject
	private GitHubGetRepositoriesRequest<GitHubRepositoryAttributeBean> gitHubGetRepositoriesRequest;
	
	private ListStore<GitHubRepositoryAttributeBean> repositoryStore;	
	
	
	public GitHubRepositoryListDialog() {
		super();
		
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
	
	@PostConstruct
	private void registerEvent() {
		gitHubGetRepositoriesRequest.addListener(this);
	}

	private Widget createPanel() {
		final VerticalLayoutContainer vPanel = new VerticalLayoutContainer();
		vPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		final GitHubRepositoryAttributeBeanProperties props = GWT
				.create(GitHubRepositoryAttributeBeanProperties.class);
		repositoryStore = new ListStore<GitHubRepositoryAttributeBean>(props.key());

		final ColumnConfig<GitHubRepositoryAttributeBean, Integer> idCol = new ColumnConfig<GitHubRepositoryAttributeBean, Integer>(
				props.attributeId(), 190, "Id");		
		final ColumnConfig<GitHubRepositoryAttributeBean, String> nameCol = new ColumnConfig<GitHubRepositoryAttributeBean, String>(
				props.attributeName(), 190, "Name");
		final ColumnConfig<GitHubRepositoryAttributeBean, String> fullNameCol = new ColumnConfig<GitHubRepositoryAttributeBean, String>(
				props.attributeFullName(), 200, "Full Name");

		final List<ColumnConfig<GitHubRepositoryAttributeBean, ?>> columns = new ArrayList<ColumnConfig<GitHubRepositoryAttributeBean, ?>>();
		columns.add(idCol);
		columns.add(nameCol);
		columns.add(fullNameCol);

		final ColumnModel<GitHubRepositoryAttributeBean> columModel = new ColumnModel<GitHubRepositoryAttributeBean>(
				columns);

		final Grid<GitHubRepositoryAttributeBean> grid = new Grid<GitHubRepositoryAttributeBean>(
				repositoryStore, columModel);
		grid.setSelectionModel(new CellSelectionModel<GitHubRepositoryAttributeBean>());
		grid.getColumnModel().getColumn(0).setHideable(false);
		grid.setAllowTextSelection(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(false);
		setGridDragable(grid);
		
		vPanel.add(grid, new VerticalLayoutData(1, 1, new Margins(5, 0, 0, 0)));		

		return vPanel;
	}

	private void setGridDragable(final Grid<GitHubRepositoryAttributeBean> grid) {
		new GridDragSource<GitHubRepositoryAttributeBean>(grid);

		final GridDropTarget<GitHubRepositoryAttributeBean> gridDrop = new GridDropTarget<GitHubRepositoryAttributeBean>(
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
	
	
	public void load(String userName) {		
		gitHubGetRepositoriesRequest.send(userName);
	}
	
	
	public void setData(List<GitHubRepositoryAttributeBean> data) {
		if(data == null) {
			data = new ArrayList<GitHubRepositoryAttributeBean>();
			log.info("Los datos de listado de repositorios son nulos");
		}
		
		log.info("numero de datos: " + data.size());
		repositoryStore.clear();
		repositoryStore.addAll(data);
		setHeadingText("Lista de repositorios");
		show();
	}

	private void addButtonHandlers() {
		this.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						
					}
				});

		this.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {
						
					}
				});
	}

	@Override
	public void onFinish(List<GitHubRepositoryAttributeBean> response) {
		log.info("entra en resultado de la petición de repositorioss");
		setData(response);	
	}

	
	
}