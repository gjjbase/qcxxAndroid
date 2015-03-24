package com.yale.qcxxandroid.util;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jredis.JRedis;
import org.jredis.RedisException;
import org.jredis.ri.alphazero.JRedisClient;
import org.jredis.ri.alphazero.support.DefaultCodec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.yale.qcxxandroid.base.FlippingLoadingDialog;
public class RedisThreadUtil extends Thread {
	private Handler handler;
	private double latitude;
	private double longitude;
	private SharedPreferences sp;
	private int type;
	private Activity activity;
	private FlippingLoadingDialog progressDialog = null;
	public RedisThreadUtil(Handler handler) {
		this.handler = handler;
	}
	public RedisThreadUtil() {
	}
	public void redisRequestService(double latitude,double longitude ,SharedPreferences sp,int type,Activity activity) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.sp = sp;
		this.type = type;
		this.activity = activity;
		if(type==1||type==3){
			progressDialog = new FlippingLoadingDialog(activity,"正在加载中,请稍后....");
			progressDialog.show();
		}
		this.start();
	}
	
	@Override
	public void run() {
		super.run();
		JRedis redis = new JRedisClient("202.103.1.66",6379);
		Bundle b = new Bundle();
		DecimalFormat df = new DecimalFormat("#.0");
		try {
			if(type==0){
				try {
					String key = df.format(latitude-longitude);
					List<String> keys = redis.keys(sp.getString("userId","")+",*");
					if(keys.size()>0){
						redis.del(keys.get(0));
					}
					String jsonPerson = "[{'name':'"+sp.getString("nickName","")+"','picHeadUrl':'','distance':'','address':'','latitude':'"+latitude+"','longitude':'"+longitude+"'}]";
					redis.set(sp.getString("userId","")+","+key,SerializeUtil.serialize(jsonPerson));
				} catch (RedisException e) {
					e.printStackTrace();
				}			
			}else if(type==1){
				 Message message = handler.obtainMessage();// 构造消息
				 message.what = 1;// 消息的类型
				 List<String> keys = null;
				 List<JSONObject> returnJsonPersons = new ArrayList<JSONObject>();
				 try {
					 	List<String> userKey = redis.keys(sp.getString("userId","")+",*");
						if(userKey.size()>0){
							JSONArray joA = new JSONArray(DefaultCodec.decode(redis.get(userKey.get(0))).toString());
							JSONObject jParam1 = joA.getJSONObject(0);
							double latitude = jParam1.getDouble("latitude");
							double longitude = jParam1.getDouble("longitude");
							keys = redis.keys("*,"+df.format(latitude-longitude));
							for(String key:keys){
								JSONArray jsonParam = new JSONArray(DefaultCodec.decode(redis.get(key)).toString());
								JSONObject jParam = jsonParam.getJSONObject(0);
								double distanceM;
								try {
									distanceM = Globals.getDistance(latitude, longitude, jParam.getDouble("latitude"), jParam.getDouble("longitude"));
									DecimalFormat df1 = new DecimalFormat("#.00");
									jParam.put("distance",df1.format(distanceM)+"米");
								} catch (JSONException e) {
									jParam.put("distance","数据异常");
								}
								returnJsonPersons.add(jParam);
							}
						}
				 } catch (Exception e) {
					message.what = 2;
					b.putString("error", e.getMessage());
				 }				
				if (returnJsonPersons != null && returnJsonPersons.size()>0) {// 代表返回结果有数据
					try {
						b.putSerializable("returnJsonPersons", (Serializable) returnJsonPersons);
					} catch (Exception e2) {
					}
				}
				message.setData(b);
				handler.sendMessage(message);
				progressDialog.dismiss();
			}else if(type==2){
				try {
				 	List<String> userKey = redis.keys(sp.getString("userId","")+",*");
					if(userKey.size()>0){
						JSONArray joA = new JSONArray(DefaultCodec.decode(redis.get(userKey.get(0))).toString());
						JSONObject jParam1 = joA.getJSONObject(0);
						double latitude = jParam1.getDouble("latitude");
						double longitude = jParam1.getDouble("longitude");
						String jsonPerson = "[{'name':'"+sp.getString("nickName","")+"','picHeadUrl':'','distance':'','latitude':'"+latitude+"','longitude':'"+longitude+"'}]";
						redis.set("yale,"+sp.getString("userId",""),SerializeUtil.serialize(jsonPerson));
						redis.expire("yale,"+sp.getString("userId",""),4);
					}
				} catch (RedisException e) {
					e.printStackTrace();
				}			
			}else if(type==3){
				 Message message = handler.obtainMessage();// 构造消息
				 message.what = 1;// 消息的类型
				 List<String> keys = null;
				 List<JSONObject> returnJsonPersons = new ArrayList<JSONObject>();
				 try {
					 	List<String> userKey = redis.keys("yale,"+sp.getString("userId",""));
						if(userKey.size()>0){
							JSONArray joA = new JSONArray(DefaultCodec.decode(redis.get(userKey.get(0))).toString());
							JSONObject jParam1 = joA.getJSONObject(0);
							double latitude = jParam1.getDouble("latitude");
							double longitude = jParam1.getDouble("longitude");
							keys = redis.keys("yale,*");
							for(String key:keys){
								if(!key.equals("yale,"+sp.getString("userId",""))){
									JSONArray jsonParam = new JSONArray(DefaultCodec.decode(redis.get(key)).toString());
									JSONObject jParam = jsonParam.getJSONObject(0);
									double distanceM;
									try {
										distanceM = Globals.getDistance(latitude, longitude, jParam.getDouble("latitude"), jParam.getDouble("longitude"));
										DecimalFormat df1 = new DecimalFormat("#.00");
										jParam.put("distance",df1.format(distanceM)+"米");
									} catch (JSONException e) {
										jParam.put("distance","数据异常");
									}
									returnJsonPersons.add(jParam);
								}
							}
						}
				 } catch (Exception e) {
					message.what = 2;
					b.putString("error", e.getMessage());
				 }				
				if (returnJsonPersons != null && returnJsonPersons.size()>0) {// 代表返回结果有数据
					try {
						b.putSerializable("returnJsonPersons", (Serializable) returnJsonPersons);
					} catch (Exception e2) {
					}
				}
				message.setData(b);
				handler.sendMessage(message);
				progressDialog.dismiss();
			}
		} catch (Exception e) {// 异常
			e.printStackTrace();
			interrupt();
		}
		redis.quit();
	}
}
