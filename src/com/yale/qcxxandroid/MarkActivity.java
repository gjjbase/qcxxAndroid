package com.yale.qcxxandroid;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
@SuppressWarnings("unused")
public class MarkActivity extends BaseActivity {
	private ListView searchList;
	private MyBaseListView list;
	private Intent intent = new Intent();
	private EditText searcher;
	RelativeLayout shade;
	private TextView cancel, search;
	private LinearLayout bottom, lin_bger, toper_lin;

	// private void showSearch() {
	// searcher.setFocusable(true);
	// searcher.setFocusableInTouchMode(true);
	// searcher.requestFocus();
	// InputMethodManager inputManager = (InputMethodManager) searcher
	// .getContext().getSystemService(INPUT_METHOD_SERVICE);
	// inputManager.showSoftInput(searcher, 0);
	// searcher.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

	// ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(控件ID,
	// 0);
	// lin_bg.setBackgroundColor(R.color.bg_color);
	// search.setBackground(getResources().getDrawable(R.drawable.qcediters));
	/*
	 * TranslateAnimation translateAnimation = new
	 * TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
	 * Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
	 * Animation.RELATIVE_TO_SELF, -1.0f);
	 * translateAnimation.setFillAfter(true);
	 * translateAnimation.setDuration(500);
	 * title.startAnimation(translateAnimation);
	 */

	// shade.setAlpha(0.5f);
	// }

	private void hideSearch() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(search, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
		toper_lin.setVisibility(View.VISIBLE);
		lin_bger.setVisibility(View.GONE);
		// lin_bg.setBackgroundColor(R.color.white);
		//
		// search.setBackground(getResources().getDrawable(R.drawable.qcediters));

		// searchListObj.clear();
		// shade.setAlpha(0.5f);
	}

	// et.setFocusable(true);et.setFocusableInTouchMode(true);et.requestFocus();et.findFocus();
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark);
		list = (MyBaseListView) findViewById(R.id.list);
		searchList = (ListView) findViewById(R.id.searchList);
		shade = (RelativeLayout) findViewById(R.id.shade);
		lin_bger = (LinearLayout) findViewById(R.id.lin_bger);
		toper_lin = (LinearLayout) findViewById(R.id.toper_lin);
		search = (TextView) findViewById(R.id.search);
		searcher = (EditText) findViewById(R.id.searcher);
		cancel = (TextView) findViewById(R.id.cancel);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toper_lin.setVisibility(View.GONE);
				lin_bger.setVisibility(View.VISIBLE);
				searcher.setFocusable(true);
				searcher.setFocusableInTouchMode(true);
				searcher.requestFocus();
				searcher.findFocus();
				searcher.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(searcher, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);

			}
		});
		// search.setOnEditorActionListener(new
		// TextView.OnEditorActionListener() {
		// @SuppressLint("NewApi")
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		//
		// return true;
		// }
		// return false;
		// }
		// });
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideSearch();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				intent.setClass(getApplicationContext(), MarkdeayActivity.class);
				startActivity(intent);
			}
		});
		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.itemmark, null);
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
				return 3;
			}
		};
		list.setAdapter(adapter);
		list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				// editor.putBoolean("fasle", true);
				// editor.commit();
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {

						try {
							// if (sp.getBoolean("false", false) == true) {
							Thread.sleep(500);
							Toast.makeText(getApplicationContext(), "刷新完成",
									Toast.LENGTH_SHORT).show();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						list.onRefreshComplete();
					}
				}.execute();
			}
		});
	}

	public void backClick(View v) {
		finish();
	}
}
