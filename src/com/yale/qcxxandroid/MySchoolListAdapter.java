package com.yale.qcxxandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yale.qcxxandroid.bean.School;

public class MySchoolListAdapter extends BaseAdapter {  
    private Context context;  
    private List<School> list;  
    private ViewHolder viewHolder;  
  
    public MySchoolListAdapter(Context context, List<School> list ) {  
        this.context = context;  
        this.list = list;  
    }  
  
    @Override  
    public int getCount() {  
        return list.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return list.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    		viewHolder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.my_school_list_adapter,  
                    null);
            viewHolder.school = (TextView)convertView.findViewById(R.id.school);
            viewHolder.school.setText(list.get(position).getSchool());
            viewHolder.collage = (TextView)convertView.findViewById(R.id.collage);
            viewHolder.collage.setText(list.get(position).getCollage());
            viewHolder.prof = (TextView)convertView.findViewById(R.id.prof);
            viewHolder.prof.setText(list.get(position).getProf());
            viewHolder.inSchool = (TextView)convertView.findViewById(R.id.inSchool);
            viewHolder.inSchool.setText(list.get(position).getInSchool()+list.get(position).getClasses());
        return convertView;  
    }  
  
    private class ViewHolder {  
    	private TextView school; 
        private TextView collage;
        private TextView prof;
    	private TextView inSchool; 
    }  
  
}  