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
package org.geowe.client.local.main.tool.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface HelpMessages extends Messages {

	public HelpMessages INSTANCE = GWT.create(HelpMessages.class);

	String help();

	String featureInfoDialogContent();

	String geoprocessingDesc();

	String geoprocessingStep1();

	String geoprocessingStep2();

	String geoprocessingStep3();

	String geoprocessingExpectedResult();

	String tip();

	String intersectionDesc();

	String intersectionResult();

	String intersectsDesc();

	String intersectsResult();

	String differenceDesc();

	String bufferDesc();

	String bufferResult();

	String bufferTip();

	String differenceResult();

	String unionDesc();

	String unionResult();

	String unionTip();

	String symdifferenceDesc();

	String symdifferenceResult();

	String mergeDesc();

	String mergeResult();
}
