package org.geowe.client.local.main.tool.measure;

import java.math.BigDecimal;

public class MeasurementInfo {

	public static final String UNIT_METERS = "m";
	public static final String UNIT_SQUARE_METERS = "m²";
	public static final String UNIT_KILOMETERS = "km";
	public static final String UNIT_SQUARE_KILOMETERS = "Km²";

	private double measureValue;
	private String unit;

	public MeasurementInfo(double measureValue, String unit) {
		super();
		this.measureValue = measureValue;
		this.unit = unit;
	}

	public double getMeasureValue() {
		return measureValue;
	}

	public void setMeasureValue(double measureValue) {
		this.measureValue = measureValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void normalizeLength() {
		if (this.getMeasureValue() > 1000d) {
			this.measureValue = getReoundedMeasure(this.measureValue / 1000d, 3);
			this.unit = UNIT_KILOMETERS;
		} else {
			this.measureValue = getReoundedMeasure(this.measureValue, 3);
		}
	}

	public void normalizeArea() {
		if (this.getMeasureValue() > 1000000d) {
			this.measureValue = getReoundedMeasure(
					this.measureValue / 1000000d, 3);
			this.unit = UNIT_SQUARE_KILOMETERS;
		} else {
			this.measureValue = getReoundedMeasure(this.measureValue, 3);
		}
	}

	private double getReoundedMeasure(Double measure, int decimal) {
		BigDecimal bd = new BigDecimal(Double.toString(measure));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

}