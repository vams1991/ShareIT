package mobilecomputing.ShareARide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by vams1991 on 10/11/2015.
 */
public class SignUpActivity extends Activity {

    Button btnGps;
    private boolean mAddressRequested = false;
    private AddressResultReceiver mResultReceiver;
    EditText etAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btnSignup = (Button)findViewById(R.id.btn_signup);
        mResultReceiver = new AddressResultReceiver(new Handler());
        final EditText etName = (EditText) findViewById(R.id.et_name);
        final EditText etEmail = (EditText) findViewById(R.id.et_email);
        final EditText etPhone = (EditText) findViewById(R.id.et_phone);
        etAddress = (EditText) findViewById(R.id.et_address);
        final EditText etPassword = (EditText) findViewById(R.id.et_password);
        final Button btnExisting = (Button) findViewById(R.id.btn_existing);
        btnGps = (Button) findViewById(R.id.btn_gps);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etEmail.getText())||TextUtils.isEmpty(etPassword.getText())||TextUtils.isEmpty(etName.getText())||TextUtils.isEmpty(etPhone.getText())||TextUtils.isEmpty(etAddress.getText())){
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("SIGN UP ERROR")
                            .setMessage("Details Missing")
                            .setNeutralButton(android.R.string.ok, null)
                            .setCancelable(false)
                            .show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setEmail(etEmail.getText().toString());
                    user.setPassword(etPassword.getText().toString());
                    user.setUsername(etName.getText().toString());
                    user.put("Phone", etPhone.getText().toString());
                    user.put("Address", etAddress.getText().toString());

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //Sign Up successful
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                //Sign Up unsuccessful
                            }
                        }
                    });
                }
            }
        });
        btnExisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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
