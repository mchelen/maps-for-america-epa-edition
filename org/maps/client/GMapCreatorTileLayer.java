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
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;

public class GMapCreatorTileLayer extends TileLayer {
	private double opacity = 0.5;
	private String urlPrefix;
	private String secondaryPrefix;
	public String getSecondaryPrefix() {
		return secondaryPrefix;
	}
	public void setSecondaryPrefix(String secondaryPrefix) {
		this.secondaryPrefix = secondaryPrefix;
	}
	public int getSecondaryZoomStart() {
		return secondaryZoomStart;
	}
	public void setSecondaryZoomStart(int secondaryZoomStart) {
		this.secondaryZoomStart = secondaryZoomStart;
	}

	private int secondaryZoomStart;
	
	public GMapCreatorTileLayer(CopyrightCollection copyrights, int minResolution,
			int maxResolution, int secondaryZoomStart) {
		super(copyrights, minResolution, maxResolution);
		setSecondaryZoomStart(secondaryZoomStart);
	}
	@Override
	public double getOpacity() {
		return opacity;
	}

	@Override
	public String getTileURL(Point tile, int zoomLevel) {
		String prefix = getUrlPrefix();
		String secondaryPrefix = getSecondaryPrefix();
		if (zoomLevel >= getSecondaryZoomStart() && secondaryPrefix == null) {
			return prefix + "blank-tile.png";
		}
		else if(zoomLevel >= getSecondaryZoomStart() && secondaryPrefix != null){
			prefix = secondaryPrefix;
		}
		//System.out.println("prefix is: " + prefix);
		final LatLngBounds mapBounds=LatLngBounds.newInstance(LatLng.newInstance(17.884813,-179.14734),LatLng.newInstance(71.35256,179.77847));
		
		double c = Math.pow(2, zoomLevel);
		double x = 360 / c * tile.getX() - 180;
		double y = 180 - 360 / c * tile.getY();
		double x2 = x + 360 / c;
		double y2 = y - 360 / c;
		double lon = x; // Math.toRadians(x); //would be lon=x+lon0, but lon0=0
						// degrees
		double lat = (2.0 * Math.atan(Math.exp(y / 180 * Math.PI)) - Math.PI / 2.0)
				* 180 / Math.PI; // in degrees
		double lon2 = x2;
		double lat2 = (2.0 * Math.atan(Math.exp(y2 / 180 * Math.PI)) - Math.PI / 2.0)
				* 180 / Math.PI; // in degrees
		LatLngBounds tileBounds = LatLngBounds.newInstance(LatLng.newInstance(lat2, lon), LatLng.newInstance(lat, lon2));
		if (!tileBounds.intersects(mapBounds)) { return prefix + "blank-tile.png"; };
        double d=tile.getX();
        double e=tile.getY();
        String f="t";
        for(int g=0; g < zoomLevel; g++){
            c=c/2;
            if(e<c){
                if(d<c){
                	f=f+"q";
                }
                else{
                	f=f+"r";
                	d-=c;
                }
            }
            else{
                if(d<c){
                	f=f+"t";
                	e-=c;
                }
                else{
                	f=f+"s";
                	d-=c;
                	e-=c;
                }
            }
        }
        return prefix+f+".png";
    }

	public String getUrlPrefix() {
		return urlPrefix;
	}

	@Override
	public boolean isPng() {
		return false;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

}
