package com.wewin.labelprintsys;

import java.util.ArrayList;
import java.util.List;
import com.wewin.labelprintsys.adapter.MenuAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ������
 * 
 * @author HCOU
 * @date 2013-6-17
 */
public class MainActivity extends Activity {
	private long exitTime = 0;// �˳�����ʱ
	private ListView menuListView;
	private MenuAdapter menuAdapter;
	private List<String> list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initData();
		initView();
	}

	private void initView() {
		menuListView = (ListView) findViewById(R.id.menuListView);
		menuListView.setOnItemClickListener(new MyItemClickListhener());// ע�����¼�

		menuAdapter = new MenuAdapter(getApplicationContext(), list);
		menuListView.setAdapter(menuAdapter);
	}

	private void initData() {
		list = new ArrayList<String>();
		list.add("������ǩ");
		list.add("��·��ǩ");
		list.add("�˾���ǩ");
		list.add("���߸˱�ǩ");
		list.add("���±�ǩ");
		list.add("�⽻��ǩ");
		list.add("ODF��ǩ");
		list.add("�Զ����ǩ");
	}

	class MyItemClickListhener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			goToLabelListActivity(position);
		}
	}

	public void goToLabelListActivity(int position) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("labelFlag", position);
		intent.setClass(this, LabelListActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", 0).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}