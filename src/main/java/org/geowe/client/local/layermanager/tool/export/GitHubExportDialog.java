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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.ui.MessageDialogBuilder;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Representa el diálogo para realizar la exportación de los datos en GitHub
 * 
 * @author jose@geowe.org
 * @since 16/09/2016
 */
@ApplicationScoped
public class GitHubExportDialog extends Dialog {
	private static final String FIELD_WIDTH = "300px";
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	private TextField userNameField;
	private PasswordField passwordField;
	private TextField repositoryField;
	private TextField pathField;
	private TextField fileNameField;
	private TextField messageField;
	
	public String getUserName() {
		return userNameField.getText();
	}

	public void setUserName(String userName) {
		this.userNameField.setText(userName);
	}

	public String getPasswordField() {
		return passwordField.getText();
	}

	public void setPasswordField(String password) {
		this.passwordField.setText(password);
	}
	
	public String getRepositoryField() {
		return repositoryField.getText();
	}

	public void setRepositoryField(String repository) {
		this.repositoryField.setText(repository);
	}
	
	public String getPathField() {
		return pathField.getText();
	}

	public void setPathField(String path) {
		this.pathField.setText(path);
	}
	
	public String getMessageField() {
		return messageField.getText();
	}

	public void setMessageField(String message) {
		this.messageField.setText(message);
	}
	
	public String getFileNameField() {
		return fileNameField.getText();
	}

	public void setFileNameField(String fileName) {
		this.fileNameField.setText(fileName);
	}
			
	public GitHubExportDialog() {
		super();
		this.setHeadingText("Create File in GitHub");
		this.getHeader().setIcon(ImageProvider.INSTANCE.layerIcon());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(350, 350);		
		this.setModal(true);
		this.setHideOnButtonClick(true);
		add(createAuthenticationPanel());
		add(createRepositoryPanel());
		add(createCommitPanel());		
	}
	
	
	
	private Widget createAuthenticationPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");		
		panel.setSpacing(10);

		userNameField = new TextField();
		userNameField.setTitle("user name");
		userNameField.setWidth(FIELD_WIDTH);
		userNameField.setAllowBlank(false);

		panel.add(userNameField);

		passwordField = new PasswordField();
		
		passwordField.setTitle("password");
		passwordField.setAllowBlank(false);
		passwordField.setWidth(FIELD_WIDTH);
		panel.add(passwordField);
		
		panel.add(messageField);

		return panel;
	}
	
	private Widget createRepositoryPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setBackgroundColor("#E0ECF8");		
		panel.setWidth("350px");		
		panel.setSpacing(10);		

		repositoryField = new TextField();
		repositoryField.setTitle("repository");
		repositoryField.setAllowBlank(false);
		repositoryField.setWidth(FIELD_WIDTH);
		panel.add(repositoryField);
		
		pathField = new TextField();
		pathField.setTitle("path");
		pathField.setAllowBlank(false);
		pathField.setWidth(FIELD_WIDTH);
		panel.add(pathField);				

		return panel;
	}
	
	private Widget createCommitPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");		
		panel.setSpacing(10);				
		
		fileNameField = new TextField();
		fileNameField.setTitle("file name");
		fileNameField.setAllowBlank(false);
		fileNameField.setWidth(FIELD_WIDTH);
		panel.add(fileNameField);
		
		
		messageField = new TextField();
		messageField.setTitle("message commit");
		messageField.setAllowBlank(false);
		messageField.setWidth(FIELD_WIDTH);
		panel.add(messageField);

		return panel;
	}
	
	public void initializeFields() {
		userNameField.setEmptyText("user name");
		passwordField.setEmptyText("password");
		repositoryField.setEmptyText("repository");
		pathField.setEmptyText("path in repository");
		messageField.setEmptyText("file name");
		messageField.setEmptyText("message commit");				
	}
}
