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
import org.geowe.client.local.main.AnchorBuilder;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

/**
 * Ventana encargada de realizar la carga de la sesión de trabajo
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class OpenProjectDialog extends Dialog {
	public static final int FEATURES_PER_PAGE = 50;
	private PlainTabPanel tabPanel;
	private TextField urlTextField;
	private Anchor urlToShareAnchor;
	private TextField urlShared;
	private HorizontalPanel urlPanel;
	
	private FormPanel uploadPanel;
	private FileUploadField file;
	
	public OpenProjectDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.openProject());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(490, 350);
		this.setModal(true);
		this.setResizable(false);
	}
	
	public void clear() {		
		this.file.clear();		
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
		
	}

	private Widget createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		//vPanel.setPixelSize(490, 420);
		vPanel.setSpacing(5);
		vPanel.add(createTabPanel());

		return vPanel;
	}
	
	
	private PlainTabPanel createTabPanel() {
		tabPanel = new PlainTabPanel();
		
		tabPanel.setPixelSize(490, 270);
		tabPanel.getElement().setId("tabPanel");
		
		tabPanel.add(getFilePanel(), UIMessages.INSTANCE.file());
		tabPanel.add(getURLPanel(), UIMessages.INSTANCE.url());		
						
		return tabPanel;
	}
	
	private VerticalPanel getURLPanel() {
		final VerticalPanel geoDataContainer = new VerticalPanel();
		geoDataContainer.setWidth("400px");
		geoDataContainer.setSpacing(3);

		geoDataContainer.add(new Label(UIMessages.INSTANCE.messageURLPanel()));

		urlTextField = new TextField();
		urlTextField.setBorders(true);
		urlTextField.setEmptyText("http://");
		urlTextField.setWidth(400);
		geoDataContainer.add(urlTextField);

		HorizontalPanel horizontalContainer = new HorizontalPanel();
		
		TextButton createUrlButton = new TextButton(
				UIMessages.INSTANCE.urlToShareButtonText());
		
		horizontalContainer.add(createUrlButton);
		
		createUrlButton.addSelectHandler(createUrlToShare(geoDataContainer));		
		geoDataContainer.add(horizontalContainer);
		geoDataContainer.add(createUrlToShareAnchor());

		urlShared = new TextField();
		urlShared.setBorders(true);
		urlShared.setWidth(400);
		urlShared.setVisible(false);
		geoDataContainer.add(urlShared);

		return geoDataContainer;
	}
	
	private Widget createUrlToShareAnchor() {
		urlPanel = new HorizontalPanel();
		urlPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		urlPanel.setSpacing(1);
		urlToShareAnchor = new AnchorBuilder().build();
		urlPanel.add(urlToShareAnchor);
		Image img = new Image(ImageProvider.INSTANCE.externalLink16());
		urlPanel.add(img);
		urlPanel.setVisible(false);
		return urlPanel;
	}
	
	private SelectHandler createUrlToShare(final VerticalPanel geoDataContainer) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				urlToShareAnchor.setHref(getHref());
				urlToShareAnchor.setText(
						UIMessages.INSTANCE.seeOtherWindow("GeoWE Project"),
						Direction.LTR);

				urlShared.setText(getHref());
				urlPanel.setVisible(true);
				urlShared.setVisible(true);
			}

			private String getHref() {
				String baseUrl = GWT.getHostPageBaseURL();

				baseUrl += "?projectUrl="
						+ URL.encodeQueryString(urlTextField.getValue());

				return baseUrl;
			}
		};
	}

	private FormPanel getFilePanel() {
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

		file = new FileUploadField();
		file.setName(UIMessages.INSTANCE.gdidFileUploadFieldText());
		file.setAllowBlank(false);

		layoutContainer.add(new FieldLabel(file, UIMessages.INSTANCE.file()),
				new VerticalLayoutData(-18, -1));
		layoutContainer.add(new Label(UIMessages.INSTANCE.maxFileSizeText()),
				new VerticalLayoutData(-18, -1));

		uploadPanel = new FormPanel();
		uploadPanel.setMethod(Method.POST);
		uploadPanel.setEncoding(Encoding.MULTIPART);
		uploadPanel.setAction("fileuploadzip.do");
		uploadPanel.setWidget(layoutContainer);

		return uploadPanel;
	}

	public FormPanel getUploadPanel() {
		return uploadPanel;
	}
	
	public String getActiveTab() {
		return tabPanel.getConfig(tabPanel.getActiveWidget()).getText();
	}
	
	public String getUrl() {
		return this.urlTextField.getText();
	}
}
