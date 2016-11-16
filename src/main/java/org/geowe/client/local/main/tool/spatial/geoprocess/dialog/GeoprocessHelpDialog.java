package org.geowe.client.local.main.tool.spatial.geoprocess.dialog;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.main.tool.help.HelpMessages;
import org.geowe.client.local.messages.UIMessages;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

@ApplicationScoped
public class GeoprocessHelpDialog extends Dialog {

	public interface VectorLayerTemplate extends XTemplates {
		@XTemplate(source = "GeoprocessHelpTemplate.html")
		public SafeHtml renderTemplate(HelpMessages helpMessages, UIMessages uiMessages);
	}

	public GeoprocessHelpDialog() {
		super();
		setHeadingText(HelpMessages.INSTANCE.help());
		setPredefinedButtons(PredefinedButton.CLOSE);
		setSize("420px", "420px");
		setResizable(true);
		setClosable(true);
		setBodyStyleName("pad-text");
		getBody().addClassName("pad-text");
		add(getHelpPanel(getHtmlReport()));
	}

	private HorizontalLayoutContainer getHelpPanel(final HTML data) {
		HorizontalLayoutContainer panel = new HorizontalLayoutContainer();
		panel.setSize("400px", "400px");
		panel.add(data);
		ScrollSupport scrollSupport = panel.getScrollSupport();
		scrollSupport.setScrollMode(ScrollMode.AUTO);
		return panel;
	}
	
	private HTML getHtmlReport() {
		VectorLayerTemplate template = GWT.create(VectorLayerTemplate.class);
		VectorFeature[] features = null;
		if (features == null) {
			features = new ArrayList<VectorFeature>()
					.toArray(new VectorFeature[0]);
		}
		return new HTML(template.renderTemplate(HelpMessages.INSTANCE, UIMessages.INSTANCE));
	}

}
