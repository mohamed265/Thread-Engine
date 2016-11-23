/**
 * 
 */
package com.asset.engine.entity;

/**
 * @author mohamed.morsy
 *
 */
public class HRequest {

	private int id;

	private int num;

	private int result;

	private int sourceId;

	private int requestId;

	public HRequest() {
	}

	public HRequest(int id, int num, int result, int sourceId) {
		this.id = id;
		this.num = num;
		this.result = result;
		this.sourceId = sourceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

}
