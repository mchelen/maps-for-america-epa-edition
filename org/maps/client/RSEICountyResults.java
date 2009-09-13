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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RSEICountyResults implements Serializable {

	private static final long serialVersionUID = 3596514564104159853L;
	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent private String shortName;
	@Persistent private String countyName;
	@Persistent private Double fullModel;
	@Persistent private Integer fullModelBin;
	@Persistent private Double fullModelChildrenUnder10;
	@Persistent private Double fullModelChildren10to17;
	@Persistent private Double fullModelMales18to44;
	@Persistent private Double fullModelFemales18to44;
	@Persistent private Double fullModelAdults18to44;
	@Persistent private Double fullModelAdults45To64;
	@Persistent private Double fullModelAdultsOver65;
	@Persistent private Integer fullModelChildrenUnder10Bin;
	@Persistent private Integer fullModelChildren10to17Bin;
	@Persistent private Integer fullModelMales18to44Bin;
	@Persistent private Integer fullModelFemales18to44Bin;
	@Persistent private Integer fullModelAdults18to44Bin;
	@Persistent private Integer fullModelAdults45To64Bin;
	@Persistent private Integer fullModelAdultsOver65Bin;
	@Persistent private Double triPounds;
	@Persistent private Double triPoundsWTox;
	@Persistent private Double triPoundsWToxTox;
	@Persistent private Integer triPoundsBin;
	@Persistent private Integer triPoundsWToxBin;
	@Persistent private Integer triPoundsWToxToxBin;
	@Persistent private Integer releaseCount;
	@Persistent private Integer facilityCount;
	public String getCountyName() {
		return countyName;
	}
	public Integer getFacilityCount() {
		return facilityCount;
	}
	
	public Double getFullModel() {
		return fullModel;
	}
	public Double getFullModelAdults18to44() {
		return fullModelAdults18to44;
	}
	public Integer getFullModelAdults18to44Bin() {
		return fullModelAdults18to44Bin;
	}

	public Double getFullModelAdults45To64() {
		return fullModelAdults45To64;
	}
	public Integer getFullModelAdults45To64Bin() {
		return fullModelAdults45To64Bin;
	}
	
	public Double getFullModelAdultsOver65() {
		return fullModelAdultsOver65;
	}
	public Integer getFullModelAdultsOver65Bin() {
		return fullModelAdultsOver65Bin;
	}

	public Integer getFullModelBin() {
		return fullModelBin;
	}

	public Double getFullModelChildren10to17() {
		return fullModelChildren10to17;
	}

	public Integer getFullModelChildren10to17Bin() {
		return fullModelChildren10to17Bin;
	}

	public Double getFullModelChildrenUnder10() {
		return fullModelChildrenUnder10;
	}

	public Integer getFullModelChildrenUnder10Bin() {
		return fullModelChildrenUnder10Bin;
	}

	public Double getFullModelFemales18to44() {
		return fullModelFemales18to44;
	}

	public Integer getFullModelFemales18to44Bin() {
		return fullModelFemales18to44Bin;
	}

	public Double getFullModelMales18to44() {
		return fullModelMales18to44;
	}

	public Integer getFullModelMales18to44Bin() {
		return fullModelMales18to44Bin;
	}

	public Long getId() {
		return id;
	}

	public Integer getReleaseCount() {
		return releaseCount;
	}

	public String getShortName() {
		return shortName;
	}

	public Double getTriPounds() {
		return triPounds;
	}

	public Integer getTriPoundsBin() {
		return triPoundsBin;
	}

	public Double getTriPoundsWTox() {
		return triPoundsWTox;
	}

	public Integer getTriPoundsWToxBin() {
		return triPoundsWToxBin;
	}

	public Double getTriPoundsWToxTox() {
		return triPoundsWToxTox;
	}

	public Integer getTriPoundsWToxToxBin() {
		return triPoundsWToxToxBin;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public void setFacilityCount(Integer facilityCount) {
		this.facilityCount = facilityCount;
	}

	public void setFullModel(Double fullModel) {
		this.fullModel = fullModel;
	}

	public void setFullModelAdults18to44(Double fullModelAdults18to44) {
		this.fullModelAdults18to44 = fullModelAdults18to44;
	}

	public void setFullModelAdults18to44Bin(Integer fullModelAdults18to44Bin) {
		this.fullModelAdults18to44Bin = fullModelAdults18to44Bin;
	}

	public void setFullModelAdults45To64(Double fullModelAdults45To64) {
		this.fullModelAdults45To64 = fullModelAdults45To64;
	}

	public void setFullModelAdults45To64Bin(Integer fullModelAdults45To64Bin) {
		this.fullModelAdults45To64Bin = fullModelAdults45To64Bin;
	}

	public void setFullModelAdultsOver65(Double fullModelAdultsOver65) {
		this.fullModelAdultsOver65 = fullModelAdultsOver65;
	}

	public void setFullModelAdultsOver65Bin(Integer fullModelAdultsOver65Bin) {
		this.fullModelAdultsOver65Bin = fullModelAdultsOver65Bin;
	}

	public void setFullModelBin(Integer fullModelBin) {
		this.fullModelBin = fullModelBin;
	}

	public void setFullModelChildren10to17(Double fullModelChildren10to17) {
		this.fullModelChildren10to17 = fullModelChildren10to17;
	}

	public void setFullModelChildren10to17Bin(Integer fullModelChildren10to17Bin) {
		this.fullModelChildren10to17Bin = fullModelChildren10to17Bin;
	}

	public void setFullModelChildrenUnder10(Double fullModelChildrenUnder10) {
		this.fullModelChildrenUnder10 = fullModelChildrenUnder10;
	}

	public void setFullModelChildrenUnder10Bin(Integer fullModelChildrenUnder10Bin) {
		this.fullModelChildrenUnder10Bin = fullModelChildrenUnder10Bin;
	}

	public void setFullModelFemales18to44(Double fullModelFemales18to44) {
		this.fullModelFemales18to44 = fullModelFemales18to44;
	}

	public void setFullModelFemales18to44Bin(Integer fullModelFemales18to44Bin) {
		this.fullModelFemales18to44Bin = fullModelFemales18to44Bin;
	}

	public void setFullModelMales18to44(Double fullModelMales18to44) {
		this.fullModelMales18to44 = fullModelMales18to44;
	}

	public void setFullModelMales18to44Bin(Integer fullModelMales18to44Bin) {
		this.fullModelMales18to44Bin = fullModelMales18to44Bin;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReleaseCount(Integer releaseCount) {
		this.releaseCount = releaseCount;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setTriPounds(Double triPounds) {
		this.triPounds = triPounds;
	}

	public void setTriPoundsBin(Integer triPoundsBin) {
		this.triPoundsBin = triPoundsBin;
	}
	
	public void setTriPoundsWTox(Double triPoundsWTox) {
		this.triPoundsWTox = triPoundsWTox;
	}

	public void setTriPoundsWToxBin(Integer triPoundsWToxBin) {
		this.triPoundsWToxBin = triPoundsWToxBin;
	}

	public void setTriPoundsWToxTox(Double triPoundsWToxTox) {
		this.triPoundsWToxTox = triPoundsWToxTox;
	}
	
	public void setTriPoundsWToxToxBin(Integer triPoundsWToxToxBin) {
		this.triPoundsWToxToxBin = triPoundsWToxToxBin;
	}
	
	public String toString(){
		return getCountyName() +"," +getShortName();
	}
}
