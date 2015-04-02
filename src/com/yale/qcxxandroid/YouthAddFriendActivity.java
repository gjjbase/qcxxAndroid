package com.yale.qcxxandroid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.bean.PicUpload;
import com.yale.qcxxandroid.util.DataHelper;

public class YouthAddFriendActivity extends BaseActivity {
	private ListView searchList;
	private EditText search;
	private TextView cancel;
	private List<String> list;
	private List<String> mylist;
	private List<String> otlist;

	// 申请中，处理中，拒绝，被拒绝，通过
	// 我申请对方加我好友，我处理对方加我好友的请求
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_addfriend_activity);
		search = (EditText) findViewById(R.id.search);
		searchList = (ListView) findViewById(R.id.searchList);
		list = new ArrayList<String>();
		list.add("yale");
		list.add("admin");
		list.add("陈璐");
		list.add("严涛");
		list.add("阿伟");
		list.add("刘欣");
		mylist = new ArrayList<String>();
		mylist.add("申请中");
		mylist.add("处理中");
		mylist.add("被拒绝");
		mylist.add("拒绝");
		final BaseAdapter base = new BaseAdapter() {

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				// TODO Auto-generated method stub
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.newfditem, null);
				final TextView txt = (TextView) convertView
						.findViewById(R.id.txt);
				final TextView name = (TextView) convertView
						.findViewById(R.id.name);
				final TextView msg = (TextView) convertView
						.findViewById(R.id.msg);
				msg.setText("您已成功向该同学发出申请");
				name.setText(list.get(position));
				if (position == 5) {
					msg.setText("您已被该同学拒绝");
					txt.setText("被拒绝");
					txt.setTextColor(R.color.gray);
					txt.setEnabled(false);
				}
				if (position == 4) {
					msg.setText("该同学正在处理您的申请信息");
					txt.setText("处理中");
					txt.setTextColor(R.color.gray);
					txt.setEnabled(false);
				}
				if (position == 3) {
					msg.setText("该同学已经通过你的好友申请");
					txt.setText("通过");
					txt.setTextColor(R.color.gray);
					txt.setEnabled(false);
				}
				if (position == 2) {
					msg.setText("您已拒绝该同学的申请");
					txt.setText("不通过");
					txt.setEnabled(false);
				}
				txt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(YouthAddFriendActivity.this)
								.setTitle("好友操作")
								.setMessage("是否同意")
								.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												txt.setText("通过");
												msg.setText("该同学已成为您的好友");
												list.remove(position);
												otlist.add(list.get(position));

												notifyDataSetChanged();
											}
										})
								.setNegativeButton("不同意",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												txt.setText("不通过");
												txt.setEnabled(false);
												txt.setTextColor(getResources()
														.getColor(R.color.gray));
												msg.setText("您拒绝该同学的申请");
											}
										})
								.setNeutralButton("取消",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												dialog.dismiss();
												msg.setText("您已成功向该同学发出申请");
											}
										}).create().show();
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		searchList.setAdapter(base);
	}

	public void backer(View v) {
		DataHelper dataHelper = DataHelper.getInstance(this);
		// sqliteDemo操作
		Dao<PicUpload, Integer> picUploadDAO = dataHelper.gePicUploadDataDAO();

		for (int i = 0; i < otlist.size(); i++) {
			PicUpload demo = new PicUpload();
			demo.setId(i);
			demo.setPicToken("pic" + i);
			demo.setNewFrd(otlist.get(i));
			try {
				picUploadDAO.create(demo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finish();
	}
}
