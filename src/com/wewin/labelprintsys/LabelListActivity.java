package com.wewin.labelprintsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import wewin.com.cn.Print;
import com.wewin.labelprintsys.adapter.ListAdapter;
import com.wewin.labelprintsys.service.WebServiceUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

/**
 * 标签显示列表
 * 
 * @author HCOU
 * @date 2013-7-1
 */
public class LabelListActivity extends Activity {
	private ListView labelListView;
	private ListAdapter listAdapter;
	private List<HashMap<String, String>> list;
	private List<HashMap<String, String>> cachelist;
	private TextView title;
	private int labelFlag;
	private Button print_btn, cls_btn;
	private List<Integer> positons;
	private Dialog mDialog;
	private Handler handler;
	private EditText txt_num;
	private WebServiceUtil serviceUtil;
	private Map<String, String> printinfo_map = new HashMap<String, String>();
	Print pp = new Print();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labellist);
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pp.Close();
	}

	/**
	 * 初始化组件
	 * 
	 * @date 2013-7-1
	 */
	private void initView() {
		Intent intent = getIntent();
		labelFlag = intent.getIntExtra("labelFlag", 9);

		labelListView = (ListView) findViewById(R.id.labelListView);
		labelListView.setOnItemClickListener(new MyItemClickListhener());// 注册点击事件
		labelListView.setOnItemLongClickListener(new ItemLongClickListhener());// 长按事件
		title = (TextView) findViewById(R.id.listTitle);
		initTitle();// 初始化标题

		print_btn = (Button) findViewById(R.id.print_btn);
		cls_btn = (Button) findViewById(R.id.cls_btn);
		print_btn.setOnClickListener(new BtnOnclickListener());
		cls_btn.setOnClickListener(new BtnOnclickListener());

		positons = new ArrayList<Integer>();
		cachelist = new ArrayList<HashMap<String, String>>();

		handler = new Handler();
		serviceUtil = new WebServiceUtil();
	}

	/**
	 * 初始化标题
	 * 
	 * @date 2013-7-1
	 */
	public void initTitle() {
		switch (labelFlag) {
		case 0:
			title.setText("调单标签");
			break;
		case 1:
			title.setText("光路标签");
			break;
		case 2:
			title.setText("人井标签");
			break;
		case 3:
			title.setText("电线杆标签");
			break;
		case 4:
			title.setText("电缆标签");
			break;
		case 5:
			title.setText("光交标签");
			break;
		case 6:
			title.setText("ODF标签");
			break;
		case 7:
			title.setText("自定义标签");
			break;
		default:
			break;
		}
	}

	/**
	 * 加载数据
	 * 
	 * @date 2013-7-1
	 */
	private void initData() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在加载数据...");
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				list = serviceUtil.selectLabelList(labelFlag);

				updateDialog();
				// 销毁窗口
				mDialog.dismiss();
			}
		}).start();
	}

	/**
	 * 更新界面
	 * 
	 * @date 2013-7-2
	 */
	public void updateDialog() {
		handler.post((new Runnable() {
			@Override
			public void run() {
				listAdapter = new ListAdapter(getApplicationContext(), list);
				labelListView.setAdapter(listAdapter);
			}
		}));
	}

	/**
	 * list item点击事件
	 * 
	 * @author HCOU
	 * @date 2013-7-2
	 */
	class MyItemClickListhener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_chk);
			checkBox.setChecked(!checkBox.isChecked());
			ListAdapter.getIsSelected().put(position, checkBox.isChecked());

			String name = ((TextView) view.findViewById(R.id.txt_name))
					.getText().toString();
			String code = ((TextView) view.findViewById(R.id.txt_code))
					.getText().toString();
			if (checkBox.isChecked())
				printinfo_map.put(code, name);
			else
				printinfo_map.remove(code);
		}
	}

	/**
	 * 按钮点击事件
	 * 
	 * @author HCOU
	 * @date 2013-7-2
	 */
	class BtnOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.print_btn:// 打印按钮
				preparePrintData();// 准备数据
				break;
			case R.id.cls_btn:// 清除选择按钮
				unCheckedSelected();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 输入打印份数
	 * 
	 * @date 2013-7-2
	 */
	public void inputPrintNum() {
		txt_num = new EditText(this);
		txt_num.setInputType(InputType.TYPE_CLASS_NUMBER);

		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("打印确认")
				.setIcon(getResources().getDrawable(R.drawable.warning))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showPrintDialog();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create();
		alertDialog.show();
	}

	/**
	 * 打印数据准备
	 * 
	 * @date 2013-7-2
	 */
	public void preparePrintData() {
		/**
		 * 清空数据缓存
		 */
		positons.clear();
		cachelist.clear();

		Iterator<Entry<Integer, Boolean>> iterator = ListAdapter
				.getIsSelected().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Boolean> entry = (Entry<Integer, Boolean>) iterator
					.next();
			boolean value = entry.getValue();
			int key = entry.getKey();

			if (value) {// 保存行号
				positons.add(key);
			}
		}
		for (int i = 0; i < positons.size(); i++) {
			cachelist.add(list.get(positons.get(i)));// 加入缓存
		}
		if (cachelist.size() <= 0) {
			Toast.makeText(getApplicationContext(), "请选择需要打印的项目", 0).show();
		} else {
			inputPrintNum();// 输入打印份数
		}
	}

	/**
	 * 访问打印请求
	 * 
	 * @date 2013-7-2
	 * @return
	 */
	public void showPrintDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "开始打印...");
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				doPrint();
				mDialog.dismiss();
			}
		}).start();

	}

	/**
	 * 调用打印接口
	 * 
	 * @date 2013-7-2
	 */
	public void doPrint() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<String, String> entry : printinfo_map.entrySet()) {
					pp.QS03QRcodePrint(entry.getKey(), entry.getValue());
				}
			}
		});
	}

	/**
	 * 清除选择状态
	 * 
	 * @date 2013-7-2
	 */
	public void unCheckedSelected() {
		for (int i = 0; i < list.size(); i++) {
			ListAdapter.getIsSelected().put(i, false);
		}
		printinfo_map.clear();// 清空
		dataChanged();
	}

	private void dataChanged() {
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 返回主菜单
	 * 
	 * @date 2013-7-1
	 */
	public void goBackToMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBackToMainActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * list view 长按事件
	 * 
	 * @author HCOU
	 * @date 2013-7-3
	 */
	class ItemLongClickListhener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				int arg2, long arg3) {
			String labelId = ((TextView) view.findViewById(R.id.txt_labelid))
					.getText().toString();
			goToDetailedActivity(labelId);
			return true;
		}
	}

	/**
	 * 跳转到详细页面
	 * 
	 * @date 2013-7-3
	 */
	public void goToDetailedActivity(String labelId) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("labelId", labelId);
		bundle.putInt("labelFlag", labelFlag);
		intent.setClass(this, LabelDetailedActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}