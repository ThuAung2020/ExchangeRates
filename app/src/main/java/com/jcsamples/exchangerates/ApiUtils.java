package com.jcsamples.exchangerates;

import java.io.*;
import java.net.*;

public class ApiUtils
{
	public static final String LATEST = "https://forex.cbm.gov.mm/api/latest";
	public static final String CURRENCIES = "https://forex.cbm.gov.mm/api/currencies";
	public static final String HISTORY = "https://forex.cbm.gov.mm/api/history/";
	
	public static String fetch(String s)
	{
		String result = "";
		try
		{		
			URL url = new URL(s);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");	

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length;

				while ((length = conn.getInputStream().read(buffer)) != -1) {
					ba.write(buffer, 0, length);
				}
				ba.close();
				result = ba.toString("UTF-8");
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;	
	}


}
