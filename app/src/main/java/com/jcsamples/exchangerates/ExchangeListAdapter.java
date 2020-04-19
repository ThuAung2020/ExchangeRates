package com.jcsamples.exchangerates;
import android.widget.*;
import android.view.*;
import android.content.*;

public class ExchangeListAdapter extends BaseAdapter
{
	private LayoutInflater inflater;
	private CurrencyList currencyList, exchangeRateList;
	
	public ExchangeListAdapter (Context context, CurrencyList currencyList, CurrencyList exchangeRateList){
		inflater = LayoutInflater.from(context);
		this.currencyList = currencyList;
		this.exchangeRateList = exchangeRateList;
	}
	
	@Override
	public int getCount() {
		return exchangeRateList.size();
	}

	@Override
	public Object getItem(int p1){
		return p1;
	}

	@Override
	public long getItemId(int p1){
		return p1;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup){
		if (view == null)
			view = inflater.inflate(R.layout.item_row, viewGroup, false);
		
		TextView tvCountry = view.findViewById(R.id.itemrowTextView1);
		TextView tvCurrency = view.findViewById(R.id.itemrowTextView2);
		TextView tvRate = view.findViewById(R.id.itemrowTextView3);
		
		CurrencyList.CurrencyModel model = exchangeRateList.get(position);
		
		tvCountry.setText(currencyList.getValueWith(model.key));
		tvCurrency.setText(model.key);
		tvRate.setText(model.value);
		
		return view;
	}

   
}
