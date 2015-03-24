package com.yale.qcxxandroid.base;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
public class ScorePlazaPlugin extends CordovaPlugin {
	private static final String ACTION_SCORE_PLAZA_LIST = "scorePlazaList";
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		try {
			if (action.equals(ACTION_SCORE_PLAZA_LIST)) {
				final StringBuffer sb = new StringBuffer();
				sb.append("<li></li>"); 
				// 当前窗口,WebCore 线程中执行
				 callbackContext.success(sb.toString()); // Thread-safe.
				return true;
			}
			callbackContext.error("有错误");
			return false;
		} catch (Exception e) {
			System.err.println("异常：" + e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
	}
}
