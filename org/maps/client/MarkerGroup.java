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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;

public abstract class MarkerGroup<T> extends Overlay implements IMapLayer {
	private List<Marker> markers;
	private boolean enabled;
	private MapWidget map;
	private IRefreshMarkersCallback refreshCallback;
	private String displayName;
	private Map<Marker,T> markerMap;
	
	public void setOpacity(double value) {
		// not supported for markers
	}
	
	public MarkerGroup(){
		refreshCallback = new IRefreshMarkersCallback(){
			public void onError(Throwable error) {
				handleError(error);
			}

			public void onRefresh(List<Marker> markers) {
				for(Marker marker : markers){
					getMap().addOverlay(marker);
				}
			}
		};
	}
	abstract protected Overlay copy();
	abstract protected boolean shouldRefresh();


	public String getDisplayName(){
		return displayName;
	}
	
	protected MapWidget getMap() {
		return map;
	}
	
//	protected String prepareInfoWindowContent(Marker marker, MapPoint point){
//		String contentString = "<div class=infowindow>" + point.getDescription()+"</div>";
//		return contentString;
//	}
	
//	private MarkerClickHandler getMarkerClickHandler(){
//		if(markerClickHandler == null){
//			markerClickHandler = new MarkerClickHandler(){
//
//				public void onClick(MarkerClickEvent event) {
//					Marker marker = event.getSender();
//					InfoWindow info = getMap().getInfoWindow();
//					MapPoint point = getPoint(marker);
//					String contentString = prepareInfoWindowContent(marker, point);
//					InfoWindowContent content = new InfoWindowContent(contentString);
//					info.open(marker, content);
//				}
//				
//			};
//		}
//		return markerClickHandler;
//	}
	private Map<Marker, T> getMarkerMap() {
		if(markerMap == null){
			markerMap = new HashMap<Marker, T>();
		}
		return markerMap;
	}

	protected List<Marker> getMarkers() {
		if(markers == null){
			markers = new ArrayList<Marker>();
		}
		return markers;
	}

	protected T getObject(Marker marker){
		return getMarkerMap().get(marker);
	}

	private void handleError(Throwable error) {
		final Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent ce) {
				ce.getMessageBox().close();
			}
		}; 
		MessageBox.alert("We Encountered a Problem", error.getMessage(), listener);
	}

	public void hide(MapWidget map) {
		setEnabled(false);
		map.removeOverlay(this);
	}
	
	protected void initialize(MapWidget map){
		setMap(map);
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	protected void redraw(boolean force){
		refreshMarkers();
	}

	private void refreshMarkers(){
		if(shouldRefresh()){
			remove();
			refreshMarkers(getMap().getBounds(), refreshCallback);
		}
	}

	abstract protected void refreshMarkers(LatLngBounds bounds, IRefreshMarkersCallback callback);
	
	protected void register(Marker marker, T anObject){
		getMarkers().add(marker);
		getMarkerMap().put(marker, anObject);
	}

	
	protected void remove(){
		for(Marker marker : getMarkers()){
			getMap().removeOverlay(marker);
			unregister(marker);
		}
		getMarkers().clear();
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setEnabled(boolean aBoolean) {
		enabled = aBoolean;
	}
	
	protected void setMap(MapWidget map) {
		this.map = map;
	}
	
	protected void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}
	
	public void show(MapWidget map) {
		setEnabled(true);
		map.addOverlay(this);
	}
	
	private void unregister(Marker marker){
		getMarkerMap().remove(marker);
		// TODO: marker.removeMarkerClickHandler
	}

}
