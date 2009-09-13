package org.maps.client;
/*
Copyright 2009, Mike Jacobs

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import java.util.ArrayList;
import java.util.List;

import org.maps.client.services.TRIFacilityService;
import org.maps.client.services.TRIFacilityServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;

public class TRIFacilitiesLayer extends MarkerGroup<ITRIFacility> {
	private static final String ENVIRO_FACTS_URL = "http://oaspub.epa.gov/enviro/tris_control.tris_print?tris_id=";
	private int startZoomLevel;
	private int currentZoomLevel;
	private TRIFacilityServiceAsync service = GWT.create(TRIFacilityService.class);
	MarkerClickHandler clusterClickHandler;
	MarkerClickHandler facilityClickHandler;
	
	private MarkerClickHandler getClusterClickHandler(){
		if(clusterClickHandler == null){
			clusterClickHandler = new MarkerClickHandler(){

				public void onClick(MarkerClickEvent event) {
					Marker marker = event.getSender();
					LatLng latLng = marker.getLatLng();
					int zoomLevel = getMap().getZoomLevel() + 1;
					getMap().setCenter(latLng, zoomLevel);
				}
				
			};
		}
		return clusterClickHandler;
	}
	@Override
	protected Overlay copy() {
		return new TRIFacilitiesLayer();
	}

	public int getCurrentZoomLevel() {
		return currentZoomLevel;
	}
	
	public int getStartZoomLevel() {
		return startZoomLevel;
	}

	@Override
	protected void refreshMarkers(LatLngBounds bounds,
			final IRefreshMarkersCallback callback) {

		if(getMap().getZoomLevel() < getStartZoomLevel()){
			List<Marker> newMarkers = new ArrayList<Marker>();
			callback.onRefresh(newMarkers);
			return;
		}
		setCurrentZoomLevel(getMap().getZoomLevel());
		
		AsyncCallback<List<ITRIFacility>> serviceCallback = new AsyncCallback<List<ITRIFacility>>(){
			public void onFailure(Throwable caught) {
				callback.onError(caught);
			}

			public void onSuccess(List<ITRIFacility> result) {
				for(ITRIFacility facility : result){
					MarkerOptions options = MarkerOptions.newInstance();
					options.setTitle(facility.getFacilityName());
					if(facility.getNumberOfFacilities() > 1){
						String url = "images/cluster.png";
						Icon icon = Icon.newInstance(url);
						options.setIcon(icon);
					}
					options.setDraggable(false);
					LatLng location = LatLng.newInstance(facility.getLatitude(), facility.getLongitude());
					Marker marker = new Marker(location, options);
					if(facility.getNumberOfFacilities() > 1){
						marker.addMarkerClickHandler(getClusterClickHandler());
					}
					else{
						marker.addMarkerClickHandler(getFacilityClickHandler());
					}
					register(marker, facility);
				}
				callback.onRefresh(getMarkers());
			}
		};
		
		double swLat = bounds.getSouthWest().getLatitude();
		double swLng = bounds.getSouthWest().getLongitude();
		double neLat = bounds.getNorthEast().getLatitude();
		double neLng = bounds.getNorthEast().getLongitude();
		service.get(swLat, swLng, neLat, neLng, serviceCallback);

	}
	

	protected String prepareInfoWindowContent(Marker marker,
			ITRIFacility facility) {
		String contentString = "<div class=infowindow>"
				+ facility.getFacilityName() + "</div>";
		return contentString;
	}

	private MarkerClickHandler getFacilityClickHandler() {
		if (facilityClickHandler == null) {
			facilityClickHandler = new MarkerClickHandler() {

				public void onClick(MarkerClickEvent event) {
					Marker marker = event.getSender();
					ITRIFacility f = getObject(marker);
					InfoWindow info = getMap().getInfoWindow();
					Grid g = new Grid(4, 2);
					g.setStyleName("infowindow");
					g.setText(0, 0, "Name: ");g.setText(0, 1, f.getFacilityName());
					g.setText(1, 0, "Industry: ");g.setText(1, 1, f.getIndustryName());
					g.setText(2, 0, "Address: ");g.setText(2, 1, f.getAddress());
					String url = ENVIRO_FACTS_URL+f.getFacilityID();
					Anchor anchor = new Anchor("Click to open on new page", true, url, "new");
					g.setText(3, 0, "Facility Report: ");g.setWidget(3, 1, anchor);
					InfoWindowContent content = new InfoWindowContent(g);
					info.open(marker, content);
				}
			};
		}
		return facilityClickHandler;
	}

	public void setCurrentZoomLevel(int currentZoomLevel) {
		this.currentZoomLevel = currentZoomLevel;
	}

	public void setStartZoomLevel(int startZoomLevel) {
		this.startZoomLevel = startZoomLevel;
	}

	@Override
	protected boolean shouldRefresh() {
		boolean answer = getMap().getZoomLevel() != getCurrentZoomLevel();
		//System.out.println("refreshing facilities: " + answer);
		return answer;
	}

}
