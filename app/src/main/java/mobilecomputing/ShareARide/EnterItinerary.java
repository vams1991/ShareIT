package mobilecomputing.ShareARide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vams1991 on 10/24/2015.
 */
public class EnterItinerary extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = EnterItinerary.class.getSimpleName();
    public static ArrayList<ParseObject> relatedUsers = new ArrayList<ParseObject>();
    private GoogleApiClient mGoogleApiClient;
    private boolean mAddressRequested = false;
    private AddressResultReceiver mResultReceiver;
    String from;
    String to;
    EditText etFrom;
    EditText etTo;
    EditText etTravelDate;
    ResultsAdapter resultsAdapter;
    LinearLayout llFrom;
    Button btnSS;
    Button btnGps;
    Button btnUpdateProfile;
    Button btnLogout;
    ParseUser currentUser;
    Button btnDirections;
    Button btnRecommendations;
    ProgressBar pbLoading;
    LatLng origin;
    LatLng destination;
    private static int PLACE_PICKER_REQUEST = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enteritinerary);
        mResultReceiver = new AddressResultReceiver(new Handler());
        etFrom = (EditText) findViewById(R.id.et_from);
        etTo = (EditText) findViewById(R.id.et_to);
        etTravelDate = (EditText) findViewById(R.id.et_traveldate);
        btnSS = (Button) findViewById(R.id.btn_SS);
        btnGps = (Button) findViewById(R.id.btn_gps);
        llFrom = (LinearLayout) findViewById(R.id.ll_from);
        btnUpdateProfile = (Button) findViewById(R.id.btn_updateProfile);
        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnDirections = (Button) findViewById(R.id.btndirections);
        btnRecommendations = (Button) findViewById(R.id.btnrecommendations);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        llFrom.setVisibility(View.VISIBLE);
        etTo.setVisibility(View.VISIBLE);
        etFrom.setVisibility(View.VISIBLE);
        etTravelDate.setVisibility(View.VISIBLE);
        btnSS.setVisibility(View.VISIBLE);
        btnGps.setVisibility(View.GONE);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Log.d("", "User is there");
            Log.d("", currentUser.getUsername());
        } else {
            // show the signup or login screen
            Log.d("", "User is not there");
        }
        btnSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etFrom.getText())||TextUtils.isEmpty(etTo.getText())||TextUtils.isEmpty(etTravelDate.getText())) {
                    new AlertDialog.Builder(EnterItinerary.this)
                            .setTitle("Error")
                            .setMessage("DETAILS ARE MISSING")
                            .setNeutralButton(android.R.string.ok, null)
                            .setCancelable(false)
                            .show();
                } else {
                    pbLoading.setVisibility(View.VISIBLE);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Traveldetails");
                    from = etFrom.getText().toString().toUpperCase();
                    from = from.replaceAll("\\s", "");
                    to = etTo.getText().toString().toUpperCase();
                    to = to.replaceAll("\\s", "");
                    query.whereEqualTo("FROM", from);
                    query.whereEqualTo("TO", to);
                    query.whereEqualTo("TRAVEL_DATE", etTravelDate.getText().toString());
                    query.selectKeys(Arrays.asList("USERNAME"));
                    List<ParseObject> userList = null;
                    try {
                        userList = query.find();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pbLoading.setVisibility(View.INVISIBLE);
                    displayResults(userList);

                    ParseObject travelDetails = new ParseObject("Traveldetails");
                    travelDetails.put("USERNAME", currentUser.getUsername());
                    travelDetails.put("FROM", from);
                    travelDetails.put("TO", to);
                    travelDetails.put("TRAVEL_DATE", etTravelDate.getText().toString());
                    travelDetails.saveInBackground();
                }

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

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterItinerary.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                onBackPressed();
            }
        });
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (TextUtils.isEmpty(etFrom.getText()) || TextUtils.isEmpty(etTo.getText())) {
                        new AlertDialog.Builder(EnterItinerary.this)
                                .setTitle("Error")
                                .setMessage("FROM or TO is missing")
                                .setNeutralButton(android.R.string.ok, null)
                                .setCancelable(false)
                                .show();
                    } else {
                        Intent intent = new Intent(EnterItinerary.this, MapsActivity.class);
                        from = etFrom.getText().toString().toUpperCase();
                        from = from.replaceAll("\\s", "");
                        to = etTo.getText().toString().toUpperCase();
                        to = to.replaceAll("\\s", "");
                        intent.putExtra("FROM", from);
                        intent.putExtra("TO", to);
                        startActivity(intent);
                    }

            }
        });

        btnRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //GeocodingLocation locationAddress = new GeocodingLocation();
                    //locationAddress.getAddressFromLocation(etFrom.getText().toString(),
                            //getApplicationContext(), new GeocoderHandlerOrigin());
                    //locationAddress.getAddressFromLocation(etTo.getText().toString(), getApplicationContext(), new GeocoderHandlerDestination());
                    origin = new LatLng(33.425510, -111.940005);
                    destination = new LatLng(33.448377, -112.074037);
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    //LatLngBounds latLngBounds = new LatLngBounds(origin, destination);
                    //builder.setLatLngBounds(latLngBounds);
                    try {
                        Intent intent = builder.build(getApplicationContext());
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == PLACE_PICKER_REQUEST) {
            // This result is from the PlacePicker dialog.

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, getApplicationContext());

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();

                // Print data to debug log
                Log.d(TAG, "Place selected: " + placeId + " (" + name.toString() + ")");


            } else {
                // User has not selected a place, hide the card.

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    private class GeocoderHandlerOrigin extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            JSONObject locationObject = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    Log.d(TAG, locationAddress);
                    String[] latlong =  locationAddress.split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    origin = new LatLng(latitude, longitude);
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    private class GeocoderHandlerDestination extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            JSONObject locationObject = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    Log.d(TAG, locationAddress);
                    String[] latlong =  locationAddress.split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    destination = new LatLng(latitude, longitude);
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, UserLocation.getLocation());
        startService(intent);
    }

    public void displayResults(List<ParseObject> userList){
        //llFrom.setVisibility(View.GONE);
        //etTo.setVisibility(View.GONE);
        //etFrom.setVisibility(View.GONE);
        //etTravelDate.setVisibility(View.GONE);
        //btnSS.setVisibility(View.GONE);
        //btnGps.setVisibility(View.GONE);
        for(int i = 0; i< userList.size(); i++){
            String userName = userList.get(i).getString("USERNAME");
            Log.d(TAG, "USERNAME: "+userName);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", userName);
            /*query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, com.parse.ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Result size: "+objects.size());
                        for(int i=0; i<objects.size(); i++) {
                            ParseObject userObject = objects.get(i);
                            relatedUsers.add(userObject);
                        }
                    } else {
                        Log.d("", "Something went wrong");
                    }
                }
            });*/
            try {
                List<ParseUser> objects = query.find();
                for(int j=0; j<objects.size(); j++) {
                    ParseObject userObject = objects.get(j);
                    relatedUsers.add(userObject);
                }
            } catch (Exception e){

            }
        }
        Log.d(TAG, relatedUsers.toString());
        //if(resultsAdapter == null) resultsAdapter = new ResultsAdapter(getApplicationContext(), relatedUsers);
        //resultsList.setAdapter(resultsAdapter);

        //Log.d(TAG, relatedUsers.toString());
        Intent intent = new Intent(EnterItinerary.this, DisplayResults.class);
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("Related_Users", relatedUsers);
        //intent.putExtras(bundle);
        startActivity(intent);
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
        etFrom.setText(address);
    }
}
