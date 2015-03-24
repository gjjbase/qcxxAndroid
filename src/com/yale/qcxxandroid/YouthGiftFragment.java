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

public class YouthGiftFragment extends Fragment{
	private ListView listView;
	private YouthGiftAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View friendView = inflater.inflate(R.layout.youth_xxhd_gift_fragment, container,false);
		return friendView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		listView = (ListView)getActivity().findViewById(R.id.giftList);
		JSONArray giftArr = new JSONArray();
		JSONObject tmp1 = new JSONObject();
		try {
			tmp1.put("name", "红米Note");
			tmp1.put("count", "剩余数量：1000");
			tmp1.put("content", "积分达到1000可免费兑换，积分达到500，半价购买");
			tmp1.put("price", "998元");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		giftArr.put(tmp1);
		adapter = new YouthGiftAdapter(this.getActivity().getApplicationContext(), giftArr);
		listView.setAdapter(adapter);
	}
}
