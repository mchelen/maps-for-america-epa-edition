package org.maps.server;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.maps.client.ITRIFacility;
import org.maps.client.TRIFacility;
import org.maps.client.TRIFacilityCluster;
import org.maps.client.services.TRIFacilityService;

import com.bbn.openmap.util.quadtree.QuadTree;
import com.bbn.openmap.util.quadtree.QuadTreeNode;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TRIFacilityServiceImpl extends RemoteServiceServlet implements
TRIFacilityService {
	private final QuadTree quadTree = new QuadTree(90.0f, -180.0f, -90.0f, 180.0f, 100, QuadTreeNode.NO_MIN_SIZE);
	private static final long serialVersionUID = 8440964648721410480L;
	private final Map<String, List<ITRIFacility>> facilitiesByState = new ConcurrentHashMap<String, List<ITRIFacility>>();
	
	
	public List<ITRIFacility> get(double swLat, double swLng, double neLat, double neLng) {
		return cluster(swLat, swLng, neLat, neLng, 3, 4);
	}
	
	@SuppressWarnings("unchecked")
	private List<ITRIFacility> cluster(double swLat, double swLng, double neLat,
			double neLng, int latGridSize, int lngGridSize) {
		Vector<TRIFacility> v = quadTree.get((float)neLat, (float)swLng, (float)swLat, (float)neLng);
		/*
		 * 2 dimensional arrays are addressed via [row][column] or [y][x]
		 * Latitude is in the y direction
		 * Longitude is in the x direction
		 */
		List<ITRIFacility>[][] bins = (List<ITRIFacility>[][])new List[latGridSize][lngGridSize];
		double latitudeStride = (neLat-swLat)/latGridSize;
		double longitudeStride = (neLng-swLng)/lngGridSize;
		
		for (TRIFacility f : v) {
			double relativeLatitude = f.getLatitude() - swLat;
			double relativeLongitude = f.getLongitude() - swLng;
			int latIndex = (int) Math.floor(relativeLatitude/latitudeStride);
			int lngIndex = (int) Math.floor(relativeLongitude/longitudeStride);
			
			if (latIndex >= 0 && lngIndex >= 0) {
				List<ITRIFacility> bin = bins[latIndex][lngIndex];
				if (bin == null) {
					bin = new ArrayList<ITRIFacility>();
					bins[latIndex][lngIndex] = bin;
				}
				bin.add(f);
			}
		}
		
		List<ITRIFacility> results = new ArrayList<ITRIFacility>();
		for(int y = 0; y< latGridSize; y++){
			for(int x = 0; x < lngGridSize; x++){
				List<ITRIFacility> bin = bins[y][x];
				if(bin != null){
					if(bin.size() == 1){
						results.add(bin.get(0));
					}
					else{
						TRIFacilityCluster cluster = new TRIFacilityCluster();
						int size = bin.size();
						cluster.setFacilityName("A cluster of " + size + " facilities");
						cluster.setNumberOfFacilities(size);
						double lat = 0.0;
						double lng = 0.0;
						for(ITRIFacility f : bin){
							lat = lat + f.getLatitude();
							lng = lng + f.getLongitude();
						}
						cluster.setLatitude(lat/size);
						cluster.setLongitude(lng/size);
						results.add(cluster);
					}
				}
			}
		}
		return results;
	}


	public List<ITRIFacility> get(String stateShortName) {
		List<ITRIFacility> facilities = facilitiesByState.get(stateShortName);
		return facilities;
	}

	public void init(ServletConfig config) throws ServletException {

        // Store the ServletConfig object and log the initialization
        super.init(config);
        List<TRIFacility> facilities = initialize();
        for(TRIFacility f : facilities){
        	initialize(f);
        }
    }
	
	private void initialize(TRIFacility f) {
		boolean success = quadTree.put((float)f.getLatitude(), (float)f.getLongitude(), f);
		if(!success){
			throw new RuntimeException("Unable to insert " + f.getFacilityName());
		}
		List<ITRIFacility> facilities = facilitiesByState.get(f.getState());
		if(facilities == null){
			facilities = new ArrayList<ITRIFacility>();
			facilitiesByState.put(f.getState(), facilities);
		}
		facilities.add(f);
	}
	
	private List<TRIFacility> initialize() {
		List<TRIFacility> results = new ArrayList<TRIFacility>();
		InputStream inputStream = getServletContext().getResourceAsStream("/TRIFacilities2006.csv");
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader  = new BufferedReader(isr);
			String line = bufferedReader.readLine(); // skip header
			//read each line of text file
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				
				TRIFacility f = new TRIFacility();
				/* TRIF ID	
				 * Industry	
				 * Name	
				 * Address	
				 * City	
				 * County	
				 * State	
				 * Fips	
				 * Latitude	
				 * Longitude
				 */
				f.setFacilityID(st.nextToken());
				f.setIndustryName(st.nextToken());
				f.setFacilityName(st.nextToken());
				f.setAddress(st.nextToken());
				f.setCity(st.nextToken());
				f.setCounty(st.nextToken());
				f.setState(st.nextToken());
				f.setFips(st.nextToken());
				String latitude = st.nextToken();
				Double lat = new Double(latitude);
				f.setLatitude(lat);
				String longitude = st.nextToken();
				f.setLongitude(new Double(longitude));
				results.add(f);
			}
			//close the file
			
		} catch (Throwable e) {
			throw new RuntimeException("Something bad happened in TRIFacilityServiceImpl", e) ;
		}
		finally{
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// gulp
				}
			}
		}
		return results;
	}

}
