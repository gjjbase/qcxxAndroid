package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.SideBar.OnTouchingLetterChangedListener;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressLint("DefaultLocale")
public class DetaActivity extends BaseActivity {
	private ThreadUtil thread;
	private ListView sortListView;
	private TextView id;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SideBar sideBar;
	private List<SortModel> SourceDateList;
	SortAdapter adapter;

	@SuppressLint("HandlerLeak")
	private void ini() {
		thread = new ThreadUtil(myhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'listCommonData'}]";
		String jsonParamStr = null;
		if (getIntent().getExtras().getString("typ").equals("1")) {
			jsonParamStr = "[{'cdType':" + 7 + "}]";
		} else if (getIntent().getExtras().getString("typ").equals("2")) {
			jsonParamStr = "[{'cdType':" + 1 + ",'cdParendId':"
					+ sp.getString("pasf_name", "") + "}]";
		} else if (getIntent().getExtras().getString("typ").equals("3")) {
			jsonParamStr = "[{'cdType':" + 2 + ",'cdParendId':"
					+ sp.getString("pasc_name", "") + "}]";
		}

		thread.doStartWebServicerequestWebService(DetaActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	Handler myhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					final JSONArray joA = new JSONArray(returnJson);
					// 设置右侧触摸监听
					sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

						@Override
						public void onTouchingLetterChanged(String s) {
							// 该字母首次出现的位置
							int position = adapter.getPositionForSection(s
									.charAt(0));
							if (position != -1) {
								sortListView.setSelection(position);
							}

						}
					});

					sortListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
									Toast.makeText(
											getApplication(),
											((SortModel) adapter
													.getItem(position))
													.getName(),
											Toast.LENGTH_SHORT).show();
								}
							});

					SourceDateList = filledData(joA);

					// 根据a-z进行排序源数据
					// .sort(SourceDateList, pinyinComparator);
					Collections.sort(SourceDateList, pinyinComparator);
					adapter = new SortAdapter(DetaActivity.this, SourceDateList);
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

	@SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_datactivity);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		id = (TextView) findViewById(R.id.id);
		if (getIntent().getExtras().getString("typ").equals("1")) {
			id.setText("省份");
		} else if (getIntent().getExtras().getString("typ").equals("1")) {
			id.setText("学校");
		} else {
			id.setText("学院");
		}
		ini();
	}

	public class SortModel {

		private String name; // 显示的数据
		private String sortLetters; // 显示数据拼音的首字母
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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
				viewHolder.rel = (RelativeLayout) view.findViewById(R.id.rel);
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
			viewHolder.rel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (getIntent().getExtras().getString("typ").equals("1")) {
						editor.putString("sf_name", list.get(position)
								.getName());
						editor.putString("pasf_name", list.get(position)
								.getId());
					} else if (getIntent().getExtras().getString("typ")
							.equals("2")) {
						editor.putString("sc_name", list.get(position)
								.getName());
						editor.putString("pasc_name", list.get(position)
								.getId());
					} else if (getIntent().getExtras().getString("typ")
							.equals("3")) {
						editor.putString("xy_name", list.get(position)
								.getName());
						editor.putString("paxy_name", list.get(position)
								.getId());
					}
					editor.commit();
					finish();
				}
			});

			viewHolder.tvTitle.setText(this.list.get(position).getName());

			return view;

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

		class ViewHolder {
			TextView tvLetter;
			TextView tvTitle;
			RelativeLayout rel;
		}
	}

	private List<SortModel> filledData(JSONArray data) throws JSONException {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < data.length(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(data.getJSONObject(i).getString("cdMc"));
			sortModel.setId(data.getJSONObject(i).getString("cdId"));
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data.getJSONObject(i)
					.getString("cdMc"));
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

	public class PinyinComparator implements Comparator<SortModel> {

		public int compare(SortModel o1, SortModel o2) {
			if (o1.getSortLetters().equals("@")
					|| o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#")
					|| o2.getSortLetters().equals("@")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}

	}
}
