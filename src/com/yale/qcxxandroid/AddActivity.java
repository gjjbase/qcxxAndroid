package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
	private AsyncQueryHandler asyncQuery;
	private static final String NAME = "name", NUMBER = "number",
			SORT_KEY = "sort_key";
	private List<ContentValues> lister;
	private RelativeLayout rel_add;

	@SuppressLint("HandlerLeak")
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);

		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				lister = new ArrayList<ContentValues>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					ContentValues cv = new ContentValues();
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					if (number.startsWith("+86")) {
						cv.put(NAME, name);
						cv.put(NUMBER, number.substring(3)); // 去掉+86
						cv.put(SORT_KEY, sortKey);
					} else {
						cv.put(NAME, name);
						cv.put(NUMBER, number);
						cv.put(SORT_KEY, sortKey);
					}
					lister.add(cv);
					Log.i("num", number);
				}
			}

		}

	}

	public void onback(View v) {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		list = (ListView) findViewById(R.id.list);
		rel_add = (RelativeLayout) findViewById(R.id.rel_add);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
		rel_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (lister.size() != 0) {
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
							txt_left.setText(lister.get(position).getAsString(
									NAME));
							txt_right.setText(lister.get(position).getAsString(
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
							return lister.size();
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