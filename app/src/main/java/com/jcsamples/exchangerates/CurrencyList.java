package com.jcsamples.exchangerates;

import java.util.*;
import android.text.*;

public class CurrencyList {
	private List<CurrencyModel> currencyList;
	
	public CurrencyList(){
		currencyList = new ArrayList<>();
	}
	
	public class CurrencyModel {
		public String key, value;
		public CurrencyModel (String currency, String country){
			this.key = currency;
			this.value = country;
		}
	}
	
	public void addData (String key, String value){
		CurrencyModel model = new CurrencyModel (key, value);
		currencyList.add(model);
	}
	
	public String getValueWith(String key){
		for (CurrencyModel model : currencyList){
			if (TextUtils.equals(key, model.key)){
				return model.value;
			}
		}
		return "1"; // 1 Kyat for MMK
	}
	
	public int size(){
		return currencyList.size();
	}
	
	public CurrencyModel get (int position){
		return currencyList.get(position);
	}
	
	public void clear(){
		currencyList.clear();
	}
}
