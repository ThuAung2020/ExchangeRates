package com.jcsamples.exchangerates;

import android.os.*;
import org.json.*;
import java.util.*;

public class AsyncCurrencyListLoader extends AsyncTask <CurrencyList, Void, String>
{
	private OnLoadedListener onLoadedListener;
	private boolean result;
	@Override
	protected String doInBackground(CurrencyList[] p1)
	{
		CurrencyList currencies = p1[0];
		String curJson = ApiUtils.fetch(ApiUtils.CURRENCIES);
		try {
			JSONObject obj = new JSONObject(curJson);
			JSONObject curObj = obj.getJSONObject("currencies");
			Iterator<String> iterator = curObj.keys();
			
			  while (iterator.hasNext()){
				String currency = iterator.next();
				String country = curObj.getString(currency);
				currencies.addData(currency, country);
			  }
			  result = true;
			  return null;
			} catch (JSONException e){
			return curJson;
		}
	}

	@Override
	protected void onPostExecute(String errorMessage){
		onLoadedListener.onLoaded(result, errorMessage);
	}
	
	public interface OnLoadedListener {
		void onLoaded (boolean success, String errorMessage);
	}
	
	public void setOnLoadedListener (OnLoadedListener listener){
		this.onLoadedListener = listener;
	}
}
