package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class SchoolDetaActivity extends BaseActivity {
	private ThreadUtil thread;
	private ListView list;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_datactivity);
		list = (ListView) findViewById(R.id.list);
		ini();
	}

	private void ini() {
		thread = new ThreadUtil(myhandler);
		String methodStr = "[{'" + Globals.COMM_SESSION
				+ ".CommonDataSessionBean':'listCommonData'}]";
		String jsonParamStr = "[{'cdType':" + 7 + ",'cdParendId':"
				+ sp.getString("cdParendId", "") + "}]";
		thread.doStartWebServicerequestWebService(SchoolDetaActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	Handler myhandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					final JSONArray joA = new JSONArray(returnJson);
					JSONObject jsoo = joA.getJSONObject(0);
					BaseAdapter adapter = new BaseAdapter() {

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {
							// TODO Auto-generated method stub
							convertView = LayoutInflater.from(
									getApplicationContext()).inflate(
									R.layout.dataitem, null);
							TextView txt = (TextView) convertView
									.findViewById(R.id.txt);
							try {
								txt.setText(joA.getJSONObject(position)
										.getString("cdMc"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
							return joA.length();
						}
					};
					list.setAdapter(adapter);
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							try {

								editor.putString(
										"cdParentId",
										joA.getJSONObject(arg2).getString(
												"cdParentId"));
								editor.putString(
										"txtnum",
										joA.getJSONObject(arg2).getString(
												"cdMc"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							editor.commit();
							finish();
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:

				break;

			}

		}
	};

}
