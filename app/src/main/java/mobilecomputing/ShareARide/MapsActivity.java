package mobilecomputing.ShareARide;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String TAG = MapsActivity.class.getSimpleName();
    GoogleMap MAP;
    LatLng origin;
    LatLng destination;
    String originPlace;
    String destinationPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        originPlace = getIntent().getStringExtra("FROM");
        destinationPlace = getIntent().getStringExtra("TO");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GeocodingLocation locationAddress = new GeocodingLocation();
        Log.d(TAG, "origin place: "+originPlace);
        locationAddress.getAddressFromLocation(originPlace,
                getApplicationContext(), new GeocoderHandler());

        //origin = getLocationFromAddress(originPlace);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            JSONObject locationObject = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    Log.d(TAG, "locationAddress: "+locationAddress);
                    if(locationAddress.equals("Error")){
                        onBackPressed();
                    } else {
                        String[] latlong = locationAddress.split(",");
                        double latitude = Double.parseDouble(latlong[0]);
                        double longitude = Double.parseDouble(latlong[1]);
                        origin = new LatLng(latitude, longitude);
                    }
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        MAP = map;
        // Add a marker in Sydney, Australia, and move the camera.
        //LatLng sydney = new LatLng(-34, 151);
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //origin = new LatLng(33.425510, -111.940005);
        //destination = new LatLng(33.448377, -112.074037);
        /*PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(33.425510, -111.940005))// Same latitude, and 30km to the west
                .add(new LatLng(33.448377, -112.074037))  // Same longitude, and 16km to the south
                .width(5).color(Color.BLUE); // Closes the polyline.

// Get back the mutable Polyline
        map.addPolyline(rectOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(place));*/
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +originPlace+ "&destination=" + destinationPlace+ "&key=AIzaSyC1tF1uIqQd979g2t-5hAspufaPaISiGW8";
        new RequestTask(new RouteDetailsCallback()).execute(url);
    }

    /*public LatLng getLocationFromAddress(String place) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(place,5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }*/

    private class RouteDetailsCallback implements RequestCallBack {
        @Override
        public void onCallback(Object object) {
            if (object instanceof String) {
                String response = (String) object;
                JSONObject responseObject = null;
                try {
                    responseObject = new JSONObject(response);
                    JSONArray routes = responseObject.getJSONArray("routes");
                    JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                            .getJSONObject(0).getJSONArray("steps");
                    Log.d(TAG, steps.toString());
                    List<LatLng> lines = new ArrayList<LatLng>();

                    for(int i=0; i < steps.length(); i++) {
                        String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

                        for(LatLng p : decodePolyline(polyline)) {
                            lines.add(p);
                        }
                    }
                    Polyline polylineToAdd = MAP.addPolyline(new PolylineOptions().addAll(lines).width(10).color(Color.RED));
                    MAP.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 13));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        private List<LatLng> decodePolyline(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();

            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
                poly.add(p);
            }

            return poly;
        }
    }
}