package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;

@SuppressLint("HandlerLeak")
public class FrdMesgActivity extends BaseActivity {
	public TextView topname, cancel;
	public ListView searchList;
	public LinearLayout hint;
	private List<HashMap<String, Object>> limap = new ArrayList<HashMap<String, Object>>();
	protected AsyncQueryHandler asyncQuery;
	protected static final String NAME = "name", NUMBER = "number",
			SORT_KEY = "sort_key";
	protected List<ContentValues> phonelist;

	protected class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);

		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				phonelist = new ArrayList<ContentValues>();
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
					phonelist.add(cv);
					Log.i("num", number);
					if (phonelist.size() != 0) {
						BaseAdapter base = new BaseAdapter() {

							@Override
							public View getView(int position, View convertView,
									ViewGroup parent) {
								// TODO Auto-generated method stub
								convertView = LayoutInflater.from(
										FrdMesgActivity.this).inflate(
										R.layout.newfditem, null);
								TextView name = (TextView) convertView
										.findViewById(R.id.name);
								TextView msg = (TextView) convertView
										.findViewById(R.id.msg);
								name.setText(phonelist.get(position)
										.getAsString(NAME));
								msg.setText(phonelist.get(position)
										.getAsString(NUMBER));

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
						searchList.setAdapter(base);
					} else {
						toast("失败", getApplicationContext());
					}
				}
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewfrd);
		topname = (TextView) findViewById(R.id.topname);
		hint = (LinearLayout) findViewById(R.id.hint);
		hint.setVisibility(View.GONE);
		topname.setText("通讯录好友");
		cancel = (TextView) findViewById(R.id.cancel);
		searchList = (ListView) findViewById(R.id.list);
		topname.setText("加好友");
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		// HashMap<String, Object> has1 = new HashMap<String, Object>();
		// has1.put("msg", "已添加");
		// has1.put("msg", "邀请");
		// has1.put("msg", "加好友");
		// limap.add(has1);
		// HashMap<String, Object> has2 = new HashMap<String, Object>();
		// has1.put("typ", "您已成功添加该同学为好友");
		// has1.put("typ", "您可以邀请该好友加入");
		// has1.put("typ", "您可以添加该同学为好友");
		// limap.add(has2);

	}

	public void back(View v) {
		finish();
	}
}
