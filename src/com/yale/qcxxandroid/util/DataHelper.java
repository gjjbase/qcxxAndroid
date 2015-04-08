package com.yale.qcxxandroid.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yale.qcxxandroid.bean.MessageBean;
import com.yale.qcxxandroid.bean.PicUpload;
import com.yale.qcxxandroid.bean.XmppMsgBean;

/****
 * 数据库操作类
 * 
 * @ClassName: DataHelper
 * @Description: TODO
 * @author yale
 */
public class DataHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "myqcxxQlite.db";
	// private static final String PHOTO_NAME="photo.db";
	private static final int DATABASE_VERSION = 1;
	private static DataHelper dataHelper;
	private Dao<PicUpload, Integer> picUploadDAO = null;
	private Dao<MessageBean, Integer> messageDAO = null;
	private Dao<XmppMsgBean, Integer> xmppMsgDAO = null;

	private DataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DataHelper getInstance(Context context) {
		if (dataHelper == null) {
			dataHelper = new DataHelper(context);
		}
		return dataHelper;
	}

	@Override
	public void close() {
		super.close();
		picUploadDAO = null;
		messageDAO = null;
		xmppMsgDAO = null;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.e(DataHelper.class.getName(), "开始创建数据库");
			TableUtils
					.createTableIfNotExists(connectionSource, PicUpload.class);
			Log.e(DataHelper.class.getName(), "创建数据库成功");

			Log.e(DataHelper.class.getName(), "开始创建数据库");
			TableUtils.createTableIfNotExists(connectionSource,
					MessageBean.class);
			Log.e(DataHelper.class.getName(), "创建数据库成功");

			Log.e(DataHelper.class.getName(), "开始创建数据库");
			TableUtils.createTableIfNotExists(connectionSource,
					XmppMsgBean.class);
			Log.e(DataHelper.class.getName(), "创建数据库成功");
		} catch (Exception e) {
			Log.e(DataHelper.class.getName(), "创建数据库失败", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
	}

	public Dao<PicUpload, Integer> gePicUploadDataDAO() {
		if (picUploadDAO == null) {
			try {
				picUploadDAO = getDao(PicUpload.class);
			} catch (Exception e) {
				System.out.println("操作数据失败");
			}
		}
		return picUploadDAO;
	}

	public Dao<MessageBean, Integer> getMessageDAO() {
		if (messageDAO == null) {
			try {
				messageDAO = getDao(MessageBean.class);
			} catch (Exception e) {
				System.out.println("操作数据失败");
			}
		}
		return messageDAO;
	}

	public Dao<XmppMsgBean, Integer> getXmppMsgDAO() {
		if (xmppMsgDAO == null) {
			try {
				xmppMsgDAO = getDao(XmppMsgBean.class);
			} catch (Exception e) {
				System.out.println("操作数据失败");
			}
		}
		return xmppMsgDAO;
	}

}
