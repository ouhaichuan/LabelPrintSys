package com.wewin.labelprintsys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据中间层，访问Soap，并处理获得的数据
 * 
 * @author HCOU
 * @date 2013-7-2
 */
public class WebServiceUtil {
	private ArrayList<String> arrayList = new ArrayList<String>();
	private ArrayList<String> brrayList = new ArrayList<String>();
	private ArrayList<String> crrayList = new ArrayList<String>();
	private HttpConnSoap Soap = new HttpConnSoap();

	/**
	 * 获取标签信息
	 * 
	 * @date 2013-7-2
	 * @param labelFlag
	 * @param userid
	 * @return
	 */
	public List<HashMap<String, String>> selectLabelList(int labelFlag) {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		arrayList.clear();
		brrayList.clear();
		crrayList.clear();

		// 传递标签类型
		arrayList.add("labelFlag");
		brrayList.add(labelFlag + "");

		crrayList = Soap.GetWebService("selectLabelList", arrayList, brrayList);
		for (int j = 0; j < crrayList.size(); j += 4) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("id", crrayList.get(j));
			hashMap.put("name", crrayList.get(j + 1));
			hashMap.put("code", crrayList.get(j + 2));
			hashMap.put("isprint", crrayList.get(j + 3));

			list.add(hashMap);
		}

		return list;
	}

	/**
	 * 验证用户登录
	 */
	public List<String> doLogin(String userid, String password) {
		arrayList.clear();
		brrayList.clear();

		arrayList.add("username");
		arrayList.add("password");

		brrayList.add(userid);
		brrayList.add(password);

		crrayList = Soap.GetWebService("doLogin", arrayList, brrayList);
		return crrayList;
	}

	public List<String> selectLabelDetailed(String labelId) {
		arrayList.clear();
		brrayList.clear();

		arrayList.add("labelId");
		brrayList.add(labelId);

		crrayList = Soap.GetWebService("selectLabelDetailed", arrayList,
				brrayList);
		return crrayList;
	}
}
