package org.geowe.client.local.layermanager.tool.create.vector.source;

public abstract class AbstractLayerVectorSource implements LayerVectorSource{
	private String layerName;
	private String projection;
	private String format;
	
	@Override
	public void setLayerName(String layerName) {
		this.layerName = layerName;		
	}

	@Override
	public void setProjection(String projection) {
		this.projection = projection;		
	}

	@Override
	public void setFormat(String format) {
		this.format = format;		
	}

	@Override
	public String getLayerName() {
		return layerName;
	}

	@Override
	public String getProjection() {
		return projection;
	}

	@Override
	public String getFormat() {
		return format;
	}

}
