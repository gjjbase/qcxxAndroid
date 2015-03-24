package com.yale.qcxxandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yale.qcxxandroid.bean.Comment;

public class CommentListAdapter extends BaseAdapter {  
    private Context context;  
    private List<Comment> list;  
    private ViewHolder viewHolder;  
  
    public CommentListAdapter(Context context, List<Comment> list ) {  
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
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_adapter,  
                    null);
            viewHolder.nc = (TextView)convertView.findViewById(R.id.nc);
            viewHolder.pubtime = (TextView)convertView.findViewById(R.id.showtime);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
        	viewHolder.nc.setText(list.get(position).getName());
        	viewHolder.content.setText(list.get(position).getContent());  
        	viewHolder.pubtime.setText(list.get(position).getPubTime());
        return convertView;  
    }  
  
    private class ViewHolder {  
    	private TextView nc; 
        private TextView pubtime;
        private TextView content;
    }  
  
}  