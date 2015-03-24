package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class YouthXmFragment extends Fragment{
	private ListView listView;
	private YouthXmAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View friendView = inflater.inflate(R.layout.youth_xxhd_xm_fragment,container,false);
		return friendView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		listView = (ListView)getActivity().findViewById(R.id.xmList);
		JSONArray giftArr = new JSONArray();
		JSONObject tmp1 = new JSONObject();
		try {
			tmp1.put("name", "周倩文");
			tmp1.put("order", "第一名");
			tmp1.put("content", "武汉大学舞蹈系，特长：跳舞、唱歌");
			tmp1.put("zan", "赞（5）");
			tmp1.put("vote", "投票（10）");
			tmp1.put("zhiTiao", "递纸条");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		giftArr.put(tmp1);
		adapter = new YouthXmAdapter(this.getActivity().getApplicationContext(), giftArr);
		listView.setAdapter(adapter);
	}
}
