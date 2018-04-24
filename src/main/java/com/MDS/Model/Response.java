package com.MDS.Model;

public class Response {
	String id;
	String ver;
	Integer ets;
	String params;
	String result;

	public Response(String id, String ver, Integer ets, String params, String result) {
		this.id = id;
		this.ver = ver;
		this.ets = ets;
		this.params = params;
		this.result = result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public Integer getEts() {
		return ets;
	}

	public void setEts(Integer ets) {
		this.ets = ets;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
