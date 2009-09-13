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
import java.io.Serializable;

public class TRIFacility implements Serializable, ITRIFacility{

	private static final long serialVersionUID = 6631299856077646173L;
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
	private String facilityID;
	private String industryName;
	private String facilityName;
	private String address;
	private String city;
	private String county;
	private String state;
	private String fips;
	private double latitude;
	private double longitude;
	
	public int getNumberOfFacilities(){
		return 1;
	}
	public String toString(){
		return getFacilityName() + " lat: " + getLatitude() + " lon:" + getLongitude();
	}
	public String getAddress() {
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getCounty() {
		return county;
	}
	public String getFacilityID() {
		return facilityID;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public String getFips() {
		return fips;
	}
	public String getIndustryName() {
		return industryName;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getState() {
		return state;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public void setFacilityID(String facilityID) {
		this.facilityID = facilityID;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	public void setFips(String fips) {
		this.fips = fips;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setState(String state) {
		this.state = state;
	}
}
