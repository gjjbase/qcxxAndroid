package com.yale.qcxxandroid.util;

/**
 * 全局常量类
 */
public final class Globals {

	/** 是否自动登录 */
	public static final String ISLOGINAUTO = "is_login_auto";
	/** WSDL的路径 **/
	// public static final String WSDL_URL =
	// "http://202.103.1.86/zdfwebservice/";
	// public static final String WSDL_URL =
	// "http://202.103.1.14:8080/qcxxweservice/";

	public static final String WSDL_URL = "http://202.103.1.27/qcxxweservice/";
	public static final String PHT_URL = "http://202.103.1.26";// 27
	// public static final String WSDL_URL =
	// "http://121.41.111.107:8080/qcxxweservice/";
	// public static final String PHT_URL = "http://121.41.111.107:8080";
	// public static final String WSDL_URL =
	// "http://202.103.1.2:8080/qcxxweservice/";
	// public static final String WSDL_URL =
	// "http://121.41.111.107:8080/qcxxweservice/";
	/** 用于控制台日志 **/
	public static final String TAG = "System.out";
	/** 通用，所有webservice的调用都要经过这个 **/
	public static final String NAME_SPACE = "http://sessionbean.qcxx.yale.com/";
	/** 通用，所有webservice的调用都要经过这个 **/
	public static final String WSDL_URL_SERVICES = "QcxxImpl?wsdl";
	/** 通用，所有webservice的调用都要经过这个 **/
	public static final String WSDL_URL_SERVICES_METHOD = "method";
	public static final String MAINID = "2";
	// 接口前缀
	public static final String MEMBER_SESSIOM = "com.yale.qcxx.sessionbean.member.impl";
	public static final String SHOW_SESSION = "com.yale.qcxx.sessionbean.show.impl";
	// 秀秀的排序标示
	public static final int XX_ORDER_BY_NEW = 0;// 最新
	public static final int XX_ORDER_BY_SCHOOL = 1;// 本校
	public static final int XX_ORDER_BY_CITY = 2;// 同城
	public static final int XX_ORDER_BY_COUNTRY = 3;// 全国
	public static final int XX_ORDER_BY_CLASSES = 4;// 班级
	// 公共事件
	public static final int COMM_DL = 0;// 登录
	public static final int COMM_GZ = 1;// 关注
	public static final int COMM_CAN_GZ = -1;// 取消关注
	public static final int COMM_DZ = 2;// 点赞
	public static final int COMM_DZT = 3;// 递纸条
	public static final int COMM_FW = 4;// 访问空间
	public static final int COMM_AC = 5;// 参加活动
	public static final int COMM_CAN_AC = -5;// 取消活动

	// show的类型（大类）
	public static final int SHOW_PIC = 0;// 图片show
	public static final int SHOW_VEDIO = 1;// 视频show
	public static final int SHOW_AC = 2;// 活动show
	public static final int SHOW_SX = 3;// 实习show
	// 好友状态
	public static final int FRIEND_IN_SYS = -3;// 系统中已经注册
	public static final int FRIEND_INIT = -2;// 初始化
	public static final int FRIEND_WAIT = -1;// 等待添加
	public static final int FRIEND_REQUEST = 0;// 请求中
	public static final int FRIEND_HAS = 1;// 已经添加
	public static final int FRIEND_REF = 2;// 已经拒绝
	public static final int FRIEND_BLACK = 3;// 已拉黑
	public static final int FRIEND_DELETE = 4;// 已删除
	// 返回值F
	public static final String RETURN_STR_TRUE = "[{\"returnStr\":\"true\"}]";
	public static final String RETURN_STR_FALSE = "[{\"returnStr\":\"false\"}]";
	public static final String RETURN_STR_YES = "[{\"returnStr\":\"yes\"}]";

	public static double getDistance(double lat1, double lon1, double lat2,
			double lon2) {

		int MAXITERS = 20;
		lat1 *= Math.PI / 180.0;
		lat2 *= Math.PI / 180.0;
		lon1 *= Math.PI / 180.0;
		lon2 *= Math.PI / 180.0;

		double a = 6378137.0; // WGS84 major axis
		double b = 6356752.3142; // WGS84 semi-major axis
		double f = (a - b) / a;
		double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

		double L = lon2 - lon1;
		double A = 0.0;
		double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
		double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

		double cosU1 = Math.cos(U1);
		double cosU2 = Math.cos(U2);
		double sinU1 = Math.sin(U1);
		double sinU2 = Math.sin(U2);
		double cosU1cosU2 = cosU1 * cosU2;
		double sinU1sinU2 = sinU1 * sinU2;

		double sigma = 0.0;
		double deltaSigma = 0.0;
		double cosSqAlpha = 0.0;
		double cos2SM = 0.0;
		double cosSigma = 0.0;
		double sinSigma = 0.0;
		double cosLambda = 0.0;
		double sinLambda = 0.0;

		double lambda = L; // initial guess
		for (int iter = 0; iter < MAXITERS; iter++) {
			double lambdaOrig = lambda;
			cosLambda = Math.cos(lambda);
			sinLambda = Math.sin(lambda);
			double t1 = cosU2 * sinLambda;
			double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
			double sinSqSigma = t1 * t1 + t2 * t2; // (14)
			sinSigma = Math.sqrt(sinSqSigma);
			cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
			sigma = Math.atan2(sinSigma, cosSigma); // (16)
			double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda
					/ sinSigma; // (17)
			cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
			cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2
					/ cosSqAlpha; // (18)

			double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
			A = 1
					+ (uSquared / 16384.0)
					* // (3)
					(4096.0 + uSquared
							* (-768 + uSquared * (320.0 - 175.0 * uSquared)));
			double B = (uSquared / 1024.0) * // (4)
					(256.0 + uSquared
							* (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
			double C = (f / 16.0) * cosSqAlpha
					* (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
			double cos2SMSq = cos2SM * cos2SM;
			deltaSigma = B
					* sinSigma
					* // (6)
					(cos2SM + (B / 4.0)
							* (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0)
									* cos2SM
									* (-3.0 + 4.0 * sinSigma * sinSigma)
									* (-3.0 + 4.0 * cos2SMSq)));

			lambda = L
					+ (1.0 - C)
					* f
					* sinAlpha
					* (sigma + C
							* sinSigma
							* (cos2SM + C * cosSigma
									* (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

			double delta = (lambda - lambdaOrig) / lambda;
			if (Math.abs(delta) < 1.0e-12) {
				break;
			}
		}

		return b * A * (sigma - deltaSigma);
	}

}
