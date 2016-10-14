package mobilecomputing.ShareARide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

/**
 * Created by vams1991 on 11/14/2015.
 */
public class UpdateProfileActivity extends Activity{
    ParseUser currentUser;
    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etAddress;
    EditText etPhone;
    Button btnSave;
    Button btnGps;
    private AddressResultReceiver mResultReceiver;
    private boolean mAddressRequested = false;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        currentUser = ParseUser.getCurrentUser();
        mResultReceiver = new AddressResultReceiver(new Handler());
        etName = (EditText)findViewById(R.id.et_name);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPassword = (EditText)findViewById(R.id.et_password);
        etAddress = (EditText)findViewById(R.id.et_address);
        etPhone = (EditText)findViewById(R.id.et_phone);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnGps = (Button)findViewById(R.id.btn_gps);
        etName.setHint(currentUser.getUsername());
        etEmail.setHint(currentUser.getEmail());
        etAddress.setHint(currentUser.getString("Address"));
        etPhone.setHint(currentUser.getString("Phone"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setUsername(etName.getText().toString());
                currentUser.setEmail(etEmail.getText().toString());
                currentUser.setPassword(etPassword.getText().toString());
                currentUser.put("Address", etAddress.getText().toString());
                currentUser.put("Phone", etPhone.getText().toString());
                onBackPressed();
                currentUser.saveInBackground();
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserLocation.getLocation() != null) {
                    startIntentService();
                }
                mAddressRequested = true;
            }
        });

    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, UserLocation.getLocation());
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                displayAddressOutput(mAddressOutput);
            } else {
                //Address not recieved
            }

        }
    }

    public void displayAddressOutput(String address){
        etAddress.setText(address);
    }

}
