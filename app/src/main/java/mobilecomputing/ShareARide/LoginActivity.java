package mobilecomputing.ShareARide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by vams1991 on 10/11/2015.
 */
public class LoginActivity extends Activity {

    EditText etUserName;
    EditText etPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, EnterItinerary.class);
            startActivity(intent);
        }
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        Button newUser = (Button)findViewById(R.id.btn_newuser);
        etUserName = (EditText) findViewById(R.id.et_uname);
        etPassword = (EditText) findViewById(R.id.et_password);
        UserLocation.getLocationUpdates();
        Location location = UserLocation.getLocation();
//        Log.d("", "Latitude: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        UserLocation.stopLocationUpdates();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPassword.getText())||TextUtils.isEmpty(etUserName.getText())) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("LOGIN ERROR")
                            .setMessage("Incomplete credentials")
                            .setNeutralButton(android.R.string.ok, null)
                            .setCancelable(false)
                            .show();
                } else {
                    ParseUser.logInInBackground(etUserName.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, com.parse.ParseException e) {
                            if (user != null) {
                                //Login successful
                                Intent intent = new Intent(getApplicationContext(), EnterItinerary.class);
                                startActivity(intent);
                            } else {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("LOGIN ERROR")
                                        .setMessage("Invalid credentials")
                                        .setNeutralButton(android.R.string.ok, null)
                                        .setCancelable(false)
                                        .show();
                            }
                        }
                    });
                }

            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
