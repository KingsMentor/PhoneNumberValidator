package com.belvi.phonenumbervalidatorsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.belvi.validator.PhoneFormatException;
import com.belvi.validator.PhoneModel;
import com.belvi.validator.PhoneNumberValidator;
import com.belvi.validator.PhoneNumberValidator.Country;


public class MainActivity extends AppCompatActivity {

    Country country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.countrySpinner);
        final TextView textView = (TextView) findViewById(R.id.countryCode);
        final TextView outputTextView = (TextView) findViewById(R.id.output);
        final EditText editText = (EditText) findViewById(R.id.phonenumber);
        final RadioButton toCCNumber = (RadioButton) findViewById(R.id.countryCodeNumber);

        ArrayAdapter<Country> arrayAdapter = new ArrayAdapter<Country>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, Country.values());
        spinner.setAdapter(arrayAdapter);
        PhoneNumberValidator verifier = new PhoneNumberValidator();
        country = verifier.getUserCountry(MainActivity.this);
        int index = 0;
        if (country != null) {

            for (Country c : Country.values()) {
                if (c == country) {
                    break;
                }
                index++;
            }

            spinner.setSelection(index);


            textView.setText("+" + String.valueOf(country.getCountryCode()));

            Button button = (Button) findViewById(R.id.verify);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = editText.getText().toString();
                    try {
                        PhoneModel phoneModel = country.isNumberValid(number);
                        if (phoneModel.isValidPhoneNumber()) {
                            if (toCCNumber.isChecked()) {
                                number = country.toCountryCode(phoneModel.getPhoneNumber());
                            } else {
                                number = country.toPlainNumber(phoneModel.getPhoneNumber());
                            }
                            outputTextView.setText(number);
                        } else {
                            outputTextView.setText("Not a valid phone number");
                        }
                    } catch (PhoneFormatException e) {
                        outputTextView.setText(e.getMessage());
                    }
                }
            });
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country = Country.values()[i];
                textView.setText("+" + country.getCountryCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
