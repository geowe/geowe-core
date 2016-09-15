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
package org.geowe.client.local;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageProvider extends ClientBundle {

	ImageProvider INSTANCE = GWT.create(ImageProvider.class);

	@Source("images/ic_base_layer-16.png")
	ImageResource baseLayer();

	@Source("images/ic_add_layer-24.png")
	ImageResource addLayer();

	@Source("images/ic_add_layer-16.png")
	ImageResource addLayer16();

	@Source("images/ic_delete_layer-24.png")
	ImageResource deleteLayer();

	@Source("images/ic_zoom_to_layer-24.png")
	ImageResource zoomToLayer();

	@Source("images/ic_earth_gray-24.png")
	ImageResource zoomToMaxExtend32();

	@Source("images/ic_menu-32.png")
	ImageResource menuIcon();

	@Source("images/logo-geowe.png")
	ImageResource logoGeowe();

	@Source("images/ic_logo-geowe.jpg")
	ImageResource menulogoGeowe();

	@Source("images/ic_capas-24.png")
	ImageResource layerIcon();

	@Source("images/ic_catalog-24.png")
	ImageResource layerCatalog();

	@Source("images/ic_info-24.png")
	ImageResource info32();

	@Source("images/ic_info-24.png")
	ImageResource info24();

	@Source("images/ic_zoom_box-24.png")
	ImageResource zoomBoxButton();

	@Source("images/ic_zoom_out-24.png")
	ImageResource zoomOut();

	@Source("images/ic_zoom_in-24.png")
	ImageResource zoomIn();

	@Source("images/ic_select-24.png")
	ImageResource select();

	@Source("images/ic_pan-24.png")
	ImageResource pan();

	@Source("images/ic_zoom-next_24.png")
	ImageResource zoomNext();

	@Source("images/ic_zoom-previous_24.png")
	ImageResource zoomPrevius();

	@Source("images/ic_zoom_selection-24.png")
	ImageResource zoomToSelection();

	@Source("images/ic_measure_lenght-24.png")
	ImageResource measureLength32();

	@Source("images/ic_circle-24.png")
	ImageResource circle();

	@Source("images/ic_point-24.png")
	ImageResource point();

	@Source("images/ic_lineString-24.png")
	ImageResource lineString();

	@Source("images/ic_polygon-24.png")
	ImageResource polygon();

	@Source("images/ic_hole_polygon-24.png")
	ImageResource holePolygon();

	@Source("images/ic_regular_polygon-24.png")
	ImageResource regularPolygon();

	@Source("images/ic_drag-24.png")
	ImageResource drag();

	@Source("images/ic_rotate-24.png")
	ImageResource rotate();

	@Source("images/ic_scale-24.png")
	ImageResource scale();

	@Source("images/ic_delete_vector-24.png")
	ImageResource deleteVector();
	
	@Source("images/ic_delete_vector-24.png")
	ImageResource deleteVector24();

	@Source("images/ic_snap_vertex-24.png")
	ImageResource snapVertex();

	@Source("images/ic_reshape_vertex-24.png")
	ImageResource reshape();

	@Source("images/ic_layer-16.png")
	ImageResource layer16();

	@Source("images/ic_style-24.png")
	ImageResource style24();

	@Source("images/ic_style-24.png")
	ImageResource style32();

	@Source("images/ic_facebook-32.png")
	ImageResource facebook();

	@Source("images/ic_google_plus-32.png")
	ImageResource googlePlus();

	@Source("images/ic_linkedIn-32.png")
	ImageResource linkedIn();
		
	@Source("images/ic_camera-32.png")
	ImageResource screemshot();

	@Source("images/ic_home-32.png")
	ImageResource home();

	@Source("images/ic_mail-32.png")
	ImageResource mail();

	@Source("images/ic_cloud_download-32.png")
	ImageResource downloadCloud32();

	@Source("images/ic_download-32.png")
	ImageResource download32();

	@Source("images/ic_cloud_download-24.png")
	ImageResource download24();
	
	@Source("images/ic_cloud_download_blue-24.png")
	ImageResource downloadBlue24();

	@Source("images/ic_cloud_download-16.png")
	ImageResource download16();

	@Source("images/ic_bug-32.png")
	ImageResource bug();

	@Source("images/ic_question-32.png")
	ImageResource question();

	@Source("images/ic_catastro-16.png")
	ImageResource catastro16();

	@Source("images/ic_osm-16.png")
	ImageResource osm16();

	@Source("images/ic_google_maps-16.png")
	ImageResource google16();
	
	@Source("images/ic_bing_maps-16.png")
	ImageResource bing16();	

	@Source("images/ic_left-24.png")
	ImageResource left();	

	@Source("images/ic_right-24.png")
	ImageResource right();	
	
	@Source("images/ic_rejilla-24.png")
	ImageResource graticule();
	
	@Source("images/ic_print-24.png")
	ImageResource print();
	
	@Source("images/ic_buffer-24.png")
	ImageResource buffer32();
	
	@Source("images/ic_union-24.png")
	ImageResource union32();
	
	@Source("images/ic_divide-24.png")
	ImageResource divide32();
	
	@Source("images/ic_measure_area-24.png")
	ImageResource areaMeasure32();
	
	@Source("images/ic_select-24.png")
	ImageResource select24();
	
	@Source("images/ic_zoom_to_feature-24.png")
	ImageResource zoomToFeature24();
	
	@Source("images/ic_twiter-32.png")
	ImageResource twiter();
	
	@Source("images/ic_circle_vertex-16.png")
	ImageResource circleVertex();
	
	@Source("images/ic_square_vertex-16.png")
	ImageResource squareVertex();	
	
	@Source("images/ic_triangle_vertex-16.png")
	ImageResource triangleVertex();
	
	@Source("images/ic_star_vertex-16.png")
	ImageResource starVertex();
	
	@Source("images/ic_cross_vertex-16.png")
	ImageResource crossVertex();
	
	@Source("images/ic_axis_vertex-16.png")
	ImageResource axisVertex();	
	
	@Source("images/ic_tools_left-24.png")
	ImageResource toolLeft24();	
	
	@Source("images/ic_tools_right-24.png")
	ImageResource toolRight24();	
	
	@Source("images/ic_copy_layer-24.png")
	ImageResource copyLayer24();
	
	@Source("images/ic_attr_list-24.png")
	ImageResource attributeList24();	
	
	@Source("images/ic_search-geocoding-24.png")
	ImageResource searchGeocoding24();
	
	@Source("images/ic_map_crosshairs-24.png")
	ImageResource geolocation24();
	
	@Source("images/ic_measure_element-24.png")
	ImageResource measureElement32();
	
	@Source("images/ic_download-16.png")
	ImageResource downloadFile16();
	
	@Source("images/ic_map_location_arrow-24.png")
	ImageResource geocoding24();
	
	@Source("images/ic_esri-.jpg")
	ImageResource esri();
	
	@Source("images/ic_esri-16.png")
	ImageResource esri16();
	
	@Source("images/ic_search-24.png")
	ImageResource search32();
	
	@Source("images/ic_edit-24.png")
	ImageResource edit32();
	
	@Source("images/ic_ideandalucia-16.png")
	ImageResource ideAndalucia16();

	@Source("images/ic_sigpac-16.png")
	ImageResource sigpac16();
	
	@Source("images/ic_comment-24.png")
	ImageResource tooltip();

	@Source("images/ic_ellipsis-h-24.png")
	ImageResource ellipsisH24();

	@Source("images/ic_file-text-24.png")
	ImageResource fileText24();
	
	@Source("images/ic_envelope-24.png")
	ImageResource envelope32();
	
	@Source("images/ic_centroid-24.png")
	ImageResource centroid32();

	@Source("images/ic_analysis-24.png")
	ImageResource analysis32();

	@Source("images/ic_w3w-24.png")
	ImageResource w3w24();
	
	@Source("images/ic_w3w_red-24.png")
	ImageResource w3wRed24();
	
	@Source("images/ic_w3w_green-24.png")
	ImageResource w3wGreen24();

	@Source("images/ic_w3w_blue-24.png")
	ImageResource w3wBlue24();

	@Source("images/ic_w3w_shadow-30.png")
	ImageResource w3wShadow();

	@Source("images/ic_copy_element-24.png")
	ImageResource copyElement32();
	
	@Source("images/ic_ign-16.png")
	ImageResource ign16();
	
	@Source("images/ic_transform-24.png")
	ImageResource transform24();
	
	@Source("images/ic_external_link-16.png")
	ImageResource externalLink16();
	
	@Source("images/ic_sofrito-16.png")
	ImageResource elsofrito16();
	
	@Source("images/ic_validation-24.png")
	ImageResource validation24();
	
	@Source("images/ic_validation-16.png")
	ImageResource validation16();
	
	@Source("images/ic_github-32.png")
	ImageResource github32();
	
}
