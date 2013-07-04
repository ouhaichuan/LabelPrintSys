package com.wewin.labelprintsys;

import java.util.List;
import com.wewin.labelprintsys.service.WebServiceUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��¼Activity
 * 
 * @author HCOU
 * @date 2013-7-3
 */
public class LoginActivity extends Activity implements OnClickListener {
	Button loginBtn;
	EditText userEdit, passwdEdit;
	private Dialog mDialog;
	private String result;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		loginBtn = (Button) findViewById(R.id.login_login_btn);
		loginBtn.setOnClickListener(this);
		passwdEdit = (EditText) findViewById(R.id.login_passwd_edit);
		userEdit = (EditText) findViewById(R.id.login_user_edit);

		handler = new Handler();
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.login_login_btn:
			if (checkNullInPutValue()) {// ����Ƿ������û���������
				doLoginRequest();// ���͵�¼����
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ��֤������Ƿ�Ϊ��
	 * 
	 * @date 2013-7-3
	 * @return
	 */
	private boolean checkNullInPutValue() {
		if ("".equals(userEdit.getText().toString())
				|| "".equals(passwdEdit.getText().toString())) {
			Toast.makeText(LoginActivity.this, "�������û���������", 0).show();
			return false;
		} else
			return true;
	}

	/**
	 * ���͵�¼����
	 * 
	 * @date 2013-7-3
	 */
	private void doLoginRequest() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "������֤�˺�...");
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ��֤����
				List<String> list = new WebServiceUtil().doLogin(userEdit
						.getText().toString(), passwdEdit.getText().toString());
				// �����û�ID����ȫ�ֱ���
				((GlobalApplication) getApplication()).setUserId(list.get(0));

				result = list.get(1);// ��֤���
				// ���½��棬����ס�û�������
				updateDialog();
				// ���ٴ���
				mDialog.dismiss();
			}
		}).start();
	}

	/**
	 * ���½����߳�,��ס�û�������
	 */
	public void updateDialog() {
		handler.post(new Runnable() {
			public void run() {
				if (result.equals("true")) {// ���Ϊ�棬��ʾ��֤�ɹ�����ת����ҳ��
					goToMainActivity();// ��ת
				} else {
					// ��֤ʧ��
					Toast.makeText(LoginActivity.this, "�û������������", 0).show();
				}
			}
		});
	}

	/**
	 * ��ת����ҳ��
	 * 
	 * @date 2013-7-3
	 */
	public void goToMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();// ��������̨
	}
}