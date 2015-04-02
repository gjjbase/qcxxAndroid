package com.yale.qcxxandroid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.DetaActivity.SortAdapter;
import com.yale.qcxxandroid.DetaActivity.SortModel;
import com.yale.qcxxandroid.SideBar.OnTouchingLetterChangedListener;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.bean.PicUpload;
import com.yale.qcxxandroid.util.StringHelper;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
public class AdrebookActivity extends BaseActivity {
	private ListView sortListView, bomlist;
	private ThreadUtil thread;

	private SharedPreferences share;
	private String searchStr = "";
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SideBar sideBar;
	private TextView dialog, search;
	private SortAdapter adapter;
	private List<SortModel> SourceDateList;
	private EditText searcher;
	private LinearLayout lin_br;
	private RelativeLayout toper_lin;
	private TextView cancel;
	private JSONArray jsoo;
	List<PicUpload> picList;
	Dao<PicUpload, Integer> picUploadDAO;

	public void init() {

		thread = new ThreadUtil(handler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'myFriendList'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "")
				+ ",'actionId':" + 100 + "}]";
		thread.doStartWebServicerequestWebService(AdrebookActivity.this,
				jsonParamStr, methodStr, true);

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					jsoo = new JSONArray(returnJson);
					// JSONObject parm = new JSONObject();
					// if (picList.isEmpty() == false) {
					// try {
					// picList = picUploadDAO.queryForAll();
					//
					// } catch (SQLException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// for (PicUpload pic : picList) {
					//
					// }
					// } else {
					// }
					SourceDateList = filledData(jsoo);
					// 根据a-z进行排序源数据
					Collections.sort(SourceDateList, pinyinComparator);
					adapter = new SortAdapter(AdrebookActivity.this,
							SourceDateList);
					sortListView.setAdapter(adapter);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:

				break;

			}
		}
	};

	public void onback(View v) {
		finish();
	}

	public void gebionclick(View v) {
		bundle.putInt("data", 1);
		intent.setClass(AdrebookActivity.this, MyClassActivityitem.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	public void newfrid(View v) {
		intent.setClass(AdrebookActivity.this, YouthAddFriendActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, 0);
	}

	public void myonclick(View v) {
		bundle.putInt("data", 0);
		intent.setClass(AdrebookActivity.this, MyClassActivityitem.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	public void txtadd(View v) {
		intent.setClass(getApplicationContext(), AddnewFrdACtivity.class);
		startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adrebook);
		search = (TextView) findViewById(R.id.search);
		cancel = (TextView) findViewById(R.id.cancel);
		searcher = (EditText) findViewById(R.id.searcher);
		lin_br = (LinearLayout) findViewById(R.id.lin_br);
		toper_lin = (RelativeLayout) findViewById(R.id.toper_lin);
		bomlist = (ListView) findViewById(R.id.bomlist);
		searcher.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				List<SortModel> Sou = new ArrayList<SortModel>();
				JSONObject js = new JSONObject();
				for (int i = 0; i < jsoo.length(); i++) {
					try {
						if (StringHelper.getPingYin(
								jsoo.getJSONObject(i).getString("cdMc"))
								.contains(searcher.getText().toString()) == true
								|| jsoo.getJSONObject(i)
										.getString("cdMc")
										.contains(searcher.getText().toString())
								|| jsoo.getJSONObject(i).getString("cdMc")
										.contains("yaleviewmicro")) {
							js.put("cdMc",
									jsoo.getJSONObject(i).getString("cdMc"));
							js.put("cdId",
									jsoo.getJSONObject(i).getString("cdId"));

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				try {
					JSONArray jso = new JSONArray().put(js);
					Sou = filledData(jso);
					Collections.sort(Sou, pinyinComparator);
					adapter = new SortAdapter(AdrebookActivity.this, Sou);
					bomlist.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searcher.getWindowToken(), 0);
				toper_lin.setVisibility(View.VISIBLE);
				lin_br.setVisibility(View.GONE);
			}
		});
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!arg0.isFocusable()) {
					search.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							toper_lin.setVisibility(View.GONE);
							lin_br.setVisibility(View.VISIBLE);
							searcher.setFocusable(true);
							searcher.setFocusableInTouchMode(true);
							searcher.requestFocus();
							searcher.findFocus();
							searcher.setGravity(Gravity.LEFT
									| Gravity.CENTER_VERTICAL);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.showSoftInput(searcher,
									InputMethodManager.RESULT_SHOWN);
							imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									InputMethodManager.HIDE_IMPLICIT_ONLY);

						}
					});
				}
			}
		});
		init();
		initViews();
	}

	public class SortModel {

		private String name; // 显示的数据
		private String sortLetters; // 显示数据拼音的首字母

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSortLetters() {
			return sortLetters;
		}

		public void setSortLetters(String sortLetters) {
			this.sortLetters = sortLetters;
		}
	}

	public class SortAdapter extends BaseAdapter implements SectionIndexer {
		private List<SortModel> list = null;
		private Context mContext;

		public SortAdapter(Context mContext, List<SortModel> list) {
			this.mContext = mContext;
			this.list = list;
		}

		/**
		 * 当ListView数据发生变化时,调用此方法来更新ListView
		 * 
		 * @param list
		 */
		public void updateListView(List<SortModel> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			final SortModel mContent = list.get(position);
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.itembookactivity, null);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
				viewHolder.tvLetter = (TextView) view
						.findViewById(R.id.catalog);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}

			viewHolder.tvTitle.setText(this.list.get(position).getName());

			return view;

		}

		class ViewHolder {
			TextView tvLetter;
			TextView tvTitle;
		}

		/**
		 * 根据ListView的当前位置获取分类的首字母的Char ascii值
		 */
		public int getSectionForPosition(int position) {
			return list.get(position).getSortLetters().charAt(0);
		}

		/**
		 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
		 */
		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = list.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}

		/**
		 * 提取英文的首字母，非英文字母用#代替。
		 * 
		 * @param str
		 * @return
		 */
		@SuppressLint("DefaultLocale")
		private String getAlpha(String str) {
			String sortStr = str.trim().substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private List<SortModel> filledData(JSONArray jso) throws JSONException {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < jso.length(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(jso.getJSONObject(i).getJSONObject("userInfo")
					.getString("nickName"));
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(jso.getJSONObject(i)
					.getJSONObject("userInfo").getString("nickName"));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

}
