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
package org.geowe.client.local.main.tool.project;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Diálogo encargado de guardar la información de la sesión de trabajo
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class SaveProjectDialog extends Dialog {

	private static final String PROJECT_VERSION = "1.0.0";
	private static final String DEFAULT_PROJECT_NAME = "geowe-project-name";
	private TextField projectNameField;
	private TextField projectVersionField;
	private TextField projectTitleField;
	private TextArea projectDescriptionField;
	private TextField vectorLayerCountField;
	

	public SaveProjectDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.saveProject());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(315, 400);
		this.setModal(true);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}
	
	public void clear() {		
		projectNameField.setValue(DEFAULT_PROJECT_NAME);
		this.projectDescriptionField.clear();
		this.projectTitleField.clear();
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());		
	}

	private Widget createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setPixelSize(315, 400);
		vPanel.setSpacing(5);
		vPanel.add(createTopPanel());

		return vPanel;
	}

	private HorizontalLayoutContainer createTopPanel() {
		String fieldWidth = "225px";
		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.setSpacing(5);

		projectNameField = new TextField();		
		projectNameField.setEnabled(true);
		projectNameField.setWidth(fieldWidth);
		infoPanel.add(new Label(UIMessages.INSTANCE.projectFileName()));
		infoPanel.add(projectNameField);
		
		projectTitleField = new TextField();
		projectTitleField.setEnabled(true);
		projectTitleField.setWidth("280px");
		infoPanel.add(new Label(UIMessages.INSTANCE.projectTitle()));
		infoPanel.add(projectTitleField);
		
		projectVersionField = new TextField();
		projectVersionField.setValue(PROJECT_VERSION);
		projectVersionField.setEnabled(false);
		projectVersionField.setWidth("125px");
		infoPanel.add(new Label(UIMessages.INSTANCE.projectVersion()));
		infoPanel.add(projectVersionField);
		
		vectorLayerCountField = new TextField();		
		vectorLayerCountField.setEnabled(false);
		vectorLayerCountField.setWidth("125px");
		infoPanel.add(new Label(UIMessages.INSTANCE.projectTotalVectorLayer()));
		infoPanel.add(vectorLayerCountField);
				
		projectDescriptionField = new TextArea();
		projectDescriptionField.setEnabled(true);
		projectDescriptionField.setWidth("280px");
		
		
		infoPanel.add(new Label(UIMessages.INSTANCE.projectDescription()));
		infoPanel.add(projectDescriptionField);
		
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();

		hPanel.add(infoPanel);

		return hPanel;
	}
	
	public String getTitle() {
		return projectTitleField.getText();
	}
	
	public String getVersion() {
		return PROJECT_VERSION;
	}
	
	public String getDescription() {
		return projectDescriptionField.getText();
	}
	
	public String getName() {
		return projectNameField.getText();
	}
	
	public String getDate() {
		final Date date = new Date();
		final String dateString = DateTimeFormat.getFormat("dd/MM/yyyy").format(date);
		return dateString;
	}
	
	public Project getProject() {
		Project project = new Project();

		project.setDate(getDate());
		project.setDescription(getDescription());
		project.setName(getName());
		project.setTitle(getTitle());
		project.setVersion(getVersion());
		
		return project;
	}

	public void setVectorLayerCount(int size) {
		vectorLayerCountField.setValue("" +size);		
	}
}
