
// Soukaina Salihi

package com.murach.mortgagecalculator;

import java.text.NumberFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.murach.mortgagecalculator.R;

// MortgageCalculator is an aplication that helps the user estimate the monthly mortgage payment
public class MortgageCalculatorActivity extends Activity
implements OnEditorActionListener, OnClickListener {

    // define variables for the widgets
    private EditText mortgageAmountEditText;
    private EditText mortgageTermEditText;

    private TextView percentTextView;   
    private Button   percentUpButton;
    private Button   percentDownButton;
    //private TextView tipTextView;
    private TextView totalTextView;
    
    // define the SharedPreferences object
    private SharedPreferences savedValues;
    
    // define instance variables that should be saved
    private String mortgageAmountString = "";
    private String mortgageTermString = "";

    private float percent = .15f;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_calculator);
        
        // get references to the widgets
        mortgageAmountEditText = (EditText) findViewById(R.id.mortgageAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentUpButton = (Button) findViewById(R.id.percentUpButton);
        percentDownButton = (Button) findViewById(R.id.percentDownButton);
        mortgageTermEditText = (EditText) findViewById(R.id.mortgageTermEditText);
       // tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        // set the listeners
        mortgageAmountEditText.setOnEditorActionListener(this);
        mortgageTermEditText.setOnEditorActionListener(this);
        percentUpButton.setOnClickListener(this);
        percentDownButton.setOnClickListener(this);
        
        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);        
    }
    
    @Override
    public void onPause() {
        // save the instance variables       
        Editor editor = savedValues.edit();        
        editor.putString("mortgageAmountString", mortgageAmountString);
        editor.putString("mortgageTermString", mortgageTermString);
        editor.putFloat("percent", percent);

        editor.commit();        

        super.onPause();      
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // get the instance variables
        mortgageAmountString = savedValues.getString("mortgageAmountString", "");
        mortgageTermString = savedValues.getString("mortgageTermString", "10");

        percent = savedValues.getFloat("percent", 0.15f);

        // set the bill amount on its widget
        mortgageAmountEditText.setText(mortgageAmountString);
        mortgageTermEditText.setText(mortgageTermString);


        // calculate and display
        calculateAndDisplay();
    }    
    
    public void calculateAndDisplay() {

        // get the bill amount
        mortgageAmountString = mortgageAmountEditText.getText().toString();
        mortgageTermString = mortgageTermEditText.getText().toString();

        float mortgageAmount;
        float mortgageTerm;
        if (mortgageAmountString.equals("")) {
            mortgageAmount = 0;
        }
        else {
            mortgageAmount = Float.parseFloat(mortgageAmountString);
        }
        if (mortgageTermString.equals("")) {
            mortgageTerm = 1;
        }
        else {
            mortgageTerm = Float.parseFloat(mortgageTermString);
        }
        
        // calculate tip and total
        double term= Math.pow((1 + (((double)percent) / 12.0)), (12.0 * mortgageTerm));
       // float termF=(float)term;
        double paymentD = (mortgageAmount * ((percent) / 12.) * term) / (term - 1);
        float payment = (float) paymentD;
        // System.out.print(tipAmount);
       // System.out.print(totalAmount);
        // display the other results with formatting

        NumberFormat currency = NumberFormat.getCurrencyInstance();
       // tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(payment));
        
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentTextView.setText(percentFormat.format(percent));
    }
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }        
        return false;
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.percentDownButton:
            percent = percent - .01f;
            calculateAndDisplay();
            break;
        case R.id.percentUpButton:
            percent = percent + .01f;
            calculateAndDisplay();
            break;
        }
    }
}
