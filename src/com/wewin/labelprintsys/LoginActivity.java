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
 * 登录Activity
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
			if (checkNullInPutValue()) {// 检查是否输入用户名、密码
				doLoginRequest();// 发送登录请求
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 验证输入框是否为空
	 * 
	 * @date 2013-7-3
	 * @return
	 */
	private boolean checkNullInPutValue() {
		if ("".equals(userEdit.getText().toString())
				|| "".equals(passwdEdit.getText().toString())) {
			Toast.makeText(LoginActivity.this, "请输入用户名或密码", 0).show();
			return false;
		} else
			return true;
	}

	/**
	 * 发送登录请求
	 * 
	 * @date 2013-7-3
	 */
	private void doLoginRequest() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 验证数据
				List<String> list = new WebServiceUtil().doLogin(userEdit
						.getText().toString(), passwdEdit.getText().toString());
				// 设置用户ID保存全局变量
				((GlobalApplication) getApplication()).setUserId(list.get(0));

				result = list.get(1);// 验证结果
				// 更新界面，并记住用户、密码
				updateDialog();
				// 销毁窗口
				mDialog.dismiss();
			}
		}).start();
	}

	/**
	 * 更新界面线程,记住用户、密码
	 */
	public void updateDialog() {
		handler.post(new Runnable() {
			public void run() {
				if (result.equals("true")) {// 结果为真，表示验证成功，跳转到主页面
					goToMainActivity();// 跳转
				} else {
					// 验证失败
					Toast.makeText(LoginActivity.this, "用户名或密码错误", 0).show();
				}
			}
		});
	}

	/**
	 * 跳转到主页面
	 * 
	 * @date 2013-7-3
	 */
	public void goToMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();// 将活动推向后台
	}
}