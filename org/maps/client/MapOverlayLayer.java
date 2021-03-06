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
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Overlay;

public class MapOverlayLayer implements IMapLayer {
	private String displayName;
	private Overlay overlay;
	private boolean enabled;
	public void setOpacity(double value) {
		
	}
	public String getDisplayName() {
		return displayName;
	}
	public Overlay getOverlay() {
		return overlay;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;
	}
	
	public void hide(MapWidget map) {
		setEnabled(false);
		map.removeOverlay(getOverlay());
	}
	
	public void show(MapWidget map) {
		setEnabled(true);
		map.addOverlay(getOverlay());
	}
	
}
