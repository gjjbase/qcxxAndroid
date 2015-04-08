package com.yale.qcxxandroid.util;
/**
 * android线程对webservice返回结果进行封装类(异步访问)
 */
import org.ksoap2.serialization.SoapObject;

import com.yale.qcxxandroid.base.FlippingLoadingDialog;

import android.app.Activity;
import android.os.Bundle; 
import android.os.Handler;
import android.os.Message;
public class ThreadUtil extends Thread {

	private Handler handler;
	@SuppressWarnings("unused")
	private boolean view;
	@SuppressWarnings("unused")
	private Activity activity;
	private String jsonParamStr;
	private String methodStr;
	private FlippingLoadingDialog progressDialog = null;
	public ThreadUtil(Handler handler) {
		this.handler = handler;
	}
	// 线程开始，调用webservice
	public void doStartWebServicerequestWebService(Activity activity,String jsonParamStr,String methodStr,boolean view) {
		this.activity = activity;
		this.view = view;
		this.jsonParamStr = jsonParamStr;
		this.methodStr = methodStr;
		// 开始请求
		if(view){//是否显示请求窗口
			progressDialog = new FlippingLoadingDialog(activity,"");
			progressDialog.show();
		}
		this.start();
	}
	@Override
	public void run() {
		super.run();
		Message message = handler.obtainMessage();// 构造消息
		Bundle b = new Bundle();
		try {
			SoapObject soapObject = WebServiceUtil.requestWebService(methodStr,jsonParamStr);
			message.what = 1;// 消息的类型
			if (soapObject != null && !soapObject.equals("")) {// 代表返回结果有数据
					try {
						b.putString("returnJson", soapObject.getProperty("string").toString());
					} catch (Exception e2) {
					}
			}
		} catch (Exception e) {// 异常
			message.what = 2;
			b.putString("error", e.getMessage());
			interrupt();
		}
		message.setData(b);
		handler.sendMessage(message);
		progressDialog.dismiss();
	}
}
