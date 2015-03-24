package com.yale.qcxxandroid.bean;

import android.text.Spanned;

public class Comment {
    private String name;  
	private String pubTime;  
	private Spanned content;
    private String userPic;
    public String getPubTime() {
 		return pubTime;
 	}

 	public void setPubTime(String pubTime) {
 		this.pubTime = pubTime;
 	}

 	public Spanned getContent() {
 		return content;
 	}

 	public void setContent(Spanned content) {
 		this.content = content;
 	}
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  

  	public String getUserPic() {
  		return userPic;
  	}

  	public void setUserPic(String userPic) {
  		this.userPic = userPic;
  	}

  
}
