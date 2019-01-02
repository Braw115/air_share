package com.air.pojo.vo;

import java.util.List;

public class BatchOperVO {
	private List<Integer> list;	// 集合
	private String operation;	// 操作
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
