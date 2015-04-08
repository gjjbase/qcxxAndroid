package com.yale.qcxxandroid.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "picUpload")
public class PicUpload {
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(columnName = "picToken", canBeNull = false)
	String picToken;
	@DatabaseField(columnName = "picUrl", canBeNull = false)
	String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicToken() {
		return picToken;
	}

	public void setPicToken(String picToken) {
		this.picToken = picToken;
	}

	public PicUpload() {// 必须要有一个无参数的构造函数
	}

}
