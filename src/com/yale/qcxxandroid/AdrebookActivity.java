package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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

import com.yale.qcxxandroid.SideBar.OnTouchingLetterChangedListener;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.chat.ChatMainActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressLint("DefaultLocale")
public class AdrebookActivity extends BaseActivity {
	private ListView sortListView;
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
	private LinearLayout bottom, lin_br;
	private RelativeLayout toper_lin;
	private TextView cancel;

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

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adrebook);
		search = (TextView) findViewById(R.id.search);
		cancel = (TextView) findViewById(R.id.cancel);
		searcher = (EditText) findViewById(R.id.searcher);
		lin_br = (LinearLayout) findViewById(R.id.lin_br);
		toper_lin = (RelativeLayout) findViewById(R.id.toper_lin);
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

		SourceDateList = filledData(getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
	}

	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
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
