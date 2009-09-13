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
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.maps.client.RSEIStateResults;
import org.maps.client.services.RSEIStateResultsService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RSEIStateResultsServiceImpl extends RemoteServiceServlet implements
	RSEIStateResultsService {

	private static final long serialVersionUID = 6550620415275984534L;
	private static final Map<String, RSEIStateResults> resultsMap = new ConcurrentHashMap<String, RSEIStateResults>();	
	private static List<RSEIStateResults> allResults;
	
	public List<RSEIStateResults> getResults() {
		return allResults;
	}
	

	public RSEIStateResults getResults(String aShortName) {
		return resultsMap.get(aShortName);
	}
	
	public void init(ServletConfig config) throws ServletException {

        // Store the ServletConfig object and log the initialization
        super.init(config);
        allResults = initialize();
        for(RSEIStateResults r : allResults){
        	initializeResult(r);
        }
    }

	private void initializeResult(RSEIStateResults r) {
		String shortName = r.getShortName(); // state short name
		resultsMap.put(shortName, r);
	}
	
	private List<RSEIStateResults> initialize(){
		List<RSEIStateResults> results = new ArrayList<RSEIStateResults>();
		InputStream inputStream = getServletContext().getResourceAsStream("/RSEIbyStateChanged.csv");
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader  = new BufferedReader(isr);
			String line = bufferedReader.readLine(); // skip header
			//read each line of text file
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				
				RSEIStateResults sr = new RSEIStateResults();
				// State
				String shortName = st.nextToken().toUpperCase().trim();
				sr.setShortName(shortName);
				// ElementCount
				st.nextToken(); // skip
				// ReleaseCount
				sr.setReleaseCount(new Integer(st.nextToken()));
				// FacilityCount
				sr.setFacilityCount(new Integer(st.nextToken()));
				// TRIPounds
				sr.setTriPounds(new Double(st.nextToken()));
				// TRIPoundsWTox
				sr.setTriPoundsWTox(new Double(st.nextToken()));
				// TRIPoundsWToxTox
				sr.setTriPoundsWToxTox(new Double(st.nextToken()));
				// ModeledPounds
				st.nextToken(); // skip
				// ModeledPoundsTox	 
				st.nextToken(); // skip
				// ModeledPoundsToxPop	
				st.nextToken(); // skip
				// FullModelChildrenUnder10	
				sr.setFullModelChildrenUnder10(new Double(st.nextToken()));
				// FullModelChildren10to17
				sr.setFullModelChildren10to17(new Double(st.nextToken()));
				// FullModelMales18to44	
				sr.setFullModelMales18to44(new Double(st.nextToken())); 
				// FullModelFemales18to44
				sr.setFullModelFemales18to44(new Double(st.nextToken())); 
				// FullModelAdults18to44
				sr.setFullModelAdults18to44(new Double(st.nextToken()));
				// FullModelAdults45To64
				sr.setFullModelAdults45To64(new Double(st.nextToken()));
				// FullModelAdultsOver65
				sr.setFullModelAdultsOver65(new Double(st.nextToken()));
				// FullModel
				sr.setFullModel(new Double(st.nextToken()));
				// FullModelBin	 
				sr.setFullModelBin(new Integer(st.nextToken()));
				// TRIPoundsWToxToxBin
				sr.setTriPoundsWToxToxBin(new Integer(st.nextToken()));
				// TRIPoundsWToxBin
				sr.setTriPoundsWToxBin(new Integer(st.nextToken()));
				// FullModelChildrenUnder10Bin	
				sr.setFullModelChildrenUnder10Bin(new Integer(st.nextToken()));
				// FullModelChildren10to17Bin	
				sr.setFullModelChildren10to17Bin(new Integer(st.nextToken()));
				// FullModelAdults18to44Bin	
				sr.setFullModelAdults18to44Bin(new Integer(st.nextToken()));
				// FullModelAdults45To64Bin	 
				sr.setFullModelAdults45To64Bin(new Integer(st.nextToken()));
				// FullModelAdultsOver65Bin
				sr.setFullModelAdultsOver65Bin(new Integer(st.nextToken()));
				results.add(sr);
			}
			//close the file
			
		} catch (Throwable e) {
			throw new RuntimeException("Something bad happened in RSEIStateResultsServiceImpl", e) ;
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
