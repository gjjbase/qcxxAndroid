package com.yale.qcxxandroid;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {  
	private Context context;  
	private List<JSONObject> jsonList;  
	private ViewHolder viewHolder;
	private TextView okText;
	private int checkCount = 0;
	public SearchAdapter(Context context,List<JSONObject> jsonList) {  
		this.context = context;
		this.jsonList = jsonList;
	}
	public SearchAdapter(Context context,List<JSONObject> jsonList,TextView okText) {  
		this.context = context;
		this.jsonList = jsonList;
		this.okText = okText;
	} 
	@Override  
	public int getCount() {  
		return jsonList.size();  
	}  

	@Override  
	public Object getItem(int position) {  
		return  position;
	}  

	@Override  
	public long getItemId(int position) {  
		return position;  
	}  

	@Override  
	public View getView(final int position, View convertView, ViewGroup parent) {  
		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(R.layout.search_adapter,null);
		viewHolder.name = (TextView)convertView.findViewById(R.id.name);
		viewHolder.checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
		try {
			viewHolder.name.setText(jsonList.get(position).getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			if(jsonList.get(position).getString("checkbox").equals("true")){
				viewHolder.checkbox.setVisibility(View.VISIBLE);
				viewHolder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						if(arg1){
							checkCount++;
						}else{
							checkCount--;
						}
						if(checkCount==0){
							okText.setTextColor(context.getResources().getColor(R.color.comm_color));
							okText.setText("确定");
						}else{
							okText.setTextColor(context.getResources().getColor(R.color.green));
							okText.setText("确定("+checkCount+")");
						}
						
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			String credit = jsonList.get(position).getString("credit");
			if(credit != null){
				viewHolder.credit = (TextView)convertView.findViewById(R.id.credit);
				viewHolder.credit.setText(credit);
				viewHolder.credit.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return convertView;  
	}  

	private class ViewHolder {
		private CheckBox checkbox;
		private TextView name;
		private TextView credit;
	}  

}  