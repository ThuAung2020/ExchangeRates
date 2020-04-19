package com.jcsamples.exchangerates;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import com.jcsamples.exchangerates.CalculatorDialog.*;
import android.database.*;
import android.widget.AdapterView.*;
import android.text.*;

public class CalculatorDialog {
	
	private CurrencyList currencyList, exchangeRateList;
	private Spinner spinner1, spinner2;
	private EditText edt1, edt2;
	
	public CalculatorDialog (Context context, CurrencyList currencyList, CurrencyList exchangeRateList){
		this.currencyList = currencyList;
		this.exchangeRateList = exchangeRateList;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View v = LayoutInflater.from(context).inflate(R.layout.dialog_calculator, null, false);
		builder.setView(v);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		spinner1 = v.findViewById(R.id.dialogcalculatorSpinner1);
		spinner2 = v.findViewById(R.id.dialogcalculatorSpinner2);
		Button button = v.findViewById(R.id.dialogcalculatorButton1);
		edt1 = v.findViewById(R.id.dialogcalculatorEditText1);
		edt2 = v.findViewById(R.id.dialogcalculatorEditText2);
		
		List<String> modelList = new ArrayList<>();
		addModels (modelList); 
		ArrayAdapter <String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, modelList);          
		
		spinner1.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new SpinnerItemSelectedListener());	
		spinner2.setAdapter(adapter);
		spinner2.setOnItemSelectedListener(new SpinnerItemSelectedListener());
		spinner1.setSelection(1);
		
		button.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					dialog.dismiss();
				}});
		
		dialog.show();
		setTextChangedListeners();
	}

	private void setTextChangedListeners(){
		edt1.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					if (edt1.getTag() == null || !(boolean) edt1.getTag()){
						calculateRow(2);
					}
				}
			});
		
		edt2.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					if (edt2.getTag() == null || !(boolean) edt2.getTag()){
						calculateRow(1);
					}
				}
			});
	}

	private void addModels(List<String> modelList){
	   modelList.add("Myanmar Kyat MMK");
	   for (int i = 0; i < currencyList.size(); i++){
		   CurrencyList.CurrencyModel cmodel = currencyList.get(i);
		   modelList.add(cmodel.value + " " + cmodel.key);
	   }
	}
	
	private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int position, long p4){
			TextView tv = (TextView) p2;
			tv.setText(getCurrencyBy(position));
			calculateRow(2); // always calculate row 2
		}
		@Override
		public void onNothingSelected(AdapterView<?> p1){	
		}	
	}
	
	private static double getDouble (String s){
		NumberFormat format = NumberFormat.getInstance();
		try {
			return format.parse(s).doubleValue();
		} catch (ParseException e){}
		return 0;
	}

	private static String toFormatedNumber (double d){
		NumberFormat format = NumberFormat.getInstance();
	    return format.format(d).toString();	
	}
	
	private double getFourDecimalDouble (double d){
		DecimalFormat df = new DecimalFormat("#.####");
		return Double.parseDouble(df.format(d));
	}
	
	private String getCurrencyBy (int position){
		if (position == 0) return "MMK";
		else return currencyList.get(position-1).key;
	}
	
	private void calculateRow(int row){
		String row1Currency = getCurrencyBy(spinner1.getSelectedItemPosition());
		String row2Currency = getCurrencyBy(spinner2.getSelectedItemPosition());

		double row1ExchangeRate = getDouble(exchangeRateList.getValueWith(row1Currency));
		double row2ExchangeRate = getDouble(exchangeRateList.getValueWith(row2Currency));
		
		switch (row)
		{
			case 1:
				String row2Text = edt2.getText().toString().trim();
				row2Text = row2Text.isEmpty() ? "0" : row2Text;		
				double row2Value =  getDouble(row2Text);	

				if (row2Value == 0) setText(edt1, 0);
				else {
					double myanmarKyat = row2Value * row2ExchangeRate;
					double result =  myanmarKyat / row1ExchangeRate;
					result = getFourDecimalDouble(result);
					setText(edt1, result);
				}
				break;
			
			case 2:
				String row1Text = edt1.getText().toString().trim();
				row1Text = row1Text.isEmpty() ? "0" : row1Text;		
				double row1Value =  getDouble(row1Text);	

				if (row1Value == 0) setText(edt2, 0);
				else {
					double myanmarKyat = row1Value * row1ExchangeRate;
					double result =  myanmarKyat / row2ExchangeRate;
					result = getFourDecimalDouble(result);
					setText(edt2, result);
				}
				break;
				
		}

	}
	
	private void setText (EditText edt, double result){
		edt.setTag(true); // set text by app
		edt.setText(toFormatedNumber(result));
		edt.setTag(false); // reset
	}
	
}
