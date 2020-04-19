package com.jcsamples.exchangerates;

import android.os.*;
import java.util.*;
import org.json.*;
import java.text.*;
import android.app.*;

public class AsyncExchangeRateLoader extends AsyncTask <CurrencyList, Void, String>
{
	private boolean isLatestData, result;
	private String serverMessage, errorMessage;
	private int[] dates;
	
	private OnLoadedListener onLoadedListener;
	private Activity activity;
	private OnRealtimeMessageListener onRealtimeMessageListener;
	private CurrencyList currencies;
	
	private static final int RECURSIVE_LIMIT = 30;
	private int CURRENT_RECURSIVE_COUNT = 0;
	
	public AsyncExchangeRateLoader (boolean isLatestData){
		this.isLatestData = isLatestData;
	}
	public AsyncExchangeRateLoader (int[] dates){
		this.dates = dates;
	}

	@Override
	protected String doInBackground(CurrencyList[] p1)
	{
		currencies = p1[0];
		
		if (isLatestData){
			latestExchangeLoader();
	   } else {  
		   Calendar cal = Calendar.getInstance();   
		   boolean isToday = cal.get(Calendar.YEAR) == dates[0] && cal.get(Calendar.MONTH) == dates[1] &&
			   cal.get(Calendar.DAY_OF_MONTH) == dates[2] ;
		   
		   if (isToday) latestExchangeLoader();
		   else {   
		   cal.set(dates[0], dates[1], dates[2]);
		   serverMessage = String.format("Exchange Rates at %d-%d-%d. ", dates[2], dates[1], dates[0]);   
		   recursiveExchangeHistoryLoader(cal);
		   }
	   }
	
	   return errorMessage;
	}

	private void latestExchangeLoader(){
		String curJson = ApiUtils.fetch(ApiUtils.LATEST);
		try {
			JSONObject obj = new JSONObject(curJson);
			long timestamp = obj.getLong("timestamp");
			serverMessage = String.format("Latest Exchange rates : updated at %s", parseTimestamp(timestamp));			
			addCurrencies(obj);
		} catch (JSONException e){
			errorMessage = curJson;
		}
	}

	private void recursiveExchangeHistoryLoader(Calendar cal){
		String curJson = ApiUtils.fetch(ApiUtils.HISTORY + dateQuery(cal));
		
		try {
			JSONObject obj = new JSONObject(curJson); 
			Object rateObj = obj.get("rates");
			if (rateObj instanceof JSONObject){
				long timestamp = obj.getLong("timestamp");
				serverMessage = serverMessage + String.format("Updated at %s", parseTimestamp(timestamp));
				addCurrencies(obj);
			} else if (CURRENT_RECURSIVE_COUNT != RECURSIVE_LIMIT){
				  CURRENT_RECURSIVE_COUNT += 1;  
				  String realtimeMessage = String.format("No data found at %s. searching ...", dateQuery(cal));
				  sendRealTimeMessage(realtimeMessage);
				  cal.add(Calendar.DATE, -1);	
				  recursiveExchangeHistoryLoader(cal);
			} else {
				errorMessage = "No data found since last month";
			}
			
		} catch (JSONException e){
			errorMessage = curJson;
		}
	}

	@Override
	protected void onPostExecute(String errorMessage){
		onLoadedListener.onLoaded(result, serverMessage, errorMessage);
	}
	
	private String parseTimestamp (long timestamp){
		Date date = new Date(timestamp * 1000l);
		SimpleDateFormat format = new SimpleDateFormat("h:mm a, MMMM d, yyyy, EEEE");
		return format.format(date);
	}
	
	private String dateQuery (Calendar cal){
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(cal.getTime());
	}
	
	private void addCurrencies (JSONObject obj) throws JSONException{
		JSONObject curObj = obj.getJSONObject("rates");
		Iterator<String> iterator = curObj.keys();

		while (iterator.hasNext()){
			String currency = iterator.next();
			String rate = curObj.getString(currency);
			currencies.addData(currency, rate);
		}
		result = true;
	}
	
	private void sendRealTimeMessage (final String message){
		activity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					onRealtimeMessageListener.set(message);
				}});
	}
	
	public interface OnLoadedListener {
		void onLoaded (boolean success, String serverMessage, String errorMessage);
	}

	public void setOnLoadedListener (OnLoadedListener listener){
		this.onLoadedListener = listener;
	}
	
	public interface OnRealtimeMessageListener {
		void set (String message);
	}

	public void setOnRealtimeMessageListener (Activity activity, OnRealtimeMessageListener listener){
		this.activity = activity; // needs activity to run on UiThread
		this.onRealtimeMessageListener = listener;
	}
	
}
