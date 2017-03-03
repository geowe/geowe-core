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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;

/**
 * Responsable de cargar la configuración de un proyecto de geowe
 * 
 * @author jose@geowe.org
 * @since 11-02-2017
 * @author rafa@geowe.org 
 * fix issue 303
 *
 */
@ApplicationScoped
public class OpenProjectTool extends ButtonTool {

	private ProgressBarDialog autoMessageBox;
	@Inject
	private ClientTaskManager taskManager;
	@Inject
	private OpenProjectDialog openProjectDialog;	
	@Inject
	private ProjectLoader projectLoader;
	@Inject
	private URLProjectLoader urlProjectLoader;

	
	public OpenProjectTool() {
		super(UIMessages.INSTANCE.openProject(), ImageProvider.INSTANCE
				.openProject24());
		setToolTipConfig(createTooltipConfig(UIMessages.INSTANCE.openProject(),
				UIMessages.INSTANCE.openProjectToolTipText(), Side.LEFT));		
	}
	
	@Override
	protected void onRelease() {
		openProjectDialog.clear();
		openProjectDialog.show();
	}
	
	@PostConstruct
	private void initialize() {
		addDialogListener();
		addCloseButtonHandler();
		openProjectDialog.getUploadPanel().addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(final SubmitCompleteEvent event) {

				final Element label = DOM.createLabel();
				label.setInnerHTML(event.getResults());

				final String jsonProject = label.getInnerText();
				
				openProjectDialog.hide();
				if (hasError(jsonProject)) {
					autoMessageBox.hide();
					showAlert("Error: " + jsonProject);
					return;
				}
				
				projectLoader.load(jsonProject);				
				autoMessageBox.hide();
								
			}

			private boolean hasError(final String contentFile) {
				return contentFile.startsWith("413")
						|| contentFile.startsWith("500")
						||  contentFile.startsWith("204")
						||  contentFile.startsWith("406");
			}

			
		});
	}
	
	
	private void showAlert(final String errorMsg) {
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}
	
	private void addDialogListener() {
		openProjectDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				
				taskManager.execute(new Runnable() {

					@Override
					public void run() {
												
						if (openProjectDialog.getActiveTab().equals(UIMessages.INSTANCE.file())) {
							if(openProjectDialog.isFileFieldCorrectFilled()){
								autoMessageBox = new ProgressBarDialog(false,
										UIMessages.INSTANCE.processing());
								autoMessageBox.show();
								openProjectDialog.getUploadPanel().submit();	
							}else{
								showAlert(UIMessages.INSTANCE.lrasterdAlertMessageBoxLabel(UIMessages.INSTANCE.file()));
							}
						}
						if (openProjectDialog.getActiveTab().equals(UIMessages.INSTANCE.url())) {
							if(openProjectDialog.isurlFieldCorrectFilled()){
								urlProjectLoader.open(urlProjectLoader, openProjectDialog.getUrl());
								openProjectDialog.hide();	
							}else{
								showAlert(UIMessages.INSTANCE.lrasterdAlertMessageBoxLabel(UIMessages.INSTANCE.url()));
							}
						}
					}

				});
			}
		});
	}

	protected void addCloseButtonHandler() {
		openProjectDialog.getButton(PredefinedButton.CANCEL).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						openProjectDialog.hide();
					}
				});
	}
}
