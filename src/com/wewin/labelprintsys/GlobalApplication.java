package com.wewin.labelprintsys;

import android.app.Application;

/**
 * 存储用户基本信息的全局变量
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
