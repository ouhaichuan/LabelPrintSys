package com.wewin.labelprintsys;

import android.app.Application;

/**
 * �洢�û�������Ϣ��ȫ�ֱ���
 * 
 * @author HCOU
 * @date 2013-7-2
 */
public class GlobalApplication extends Application {
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
