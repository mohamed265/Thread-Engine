package com.asset.engine.entity;

public class Request {

	private int id;

	private int num;

	private int sourceId;

	private int response;

	public Request() {

	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

	public void setId(int id) {
		this.id = id;

	}

	public int getId() {
		return id;

	}

}
