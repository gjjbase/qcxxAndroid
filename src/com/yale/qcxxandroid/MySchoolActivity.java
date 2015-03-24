package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.bean.School;
public class MySchoolActivity extends BaseActivity {
	private ListView litView;  
	private List<School> schools = new ArrayList<School>(); 
	private MySchoolListAdapter adapter;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myschool_activity);
		litView = (ListView) findViewById(R.id.listView);  
        init();
        adapter = new MySchoolListAdapter(this, schools);	
        litView.setAdapter(adapter);
	}
	
    public void init(){
    	schools.clear();
		School c1 = new School();  
		c1.setClasses("2班");
		c1.setCollage("计算机学院");
		c1.setInSchool("2002年入学");
		c1.setProf("计算机科学");
		c1.setSchool("华中科技大学");
		schools.add(c1);  
		School c2 = new School();  
		c2.setClasses("4班");
		c2.setCollage("计算机学院");
		c2.setInSchool("2006年入学");
		c2.setProf("计算机科学");
		c2.setSchool("武汉科技大学");
		schools.add(c2);  
	}
    
    public void back(View view){
    	this.finish();
    }
	
}
