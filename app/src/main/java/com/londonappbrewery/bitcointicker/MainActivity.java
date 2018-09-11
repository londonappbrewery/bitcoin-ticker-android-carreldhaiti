package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    // Complete url = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTCUSD"
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                // An item was selected. You can retrieve it using adapterView.getItemAtPosition( pos )
                Log.d( "Bitcoin", "Dropdown value: " + ( adapterView.getItemAtPosition(position) ) );

                String strURL = BASE_URL + adapterView.getItemAtPosition( position );

                // Invoke the private method letsDoSomeNetworking(String url)
                letsDoSomeNetworking( strURL );

            }   // end of onItemSelected() method

            @Override
            public void onNothingSelected( AdapterView<?> parent ) {

                Log.d( "Bitcoin", "Nothing Selected" );

            }   // end of method onNothingSelected()

        }); // end of method setOnItemSelectedListener()

    }   // end of method onCreate()

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // On success, the response HTTP status is "200 OK"
                Log.d( "Bitcoin", "status code: " + statusCode );

                // Show the JSON string from the Bitcoin site
                Log.d("Bitcoin", "JSON: " + response.toString());

                // Extract the requested information from the JSON response
                try {

                    // extract ask price from JSON
                    String strAskPriceFromJson = response.getString( "ask" );
                    Log.d("Bitcoin", "JSON ask price: " + strAskPriceFromJson );

                    // extract bid price from JSON
                    String strBidPriceFromJson = response.getString( "bid" );
                    Log.d("Bitcoin", "JSON bid price: " + strBidPriceFromJson );

                    // extract last price from JSON
                    String strLastPriceFromJson = response.getString( "last" );
                    Log.d("Bitcoin", "JSON last price: " + strLastPriceFromJson );

                    // extract day average price from JSON
                    String strDayAvgPriceFromJson = ( Double.toString( response.getJSONObject("averages").getDouble("day" ) ) );
                    Log.d("Bitcoin", "JSON day average price: " + strDayAvgPriceFromJson );

                    // Update the mPriceTextView with the last Bitcoin price information from the JSON
                    mPriceTextView.setText( strLastPriceFromJson );

                } catch( JSONException e ) {

                    Log.d("Bitcoin", "JSON error in extracting the Bitcoin price: " + e.toString() ) ;

                }   // end of try-catch block

            }   // end of method onSuccess()

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());

            }   // end of method onFailure()

        }); // end of method get()

    }   // end of method letsDoSomeNetworking()


}   // end of class MainActivity
