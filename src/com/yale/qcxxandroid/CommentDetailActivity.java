package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.CircularImage;
import com.yale.qcxxandroid.bean.Comment;

public class CommentDetailActivity extends BaseActivity {
	CircularImage myPic;
    private ListView commentView;  
    private List<Comment> comments = new ArrayList<Comment>(); 
    private CommentListAdapter adapter;  	
	Intent intent = new Intent();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_detaill_activity);
		commentView = (ListView) findViewById(R.id.listView);  
        init();
        adapter = new CommentListAdapter(this, comments);
        commentView.setAdapter(adapter); 
	}

	
    public void init(){
    	comments.clear();
		Comment c1 = new Comment();  
		c1.setPubTime("11月20日");
		c1.setContent(Html.fromHtml("<html>好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？好玩吗？</html>"));
		c1.setName("邱金勇 武汉大学");
		c1.setUserPic("");
		comments.add(c1);  
		Comment c2 = new Comment();  
		c2.setPubTime("12月21日");
		c2.setContent(Html.fromHtml("<html>应该可以吧？</html>"));
		c2.setName("陈璐 武汉大学");
		c2.setUserPic("");
		comments.add(c2);  
		Comment c3 = new Comment();  
		c3.setPubTime("12月21日");
		c3.setContent(Html.fromHtml("<html>应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？</html>"));
		c3.setName("陈璐2 华中科技大学");
		c3.setUserPic("");
		comments.add(c3);  
		Comment c4 = new Comment();  
		c4.setPubTime("12月21日");
		c4.setContent(Html.fromHtml("<html>应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？</html>"));
		c4.setName("陈璐4 华中科技大学");
		c4.setUserPic("");
		comments.add(c4);  
		Comment c5 = new Comment();  
		c5.setPubTime("12月21日");
		c5.setContent(Html.fromHtml("<html>应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？</html>"));
		c5.setName("陈璐5 华中科技大学");
		c5.setUserPic("");
		comments.add(c5);  	
		Comment c6 = new Comment();  
		c6.setPubTime("12月21日");
		c6.setContent(Html.fromHtml("<html>应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？应该可以吧？</html>"));
		c6.setName("陈璐6 华中科技大学");
		c6.setUserPic("");
		comments.add(c6);  
	}
    
    public void backOnclick(View view){
 		this.finish();
    }
}
