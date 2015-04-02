package com.yale.qcxxandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;

public class AddActivity extends BaseActivity {
	private ListView list;
	private RelativeLayout rel_add;
	public void onback(View v) {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		list = (ListView) findViewById(R.id.list);
		rel_add = (RelativeLayout) findViewById(R.id.rel_add);
		Pholis(true);
		rel_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phonelist.size() != 0) {
					BaseAdapter adapter = new BaseAdapter() {

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {
							// TODO Auto-generated method stub
							convertView = LayoutInflater.from(AddActivity.this)
									.inflate(R.layout.additem, null);
							TextView txt_right = (TextView) convertView
									.findViewById(R.id.txt_right);
							TextView txt_left = (TextView) convertView
									.findViewById(R.id.txt_left);
							txt_left.setText(phonelist.get(position).getAsString(
									NAME));
							txt_right.setText(phonelist.get(position).getAsString(
									NUMBER));
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
							return phonelist.size();
						}
					};
					list.setAdapter(adapter);
				} else {
					Toast.makeText(getApplicationContext(), "当前通讯录没有联系人", 3000)
							.show();
				}
			}
		});

		// rel_add.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}
}