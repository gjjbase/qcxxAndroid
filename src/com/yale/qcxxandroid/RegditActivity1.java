package com.yale.qcxxandroid;


import java.util.Timer;

import org.apache.commons.lang.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MainActivity;
public class RegditActivity1 extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private TextView sexValue,loveValue;
	private EditText code, nc,passWord,valPassWord;
	private String cd, ncheng,pw,valpw,sex,love;
	private Button regdit;
	Timer timer = new Timer();  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regdit1_activity);
		regdit = (Button) this.findViewById(R.id.regdit);
		nc = (EditText) this.findViewById(R.id.nc);
		nc.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				commValid();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				commValid();
			}
			@Override
			public void afterTextChanged(Editable s) {
				commValid();
			}
		});	
	
		passWord = (EditText) this.findViewById(R.id.passWord);
		passWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				commValid();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				commValid();
			}
			@Override
			public void afterTextChanged(Editable s) {
				commValid();
			}
		});	
		
		valPassWord = (EditText) this.findViewById(R.id.valPassWord);
		valPassWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				commValid();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				commValid();
			}
			@Override
			public void afterTextChanged(Editable s) {
				commValid();
			}
		});	
		
		sexValue = (TextView) this.findViewById(R.id.sexValue);
		sexValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				commValid();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				commValid();
			}
			@Override
			public void afterTextChanged(Editable s) {
				commValid();
			}
		});			
	}
	
	public void regPreClick(View view){
		this.finish();
	}
	
	public void regditSub(View view){
		intent = new Intent();
		bundle=new Bundle();
    	intent.setClass(RegditActivity1.this,MainActivity.class);
    	startActivity(intent);
    	this.finish();
	}
	
	public void loveChose(View view){
		
	}	
	public void sexChose(View view){
		
	}		
	public void commValid(){
		cd = code.getText().toString();
		ncheng = nc.getText().toString();
		pw = passWord.getText().toString();
		valpw = valPassWord.getText().toString();
		sex = sexValue.getText().toString();
		love = loveValue.getText().toString();
    	if(!StringUtils.isEmpty(cd)&&!StringUtils.isEmpty(ncheng)&&!StringUtils.isEmpty(pw)&&!StringUtils.isEmpty(valpw)&&!StringUtils.isEmpty(sex)&&!StringUtils.isEmpty(love)){
    		regdit.setEnabled(true);
    		regdit.refreshDrawableState();
    	}else{
    		regdit.setEnabled(false);
    		regdit.refreshDrawableState();	        		
    	}		
	}
}
