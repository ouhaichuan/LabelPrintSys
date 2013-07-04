package com.wewin.labelprintsys.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * ����WebService�����ڵײ���
 * 
 * @author HCOU
 * @date 2013-7-2
 */
public class HttpConnSoap {
	public ArrayList<String> GetWebService(String methodName,
			ArrayList<String> Parameters, ArrayList<String> ParValues) {
		ArrayList<String> Values = new ArrayList<String>();

		// ServerUrl��ָwebservice��url
		// 10.0.2.2����androidģ�������ʱ��أ�PC��������������д��127.0.0.1
		// 2364��ָ�˿ںţ������ص�IIS�ϵ�ʱ�����Ķ˿�
		// DbService.asmx��ָ�ṩ�����ҳ��
		// ���Ե�ַ
		// String ServerUrl =
		// "http://www.wewin.com.cn:1640/LabelDbWebService.asmx";
		String ServerUrl = "http://218.206.25.36:12015/labelservice/LabelDbWebService.asmx";
		// ������ַ
		// String ServerUrl = "http://10.0.2.2:1640/LabelDbWebService.asmx";

		// String soapAction="http://tempuri.org/LongUserId1";
		String soapAction = "http://tempuri.org/" + methodName;
		// String data = "";
		String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body />";
		String tps, vps, ts;
		String mreakString = "";

		mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";
		for (int i = 0; i < Parameters.size(); i++) {
			tps = Parameters.get(i).toString();
			// ���ø÷����Ĳ���Ϊ.net webService�еĲ�������
			vps = ParValues.get(i).toString();
			ts = "<" + tps + ">" + vps + "</" + tps + ">";
			mreakString = mreakString + ts;
		}
		mreakString = mreakString + "</" + methodName + ">";
		/*
		 * +"<HelloWorld xmlns=\"http://tempuri.org/\">" +"<x>string11661</x>"
		 * +"<SF1>string111</SF1>" + "</HelloWorld>"
		 */
		String soap2 = "</soap:Envelope>";
		String requestData = soap + mreakString + soap2;

		try {
			URL url = new URL(ServerUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			byte[] bytes = requestData.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(6000);// ���ó�ʱʱ��
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.setRequestProperty("SOAPAction", soapAction);
			con.setRequestProperty("Content-Length", "" + bytes.length);
			OutputStream outStream = con.getOutputStream();
			outStream.write(bytes);
			outStream.flush();
			outStream.close();
			InputStream inStream = con.getInputStream();
			Values = inputStreamtovaluelist(inStream, methodName);
			inStream.close();
			return Values;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}

	public ArrayList<String> inputStreamtovaluelist(InputStream in,
			String MonthsName) throws IOException {
		StringBuffer out = new StringBuffer();
		String s1 = "";

		byte[] b = new byte[1024 * 1024];
		ArrayList<String> values = new ArrayList<String>();

		for (int n; (n = in.read(b)) != -1;) {
			s1 = new String(b, 0, n);
			out.append(s1);
		}

		String[] s13 = out.toString().split("><");
		String ifString = MonthsName + "Result";
		String TS = "";
		String vs = "";

		Boolean getValueBoolean = false;
		for (int i = 0; i < s13.length; i++) {
			TS = s13[i];
			int j, k, l;
			j = TS.indexOf(ifString);
			k = TS.lastIndexOf(ifString);

			if (j >= 0) {
				if (getValueBoolean == false) {
					getValueBoolean = true;
				} else {
				}
				if ((j >= 0) && (k > j)) {
					l = ifString.length() + 1;
					vs = TS.substring(j + l, k - 2);
					values.add(vs);
					getValueBoolean = false;
					return values;
				}
			}
			if (TS.lastIndexOf("/" + ifString) >= 0) {
				getValueBoolean = false;
				return values;
			}
			if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0)
					&& (j < 0)) {
				k = TS.length();
				vs = TS.substring(7, k - 8);
				values.add(vs);
			}
		}
		return values;
	}
}
