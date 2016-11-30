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
package org.geowe.client.local.layermanager.tool.create;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.github.request.GitHubGetFileListRequest;
import org.geowe.client.local.github.request.GitHubListEventListener;
import org.geowe.client.local.layermanager.tool.export.GitHubRepositoryListDialog;
import org.geowe.client.local.layermanager.tool.export.github.GitHubFileListAttributeBeanProperties;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.github.response.GitHubFileListAttributeBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

@ApplicationScoped
public class GitHubImportTab extends VerticalLayoutContainer implements
GitHubListEventListener<GitHubFileListAttributeBean>{
	private static final String FIELD_WIDTH = "300px";
	private TextField userNameField;
	private TextField repositoryField;
	private TextField pathField;
	private TextField targetUrlTextField;
	private static final String FILE_TYPE = "file";

	@Inject
	private GitHubGetFileListRequest<GitHubFileListAttributeBean> gitHubGetFileListRequest;

	private Grid<GitHubFileListAttributeBean> grid;
	private ListStore<GitHubFileListAttributeBean> fileStore;
	
	@Inject
	private GitHubRepositoryListDialog repositoryListDialog;
	@Inject
	private GitHubPathListDialog pathListDialog;
	
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	public GitHubImportTab() {
		super();
		
		//this.setSpacing(5);
				
		setWidth(350);
//		setHeight(400);
		
		userNameField = new TextField();
		userNameField.setTitle(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setEmptyText(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setWidth(150);

		repositoryField = new TextField();
		repositoryField.setTitle("Repositorio");
		repositoryField.setEmptyText("Introduce repositorio");
		repositoryField.setWidth(150);

		pathField = new TextField();
		pathField.setTitle(UIMessages.INSTANCE.gitHubPathNameField());
		pathField.setEmptyText(UIMessages.INSTANCE.gitHubPathNameField());
		pathField.setWidth(FIELD_WIDTH);

		TextButton repositoryButton = new TextButton("...");		
		TextButton pathButton = new TextButton("...");
		TextButton loadFilesButton = new TextButton("Load files");
		
//		final VerticalLayoutContainer vPanel = new VerticalLayoutContainer();

		//this.add(userNameField);
		
		final HorizontalPanel repositoryPanel = new HorizontalPanel();
		repositoryPanel.add(userNameField);
		repositoryPanel.add(repositoryField);
		repositoryPanel.add(repositoryButton);
		add(repositoryPanel);

		final HorizontalPanel pathPanel = new HorizontalPanel();

		pathPanel.add(pathField);
		pathPanel.add(pathButton);		
		add(pathPanel);		
		add(loadFilesButton);
		
		repositoryButton.addSelectHandler(getRepository());
		pathButton.addSelectHandler(getPath());
		loadFilesButton.addSelectHandler(requestGetFiles());

		addStyleName(ThemeStyles.get().style().borderBottom());
		final GitHubFileListAttributeBeanProperties props = GWT
				.create(GitHubFileListAttributeBeanProperties.class);

		fileStore = new ListStore<GitHubFileListAttributeBean>(
				props.key());

		final ColumnConfig<GitHubFileListAttributeBean, String> nameCol = new ColumnConfig<GitHubFileListAttributeBean, String>(
				props.attributeName(), 330,
				UIMessages.INSTANCE.gitHubColumNameRepo());

//		final ColumnConfig<GitHubFileListAttributeBean, String> typeCol = new ColumnConfig<GitHubFileListAttributeBean, String>(
//				props.attributeType(), 200, "Type");

		final List<ColumnConfig<GitHubFileListAttributeBean, ?>> columns = new ArrayList<ColumnConfig<GitHubFileListAttributeBean, ?>>();

		columns.add(nameCol);
		//columns.add(typeCol);

		final ColumnModel<GitHubFileListAttributeBean> columModel = new ColumnModel<GitHubFileListAttributeBean>(
				columns);

		
		grid = new Grid<GitHubFileListAttributeBean>(fileStore,
				columModel);
		grid.getView().setAutoFill(false);
		grid.setWidth(260);
		// grid.setSelectionModel(new
		// CellSelectionModel<GitHubRepositoryAttributeBean>());
		grid.getColumnModel().getColumn(0).setHideable(false);
		grid.setAllowTextSelection(true);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(false);
		setGridDragable(grid);				
		add(grid, new VerticalLayoutData(1, 1, new Margins(5, 0, 0, 0)));
		
		
		//add(vPanel);				
	}
	
	
	
	
	private SelectHandler requestGetFiles() {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				gitHubGetFileListRequest.setListener(GitHubImportTab.this);
				gitHubGetFileListRequest.send(userNameField.getText(),
						repositoryField.getText(), pathField.getText());
			}

		};
	}
	
	private SelectHandler getRepository() {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				pathField.clear();
				grid.getStore().clear();
				repositoryListDialog.setTargetTextField(repositoryField);
				repositoryListDialog.load(userNameField.getText());
			}

		};
	}

	
	private SelectHandler getPath() {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {				
				grid.getStore().clear();
				pathListDialog.setTargetTextField(pathField);
				pathListDialog.load(userNameField.getText(), repositoryField.getText(), pathField.getText());
			}

		};
	}
	
	
	private void setGridDragable(final Grid<GitHubFileListAttributeBean> grid) {
		new GridDragSource<GitHubFileListAttributeBean>(grid);

		final GridDropTarget<GitHubFileListAttributeBean> gridDrop = new GridDropTarget<GitHubFileListAttributeBean>(
				grid);
		gridDrop.setAllowSelfAsSource(true);
		gridDrop.setFeedback(Feedback.BOTH);

		gridDrop.addDragEnterHandler(new DndDragEnterHandler() {
			@Override
			public void onDragEnter(final DndDragEnterEvent event) {

			}
		});
	}
	
	public void clearFileGrid() {
		fileStore.clear();
	}
	
	public void setData(List<GitHubFileListAttributeBean> data) {
		if (data == null) {
			data = new ArrayList<GitHubFileListAttributeBean>();
		}

		clearFileGrid();
		fileStore.addAll(data);
		//setHeadingText(UIMessages.INSTANCE.gitHubTitleListRepo());
		show();
	}

//	private void addButtonHandlers() {
//		this.getButton(PredefinedButton.OK).addSelectHandler(
//				new SelectHandler() {
//					@Override
//					public void onSelect(final SelectEvent event) {
//						targetUrlTextField.setValue(getSelectedFile()
//								.getAttributeUrl());
//					}
//				});
//	}

	@Override
	public void onFinish(List<GitHubFileListAttributeBean> response) {
		List<GitHubFileListAttributeBean> filter = getFilterByFile(response);

		setData(filter);
	}

	private List<GitHubFileListAttributeBean> getFilterByFile(
			List<GitHubFileListAttributeBean> fileList) {
		List<GitHubFileListAttributeBean> filter = new ArrayList<GitHubFileListAttributeBean>();
		for (GitHubFileListAttributeBean file : fileList) {
			if (FILE_TYPE.equals(file.getAttributeType())) {
				filter.add(file);
			}
		}

		return filter;
	}

	public void setTargetURLTextField(TextField urlTextField) {
		this.targetUrlTextField = urlTextField;

	}
	
	public GitHubFileListAttributeBean getSelectedFile() {
		return grid.getSelectionModel().getSelectedItem();
	}
	
	public List<GitHubFileListAttributeBean> getSelectedFiles() {
		return grid.getSelectionModel().getSelectedItems();
	}
	
	
}
