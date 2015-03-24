package com.yale.qcxxandroid.util;

/**
 * android操作webservice请求,以及返回结果的操作
 */
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceUtil {

	public static SoapObject requestWebService(String methodStr,
			String jsonParamStr) {
		String SOPA_ACTION = Globals.WSDL_URL + Globals.WSDL_URL_SERVICES;
		SoapObject soapObject = new SoapObject(Globals.NAME_SPACE,
				Globals.WSDL_URL_SERVICES_METHOD);
		soapObject.addProperty("methodStr", methodStr);
		soapObject.addProperty("jsonParamStr", jsonParamStr);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOPA_ACTION);
		SoapObject sb = null;
		try {
			androidHttpTransport.call(null, envelope);
			if (envelope.getResponse() != null) {
				sb = (SoapObject) envelope.bodyIn;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}

}
