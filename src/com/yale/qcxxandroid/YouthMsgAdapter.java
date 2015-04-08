package com.yale.qcxxandroid;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.yale.qcxxandroid.base.OnDeleteListioner;
import com.yale.qcxxandroid.util.GlobalUtil;

public class YouthMsgAdapter extends BaseAdapter {
	private Context context;
	private List<JSONObject> jsonList;
	private ViewHolder viewHolder;
	private OnDeleteListioner mOnDeleteListioner;
	private boolean delete = false;

	public YouthMsgAdapter(Context context, List<JSONObject> jsonList) {
		this.context = context;
		this.jsonList = jsonList;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setOnDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
		this.mOnDeleteListioner = mOnDeleteListioner;
	}

	@Override
	public int getCount() {
		return jsonList.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder = new ViewHolder();
		Bitmap bitmap = null;
		try {
			bitmap = GlobalUtil.getBitmapFromMemCache(jsonList.get(position)
					.get("imgUrl").toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (bitmap != null) {

		} else {
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.logo);
			/*
			 * float screenwidth = arg0.getWidth(); float viewWidth =
			 * bitmap.getWidth(); float viewheight = bitmap.getHeight();
			 * LayoutParams params = new LayoutParams((int) screenwidth,
			 * (int)((viewheight/viewWidth)*screenwidth));
			 * imageView.setLayoutParams(params);
			 * imageView.setScaleType(ScaleType.CENTER_CROP);
			 * imageView.setImageBitmap(bitmap);
			 */
		}

		/*
		 * float screenwidth = arg0.getWidth(); float viewWidth =
		 * bitmap.getWidth(); float viewheight = bitmap.getHeight();
		 * LayoutParams params = new LayoutParams((int) screenwidth,
		 * (int)((viewheight/viewWidth)*screenwidth));
		 * imageView.setLayoutParams(params);
		 */

		convertView = LayoutInflater.from(context).inflate(
				R.layout.msg_adapter, null);
		viewHolder.nc = (TextView) convertView.findViewById(R.id.nc);
		viewHolder.pubtime = (TextView) convertView.findViewById(R.id.pubtime);
		viewHolder.firImg = (ImageView) convertView.findViewById(R.id.firImg);
		viewHolder.firImg.setScaleType(ScaleType.FIT_XY);
		viewHolder.firImg.setImageBitmap(bitmap);
		viewHolder.content = (TextView) convertView.findViewById(R.id.content);
		viewHolder.tongzhi = (TextView)convertView.findViewById(R.id.txt_tongzhi);
		viewHolder.deleteAction = (TextView) convertView
				.findViewById(R.id.delete_action);
		try {
			viewHolder.nc.setText(jsonList.get(position).getString("nc"));
			viewHolder.content.setText(jsonList.get(position).getString(
					"content"));
			viewHolder.pubtime.setText(jsonList.get(position).getString(
					"pubtime"));
			int tongzhi = Integer.parseInt(jsonList.get(position).getString("tongzhi"));
			if(tongzhi>0){
				viewHolder.tongzhi.setText(tongzhi+"");
				viewHolder.tongzhi.setVisibility(View.VISIBLE);
			}else{
				viewHolder.tongzhi.setVisibility(View.INVISIBLE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mOnDeleteListioner != null)
					mOnDeleteListioner.onDelete(position);
			}
		};
		viewHolder.deleteAction.setOnClickListener(mOnClickListener);
		return convertView;
	}

	private class ViewHolder {
		private TextView nc;
		private TextView pubtime;
		private TextView content;
		private TextView tongzhi;
		private TextView deleteAction;
		private ImageView firImg;
	}

}