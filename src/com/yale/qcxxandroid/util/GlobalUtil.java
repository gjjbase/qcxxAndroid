package com.yale.qcxxandroid.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

/**
 * 全局方法工具类
 * 
 */
@SuppressLint("DefaultLocale")
public class GlobalUtil {
	public static int cacheSize = 50 * 1024 * 1024;
	protected static int count = 0;
	public static Context context;
	public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(
			cacheSize) {
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return value.getRowBytes() * value.getHeight();
		}
	};

	public static void toast(String msg, Context context) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	private static String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// 三元运算符 t>10时取 ""+t
	}

	public static String time() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR)
				+ formatTime(c.get(Calendar.MONTH) + 1)
				+ formatTime(c.get(Calendar.DAY_OF_MONTH))
				+ formatTime(c.get(Calendar.HOUR_OF_DAY))
				+ formatTime(c.get(Calendar.MINUTE))
				+ formatTime(c.get(Calendar.SECOND));
		System.out.println(time); // 输出
		return time;

	}

	public static String TIME() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR)
				+ formatTime(c.get(Calendar.MONTH) + 1)
				+ formatTime(c.get(Calendar.DAY_OF_MONTH))
				+ formatTime(c.get(Calendar.HOUR_OF_DAY))
				+ formatTime(c.get(Calendar.MINUTE))
				+ formatTime(c.get(Calendar.SECOND))
				+ formatTime(c.get(Calendar.MILLISECOND));
		System.out.println(time); // 输出
		return time;

	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			Bitmap bitmap = BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
			fis.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像并加入缓存
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmapFromNet(String imgUrl) {
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			// 取得图片
			InputStream temp = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(temp);
			// bitmap =
			// Bitmap.createScaledBitmap(BitmapFactory.decodeStream(temp), 200,
			// 80, false);
			// 关闭流
			temp.close();
			addBitmapToMemoryCache(imgUrl, bitmap);
		} catch (IOException e) {
			bitmap = null;
		}
		return bitmap;
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) != null) {
			mMemoryCache.remove(key);
		}
		mMemoryCache.put(key, bitmap);
	}

	public static synchronized String getUUID(String uuid) {
		count++;
		long time = System.currentTimeMillis();

		uuid = "G" + Long.toHexString(time) + Integer.toHexString(count)
				+ Long.toHexString(Double.doubleToLongBits(Math.random()));

		uuid = uuid.substring(0, 24).toUpperCase();

		return uuid;
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		Bitmap bitmap = mMemoryCache.get(key);
		Log.i("********************", key);
		if (bitmap != null) {
			return bitmap;
		}
		return null;
	}

	/**
	 * Bitmap转换成byte[ ]:该方法的方法参数为一个Bitmap,最后返回的就是字节数组
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBytesFromBitmap(Bitmap bitmap) {
		// 实例化字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);// 压缩位图
		return baos.toByteArray();// 创建分配字节数组
	}

	/**
	 * byte[ ]转换回来Bitmap:该方法的方法参数为一个字节数组，返回的就是Bitmap
	 * 
	 * @param data
	 * @return
	 */
	public static Bitmap getBitmapFromBytes(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);// 从字节数组解码位图
	}

	/**
	 * 获取圆角位图的方法
	 * 
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap getBitmapSmall(Bitmap bitmap, float x, float y) {
		Matrix matrix = new Matrix();
		matrix.postScale(x, y); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getLocalDate() {
		String localDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		localDate = format.format(new Date());
		return localDate;
	}
}
