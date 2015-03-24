package com.yale.qcxxandroid.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
/****
 * 默认textview中的超链接都带下划线,此类是去掉下划线
 * @author yale
 *
 */
public class TextViewHtmlClick extends ClickableSpan {
    private Intent mIntent;  
    
    public TextViewHtmlClick(Intent toActivity) {  
        mIntent = toActivity;  
    }  
      
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#00BFFF"));
        ds.setUnderlineText(false); 
    }
    
    @Override  
    public void onClick(View sourceView) {  
        Context context = sourceView.getContext();  
        context.startActivity( mIntent );  
    }  
      
    
    public int getSpanTypeId() {  
        return 100;  
    }  
      
     
    public int describeContents() {  
        return 0;  
    }  
    
    public Intent getIntent() {  
        return mIntent;  
    }  
}
