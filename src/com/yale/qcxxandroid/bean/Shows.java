package com.yale.qcxxandroid.bean;

import java.util.List;

import android.view.View;

public class Shows {
	public String getShowPic() {
		return showPic;
	}
	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getXz() {
		return xz;
	}
	public void setXz(String xz) {
		this.xz = xz;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	private String showPic;
	private String showType;
	private String xz;
	private String showTime;
	private String age;
	private String nickName;
	private String schoolName;
	private String sexName;
	private String content;
	private List<View> views;
	int[] viewResId;
	public int[] getViewResId() {
		return viewResId;
	}
	public void setViewResId(int[] viewResId) {
		this.viewResId = viewResId;
	}
	public List<View> getViews() {
		return views;
	}
	public void setViews(List<View> views) {
		this.views = views;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getViewStr() {
		return viewStr;
	}
	public void setViewStr(String viewStr) {
		this.viewStr = viewStr;
	}
	public String getTopStr() {
		return topStr;
	}
	public void setTopStr(String topStr) {
		this.topStr = topStr;
	}
	private String viewStr;
	private String topStr;
	private String plStr;
	public String getPlStr() {
		return plStr;
	}
	public void setPlStr(String plStr) {
		this.plStr = plStr;
	}
}
