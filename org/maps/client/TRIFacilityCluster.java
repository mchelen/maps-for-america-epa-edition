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
public class TRIFacilityCluster extends TRIFacility implements ITRIFacility {

	private static final long serialVersionUID = -5761152502677889094L;
	private int numberOfFacilities;
	
	public void setNumberOfFacilities(int numberOfFacilities) {
		this.numberOfFacilities = numberOfFacilities;
	}

	public int getNumberOfFacilities() {
		return numberOfFacilities;
	}


}
