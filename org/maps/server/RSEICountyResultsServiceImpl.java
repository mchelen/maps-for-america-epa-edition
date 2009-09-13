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

import org.maps.client.RSEICountyResults;
import org.maps.client.services.RSEICountyResultsService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RSEICountyResultsServiceImpl extends RemoteServiceServlet implements
	RSEICountyResultsService {
	private static final long serialVersionUID = -5526257811112883918L;
	private static final Map<String, List<RSEICountyResults>> resultsMap = new ConcurrentHashMap<String, List<RSEICountyResults>>(); 
	

	public List<RSEICountyResults> getResults(String aShortName) {
		List<RSEICountyResults> stateResults = resultsMap.get(aShortName);
		return stateResults;
	}
	
	public void init(ServletConfig config) throws ServletException {

        // Store the ServletConfig object and log the initialization
        super.init(config);
        List<RSEICountyResults> results = initialize();
        for(RSEICountyResults r : results){
        	initializeResult(r);
        }
    }

	private void initializeResult(RSEICountyResults r) {
		String shortName = r.getShortName(); // state short name
		List<RSEICountyResults> stateResults = resultsMap.get(shortName);
		if(stateResults == null){
			stateResults = new ArrayList<RSEICountyResults>();
			resultsMap.put(shortName, stateResults);
		}
		stateResults.add(r);
	}
	
	private List<RSEICountyResults> initialize(){
		List<RSEICountyResults> results = new ArrayList<RSEICountyResults>();
		InputStream inputStream = getServletContext().getResourceAsStream("/RSEIbyCountyChanged.csv");
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader  = new BufferedReader(isr);
			String line = bufferedReader.readLine(); // skip header
			//read each line of text file
			while ((line = bufferedReader.readLine()) != null) {
				Object token = null;
				StringTokenizer tokenizer = new StringTokenizer(line, ",");
				RSEICountyResults cr = new RSEICountyResults();
				// County
				token = tokenizer.nextToken();
				//token = tokenizer.nextToken(); // comma in data - converted to ;
				// State	
				cr.setShortName(tokenizer.nextToken());
				// Count of Elements
				tokenizer.nextToken(); // skip
				// Count of Releases
				cr.setReleaseCount(new Integer(tokenizer.nextToken()));
				// Count of Facilities	
				cr.setFacilityCount(new Integer(tokenizer.nextToken()));
				// TRI Pounds
				cr.setTriPounds(new Double(tokenizer.nextToken()));
				// TRI Pounds with Toxicity Weights
				cr.setTriPoundsWTox(new Double(tokenizer.nextToken()));
				// Hazard
				cr.setTriPoundsWToxTox(new Double(tokenizer.nextToken()));
				// Modeled Pounds
				token = tokenizer.nextToken(); // skip
				// Modeled Hazard	
				token = tokenizer.nextToken(); // skip
				// Modeled Hazard*Pop	
				token = tokenizer.nextToken(); // skip
				// Risk-related Results - Children Under 10	
				cr.setFullModelChildrenUnder10(new Double(tokenizer.nextToken()));
				// Risk-related Results - Children 10 to 17	
				cr.setFullModelChildren10to17(new Double(tokenizer.nextToken()));
				// Risk-related Results - Males 18 to 44
				token = tokenizer.nextToken(); // skip
				// Risk-related Results - Females 18 to 44
				token = tokenizer.nextToken(); // skip
				// Risk-related Results - Adults 18 to 44
				cr.setFullModelAdults18to44(new Double(tokenizer.nextToken()));
				// Risk-related Results - Adults 45 to 64	
				cr.setFullModelAdults45To64(new Double(tokenizer.nextToken()));
				// Risk-related Results - Adults 65 and Older	
				cr.setFullModelAdultsOver65(new Double(tokenizer.nextToken()));
				// Risk-related Results	
				cr.setFullModel(new Double(tokenizer.nextToken()));
				// Display Name
				cr.setCountyName(tokenizer.nextToken());
				// State FIPS
				token = tokenizer.nextToken(); // skip
				// County FIPS	
				token = tokenizer.nextToken(); // skip
				// FullModelBin
				cr.setFullModelBin(new Integer(tokenizer.nextToken()));
				// TRIPoundsWToxToxBin
				cr.setTriPoundsWToxToxBin(new Integer(tokenizer.nextToken()));
				// TRIPoundsWToxBin	
				cr.setTriPoundsWToxBin(new Integer(tokenizer.nextToken()));
				// TRIPoundsBin	 
				cr.setTriPoundsBin(new Integer(tokenizer.nextToken()));
				// FullModelChildrenUnder10Bin
				cr.setFullModelChildrenUnder10Bin(new Integer(tokenizer.nextToken()));
				// FullModelChildren10to17Bin
				cr.setFullModelChildren10to17Bin(new Integer(tokenizer.nextToken()));
				// FullModelAdults18to44Bin
				cr.setFullModelAdults18to44Bin(new Integer(tokenizer.nextToken()));
				// FullModelAdults45To64Bin	
				cr.setFullModelAdults45To64Bin(new Integer(tokenizer.nextToken()));
				// FullModelAdultsOver65Bin
				cr.setFullModelAdultsOver65Bin(new Integer(tokenizer.nextToken()));

				results.add(cr);
			}
			
		} catch (Throwable e) {
			throw new RuntimeException("Something bad happened in RSEICountyResultsServiceImpl", e) ;
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
