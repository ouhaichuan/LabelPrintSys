package com.wewin.labelprintsys;

import java.util.List;
import com.wewin.labelprintsys.service.WebServiceUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

public class LabelDetailedActivity extends Activity {
	private String labelId;
	private int labelFlag;
	private WebServiceUtil dbUtil;
	private Dialog mDialog;
	private List<String> list;
	private Handler handler;
	private EditText txt_labelname, txt_luyou, txt_title, txt_speed, txt_rote,
			txt_jiandan, txt_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labeldetailed);

		initView();
	}

	/**
	 * 初始化界面
	 * 
	 * @date 2013-7-3
	 */
	private void initView() {
		Intent intent = getIntent();
		labelId = intent.getStringExtra("labelId");
		labelFlag = intent.getIntExtra("labelFlag", 9);

		dbUtil = new WebServiceUtil();
		handler = new Handler();

		txt_labelname = (EditText) findViewById(R.id.txt_labelname);
		txt_luyou = (EditText) findViewById(R.id.txt_luyou);
		txt_title = (EditText) findViewById(R.id.txt_title);
		txt_speed = (EditText) findViewById(R.id.txt_speed);
		txt_rote = (EditText) findViewById(R.id.txt_rote);
		txt_jiandan = (EditText) findViewById(R.id.txt_jiandan);
		txt_code = (EditText) findViewById(R.id.txt_code);

		setViewData();// 查询页面要显示的数据
	}

	/**
	 * 设置listView
	 */
	private void setViewData() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在加载数据...");
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = dbUtil.selectLabelDetailed(labelId);
				// 更新界面
				updateView();
				// 销毁窗口
				mDialog.dismiss();
			}
		}).start();
	}

	/**
	 * 更新视图
	 * 
	 * @date 2013-6-6
	 */
	public void updateView() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				txt_labelname.setText(list.get(0).toString());
				txt_luyou.setText(list.get(1).toString());
				txt_title.setText(list.get(2).toString());
				txt_speed.setText(list.get(3).toString());
				txt_rote.setText(list.get(4).toString());
				txt_jiandan.setText(list.get(5).toString());
				txt_code.setText(list.get(6).toString());
			}
		});
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
	 * KeyEvent.ACTION_DOWN) { goBackToListActivity(); return true; } return
	 * super.onKeyDown(keyCode, event); }
	 */

	public void goBackToListActivity() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("labelFlag", labelFlag);
		intent.setClass(this, LabelListActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
