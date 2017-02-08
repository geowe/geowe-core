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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.ProgressBarDialog;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * Responsable de cargar los proyectos GeoWE desde una URL dada
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class URLProjectLoader {
	private static final String URL_BASE = GWT.getHostPageBaseURL()+ "gwtOpenLayersProxy?targetURL=";
	private ProgressBarDialog autoMessageBox;
	
	@Inject
	private ProjectLoader projectLoader;
	
	private native static void loadZip(URLProjectLoader instance, final String projectUrl) /*-{
				
		$wnd.JSZipUtils.getBinaryContent(projectUrl, function(err, data) {

			if (err) {				
				instance.@org.geowe.client.local.main.tool.project.URLProjectLoader::showAlert(Ljava/lang/String;)(err); 
				return;
			}

			try {
				$wnd.JSZip.loadAsync(data).then(function(zip) {
					return zip.file("geowe-project.prj").async("string");
				}).then(function success(jsonProject) {
					instance.@org.geowe.client.local.main.tool.project.URLProjectLoader::loadProject(Ljava/lang/String;)(jsonProject);
				}, function error(e) {
					instance.@org.geowe.client.local.main.tool.project.URLProjectLoader::showAlert(Ljava/lang/String;)(e);
				});
			} catch (e) {
				instance.@org.geowe.client.local.main.tool.project.URLProjectLoader::showAlert(Ljava/lang/String;)(e);
			}
		});		

	}-*/;

	public void open(URLProjectLoader instance, final String projectUrl) {
		autoMessageBox = new ProgressBarDialog(
				false, UIMessages.INSTANCE.processing());
		autoMessageBox.show();
		loadZip(instance, URL_BASE + projectUrl);
	}
	
	private void loadProject(String jsonProject) {
		projectLoader.load(jsonProject);
		autoMessageBox.hide();
	}
	
	private void showAlert(final String errorMsg) {
		autoMessageBox.hide();
		AlertMessageBox messageBox = new AlertMessageBox(
				UIMessages.INSTANCE.warning(), errorMsg);
		messageBox.show();
	}

}
