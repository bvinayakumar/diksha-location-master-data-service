package com.MDS.Model;

public class School {

	private String schoolCode;
	private String name;
	private String latlong;
	private String educationLevel;
	private String schoolType;
	private String noOfTeachers;
	private String clusterCode;
	private boolean schoolWithSpecialNeed;
	private String schoolInchargeCode;
	private String schoolInchargeName;
	private String schoolInchargeMobile;
	private String address;
	private String externalId;
	private String idProvider;
	private String idProviderType;

	public School(String schoolCode, String name, String latlong, String educationLevel, String schoolType,
			String noOfTeachers, String clusterCode, boolean schoolWithSpecialNeed, String schoolInchargeCode,
			String schoolInchargeName, String schoolInchargeMobile, String address, String externalId,
			String idProvider, String idProviderType) {
		this.schoolCode = schoolCode;
		this.name = name;
		this.latlong = latlong;
		this.name = name;
		this.schoolCode = schoolCode;
		this.educationLevel = educationLevel;
		this.schoolType = schoolType;
		this.noOfTeachers = noOfTeachers;
		this.clusterCode = clusterCode;
		this.schoolWithSpecialNeed = schoolWithSpecialNeed;
		this.schoolInchargeCode = schoolInchargeCode;
		this.schoolInchargeName = schoolInchargeName;
		this.schoolInchargeMobile = schoolInchargeMobile;
		this.address = address;
		this.externalId = externalId;
		this.idProvider = idProvider;
		this.idProviderType = idProviderType;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatlong() {
		return latlong;
	}

	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public String getNoOfTeachers() {
		return noOfTeachers;
	}

	public void setNoOfTeachers(String noOfTeachers) {
		this.noOfTeachers = noOfTeachers;
	}

	public String getClusterCode() {
		return clusterCode;
	}

	public void setClusterCode(String clusterCode) {
		this.clusterCode = clusterCode;
	}

	public boolean isSchoolWithSpecialNeed() {
		return schoolWithSpecialNeed;
	}

	public void setSchoolWithSpecialNeed(boolean schoolWithSpecialNeed) {
		this.schoolWithSpecialNeed = schoolWithSpecialNeed;
	}

	public String getSchoolInchargeCode() {
		return schoolInchargeCode;
	}

	public void setSchoolInchargeCode(String schoolInchargeCode) {
		this.schoolInchargeCode = schoolInchargeCode;
	}

	public String getSchoolInchargeName() {
		return schoolInchargeName;
	}

	public void setSchoolInchargeName(String schoolInchargeName) {
		this.schoolInchargeName = schoolInchargeName;
	}

	public String getSchoolInchargeMobile() {
		return schoolInchargeMobile;
	}

	public void setSchoolInchargeMobile(String schoolInchargeMobile) {
		this.schoolInchargeMobile = schoolInchargeMobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(String idProvider) {
		this.idProvider = idProvider;
	}

	public String getIdProviderType() {
		return idProviderType;
	}

	public void setIdProviderType(String idProviderType) {
		this.idProviderType = idProviderType;
	}

}
