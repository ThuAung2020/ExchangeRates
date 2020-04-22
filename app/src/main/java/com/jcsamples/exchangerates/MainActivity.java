package com.jcsamples.exchangerates;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.util.*;

public class MainActivity extends Activity 
{
	private ProgressBar progressBar;
	private ListView listView;
	private LinearLayout table;
	private Button button;
	private TextView tvMessage;
	
	private CurrencyList currencyList, exchangeRateList;
	private ExchangeListAdapter adapter;
	private int[] pickedDates;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		setupViews();
	
		currencyList = new CurrencyList();
		exchangeRateList = new CurrencyList();
		adapter = new ExchangeListAdapter (this, currencyList, exchangeRateList);
		listView.setAdapter(adapter);
		
		button.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					showCurrencyCalculator();
				}});
		
		loadCurrencies();
		
    }

	private void setupViews() {
		progressBar = findViewById(R.id.mainProgressBar1);
		tvMessage = findViewById(R.id.mainTextView1);
		listView = findViewById(R.id.mainListView1);
		table = findViewById(R.id.maininclude1);
		button = findViewById(R.id.mainButton1);
	}
	
	private void loadCurrencies() {
		hideRelatedViews();
		AsyncCurrencyListLoader loader = new AsyncCurrencyListLoader();
		loader.setOnLoadedListener(new AsyncCurrencyListLoader.OnLoadedListener(){
				@Override
				public void onLoaded(boolean success, String errorMessage) {
					if (success) loadExchangeRates(true, null);
			        else showToast(errorMessage);		
				}});
		loader.execute(currencyList);
	}
	
	private void loadExchangeRates(boolean isLatest, int[] dates) {
		exchangeRateList.clear();
		AsyncExchangeRateLoader loader = null;
		if (isLatest) loader = new AsyncExchangeRateLoader(isLatest);
		else loader = new AsyncExchangeRateLoader(dates);
		
		loader.setOnLoadedListener(new AsyncExchangeRateLoader.OnLoadedListener(){
				@Override
				public void onLoaded(boolean success, String serverMessage, String errorMessage){
					if (success){
						tvMessage.setText(serverMessage);
						showRelatedViews();
						adapter.notifyDataSetChanged();
					} else showToast(errorMessage);
				}});
				
		loader.setOnRealtimeMessageListener(this, new AsyncExchangeRateLoader.OnRealtimeMessageListener(){
				@Override
				public void set(String message){
					tvMessage.setText(message);
				}});
		loader.execute(exchangeRateList);
	};
	
	private void showRelatedViews(){
		progressBar.setVisibility(View.GONE);
		tvMessage.setVisibility(View.VISIBLE);
		table.setVisibility(View.VISIBLE);
	}
	
	private void hideRelatedViews(){
		progressBar.setVisibility(View.VISIBLE);
		tvMessage.setVisibility(View.GONE);
		table.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.itemDatePicker:
				showDatePickerDialog();
				break;		
			
			case R.id.itemInfo:
				showInfo();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showInfo(){
		new AlertDialog.Builder(this)
		. setTitle("About App")
		. setMessage(R.string.about_app)
		. setPositiveButton(R.string.ok, null)
		. show();
		
	}

	private void showDatePickerDialog(){
		DatePickerDialog dialog = null;
		if (pickedDates == null) {
			Calendar cal = Calendar.getInstance();
			dialog = new DatePickerDialog(this, null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		} else dialog = new DatePickerDialog (this, null, pickedDates[0], pickedDates[1], pickedDates[2]);
		
		dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener(){
				@Override
				public void onDateSet(DatePicker p1, int year, int month, int day){
					progressBar.setVisibility(View.VISIBLE);
					pickedDates = new int[] {year, month, day};
					loadExchangeRates(false, pickedDates);
				}});
		dialog.show();
	}
	
	private void showToast (String message){
		progressBar.setVisibility(View.GONE);
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void showCurrencyCalculator(){
		if (exchangeRateList.size() == 0) showToast("reading exchange rates ... please wait");
		else new CalculatorDialog(this, currencyList, exchangeRateList);
	}
	
}
