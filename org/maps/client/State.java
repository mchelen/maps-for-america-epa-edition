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
import java.util.HashMap;
import java.util.Map;

public class State {
	private static Map<String, State> fipsToStateMap = new HashMap<String, State>();
	private static Map<String, State> shortToStateMap = new HashMap<String, State>();
	
	private String shortName, longName, fips;
	
	public State(String shortName, String fips, String longName ){
		this.shortName = shortName;
		this.fips = fips;
		this.longName = longName;
	}
	
	public static void initialize(){
		register(new State("AK","02","ALASKA"));
		register(new State("AL","01","ALABAMA"));
		register(new State("AR","05","ARKANSAS"));
		register(new State("AS","60","AMERICAN SAMOA"));
		register(new State("AZ","04","ARIZONA"));
		register(new State("CA","06","CALIFORNIA"));
		register(new State("CO","08","COLORADO"));
		register(new State("CT","09","CONNECTICUT"));
		register(new State("DC","11","DISTRICT OF COLUMBIA"));
		register(new State("DE","10","DELAWARE"));
		register(new State("FL","12","FLORIDA"));
		register(new State("GA","13","GEORGIA"));
		register(new State("GU","66","GUAM"));
		register(new State("HI","15","HAWAII"));
		register(new State("IA","19","IOWA"));
		register(new State("ID","16","IDAHO"));
		register(new State("IL","17","ILLINOIS"));
		register(new State("IN","18","INDIANA"));
		register(new State("KS","20","KANSAS"));
		register(new State("KY","21","KENTUCKY"));
		register(new State("LA","22","LOUISIANA"));
		register(new State("MA","25","MASSACHUSETTS"));
		register(new State("MD","24","MARYLAND"));
		register(new State("ME","23","MAINE"));
		register(new State("MI","26","MICHIGAN"));
		register(new State("MN","27","MINNESOTA"));
		register(new State("MO","29","MISSOURI"));
		register(new State("MS","28","MISSISSIPPI"));
		register(new State("MT","30","MONTANA"));
		register(new State("NC","37","NORTH CAROLINA"));
		register(new State("ND","38","NORTH DAKOTA"));
		register(new State("NE","31","NEBRASKA"));
		register(new State("NH","33","NEW HAMPSHIRE"));
		register(new State("NJ","34","NEW JERSEY"));
		register(new State("NM","35","NEW MEXICO"));
		register(new State("NV","32","NEVADA"));
		register(new State("NY","36","NEW YORK"));
		register(new State("OH","39","OHIO"));
		register(new State("OK","40","OKLAHOMA"));
		register(new State("OR","41","OREGON"));
		register(new State("PA","42","PENNSYLVANIA"));
		register(new State("PR","72","PUERTO RICO"));
		register(new State("RI","44","RHODE ISLAND"));
		register(new State("SC","45","SOUTH CAROLINA"));
		register(new State("SD","46","SOUTH DAKOTA"));
		register(new State("TN","47","TENNESSEE"));
		register(new State("TX","48","TEXAS"));
		register(new State("UT","49","UTAH"));
		register(new State("VA","51","VIRGINIA"));
		register(new State("VI","78","VIRGIN ISLANDS"));
		register(new State("VT","50","VERMONT"));
		register(new State("WA","53","WASHINGTON"));
		register(new State("WI","55","WISCONSIN"));
		register(new State("WV","54","WEST VIRGINIA"));
		register(new State("WY","56","WYOMING"));
	}
	
	public static void register(State aState){
		fipsToStateMap.put(aState.getFips(), aState);
		shortToStateMap.put(aState.getShortName(), aState);
	}
	public static State getByShortName(String aName){
		return shortToStateMap.get(aName.toUpperCase());
	}
	public static State getByFips(String aFips){
		return fipsToStateMap.get(aFips);
	}
	public String getDisplayName(){
		return getLongName();
	}
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getFips() {
		return fips;
	}

	public void setFips(String fips) {
		this.fips = fips;
	}
}
