package com.yale.qcxxandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class YouthBmFragment extends Fragment{
	private RelativeLayout title;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View friendView = inflater.inflate(R.layout.show_pub_activity,container,false);
		title = (RelativeLayout)friendView.findViewById(R.id.title);
		title.setVisibility(View.GONE);
		return friendView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
}
