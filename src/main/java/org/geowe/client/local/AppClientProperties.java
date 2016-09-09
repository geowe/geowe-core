package org.geowe.client.local;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.ui.shared.JSONMap;

@ApplicationScoped
public class AppClientProperties {
	private JSONMap props;
	
	public AppClientProperties() {
	
		props = JSONMap.create(ConfigResourceProvider.INSTANCE.config().getText());
	}
	
	public String getStringValue(String field) {
		return props.get(field);
	}
	
	public float getFloatValue(String field) {
		return Float.parseFloat(props.get(field));
	}
	
	public double getDoubleValue(String field) {
		return Double.parseDouble(props.get(field));
	}
	
	public int getIntValue(String field) {
		return Integer.parseInt(props.get(field));
	}
}
