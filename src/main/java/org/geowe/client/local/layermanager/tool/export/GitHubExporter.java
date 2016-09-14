package org.geowe.client.local.layermanager.tool.export;

import java.util.List;

import com.sencha.gxt.widget.core.client.info.Info;

public class GitHubExporter implements Exporter {

	@Override
	public void export(List<String> parameters) {
		Info.display("GitHub", "GitHub");

	}

}
