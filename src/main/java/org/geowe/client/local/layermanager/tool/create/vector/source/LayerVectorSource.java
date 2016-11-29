package org.geowe.client.local.layermanager.tool.create.vector.source;

public interface LayerVectorSource {
	public void setLayerName(String layerName);
	public void setProjection(String projection);
	public void setFormat(String format);
	
	public String getLayerName();
	public String getProjection();
	public String getFormat();
}
