package mobilecomputing.ShareARide;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by vams1991 on 10/25/2015.
 */
public final class UserLocation {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static Context mContext;
    private static Location actualLocation;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static final void getLocationUpdates (){
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        actualLocation = getLastBestLocation();
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if((isBetterLocation(location, actualLocation))){
                    actualLocation = location;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public static final void stopLocationUpdates(){
        locationManager.removeUpdates(locationListener);
    }

    public static final Location getLocation(){
        return actualLocation;
    }

    public static double getLatitude(){
        double lat = 0.00;

        if (actualLocation != null) {
            lat = actualLocation.getLatitude();
        } else {
            // Set to Atticue Office latitude coordinate
            lat = 33.42147;
        }
        return lat;
    }

    public static double getLongitude(){
        double lng = 0.00;

        if (actualLocation != null) {
            lng = actualLocation.getLongitude();
        } else {
            // Set to Atticue's Office longitude coordinate
            lng = -111.97109;
        }
        return lng;
    }

    private static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private static Location getLastBestLocation() {
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

}

