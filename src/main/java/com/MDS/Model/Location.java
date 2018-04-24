package com.MDS.Model;


public class Location {
String code;
String parentCode;
String name;
String type;

public Location(String code,String parentCode,String name,String type) {
	this.code = code;
	this.parentCode = parentCode;
	this.name = name;
	this.type = type;
	
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public String getParentCode() {
	return parentCode;
}

public void setParentCode(String parentCode) {
	this.parentCode = parentCode;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

@Override
public String toString() {
	return "{" + code + "::" + parentCode + "::" + name + "::" + type + "}";
	
}
}
