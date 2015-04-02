package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import com.yale.qcxxandroid.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FabuActivity extends BaseActivity {
	private GridView grd;
	private List<Integer> list;
	private TextView txt;
	private EditText content;
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabu);
		grd = (GridView) findViewById(R.id.grd);
		txt = (TextView) findViewById(R.id.txt);
		content = (EditText) findViewById(R.id.content);
		txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (content.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "内容不能为空", 3000)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), "发布成功", 3000)
							.show();
					bundle.putInt("data", 1);
					intent.setClass(FabuActivity.this,
							MyClassActivityCont.class).putExtras(bundle);
					startActivity(intent);
				}

			}
		});
		list = new ArrayList<Integer>();
		list.add(R.drawable.xiuxiu3);
		list.add(R.drawable.xiuxiu4);
		list.add(R.drawable.xiuxiu5);
		list.add(R.drawable.xiuxiu5);

		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item, null);
				final ImageView imger = (ImageView) convertView
						.findViewById(R.id.imger);
				imger.setImageResource(list.get(position));
				return convertView;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		grd.setAdapter(adapter);
	}

	public void backClick(View v) {
		finish();
	}
}
