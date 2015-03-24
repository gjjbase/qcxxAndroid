package com.yale.qcxxandroid.bean;

import android.text.Spanned;

public class Mail {
	private String name;
	private String pinYinName;
	private String tag;
	private String sex;
	private String qm;
	private String pubTime;
	private int img;

	public int getimg() {
		return img;
	}

	public void setimg(int img) {
		this.img = img;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	private Spanned content;

	public Spanned getContent() {
		return content;
	}

	public void setContent(Spanned content) {
		this.content = content;
	}

	public String getQm() {
		return qm;
	}

	public void setQm(String qm) {
		this.qm = qm;
	}

	public Mail() {

	}

	public Mail(String name) {
		super();
		this.name = name;
	}

	public Mail(String name, String pinYinName) {
		super();
		this.name = name;
		this.pinYinName = pinYinName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinYinName() {
		return pinYinName;
	}

	public void setPinYinName(String pinYinName) {
		this.pinYinName = pinYinName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
