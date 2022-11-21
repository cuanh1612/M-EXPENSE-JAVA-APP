package vn.edu.greenwich.expensemanagementjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //  The user's current text location is a static variable that can be accessed anywhere
    static String textLocation = "";

    //  For GPS
    //  Location update time, location update distance and code request permission GPS
    protected final int LOCATION_REFRESH_TIME = 15000;
    protected final int LOCATION_REFRESH_DISTANCE = 500;
    protected final int REQUEST_CODE_PERMISSIONS_GPS = 105;

    //  Array contains all necessary permissions to use GPS
    protected final String[] REQUIRED_PERMISSIONS_GPS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //  LocationManager is a class that helps to track the location of the phone
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ket hop bottomnavigation and navhostfragment de có thể chuyển trang khi nhán vào bottom navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonNavigationView);
        // ket hop bottomnavigation and navhostfragment de có thể chuyển trang khi nhán vào bottom navigation

        // xử lý khi chuyển trang thì text trên top bar cx đc thay đổi thành tên của fragment
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                //liệt kê các fragment sẽ không có nút goback trên top bar
                R.id.fragment_Home, R.id.fragment_Add_Trip, R.id.fragment_List_Trips, R.id.fragment_Contact
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // ket hop bottomnavigation and navhostfragment de có thể chuyển trang khi nhán vào bottom navigation

        startGPS();
    }

    //  Check if the GPS permission has been granted by the user or not
    private boolean allPermissionGranted_GPS() {
        for(String permission : REQUIRED_PERMISSIONS_GPS)
            if(     ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    protected void startGPS() {
        /*
        Check GPS permission if you don't have permission,
        the app will ask for permission
        */
        if(!allPermissionGranted_GPS()) {
            /*
                Assign code for this permission action
                Whether the user refuses or accepts it runs down to the
                function onRequestPermissionsResult
            */
            ActivityCompat.requestPermissions(  this,
                                                REQUIRED_PERMISSIONS_GPS,
                                                REQUEST_CODE_PERMISSIONS_GPS);
            return;
        }

        locationListener = getLocationListener();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //  Set time to update each update will run location listener
        locationManager.requestLocationUpdates(     LocationManager.GPS_PROVIDER,
                                                    LOCATION_REFRESH_TIME,
                                                    LOCATION_REFRESH_DISTANCE,
                                                    locationListener);
    }

    protected LocationListener getLocationListener() {
        return new LocationListener() {
            @Override
            /*
                When updating the new location, this function will be
                executed to set info location
            */
            public void onLocationChanged(@NonNull Location location) {
                String error = "Cannot find detailed information";

                String address = error;
                String countryName = error;
                String cityName = error;
                String postalCode = error;

                double latitude = location.getLatitude();
                double longitude = location .getLongitude();

                try {
                    //  Geocoder helps to analyze the surrounding location
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    /*
                        At this coordinate, only the nearest address is taken, returning
                        a list containing that location
                    */
                    List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);

                    /*
                        Check the length of the location array if the length of the array is
                        not less than or equal to 0. If greater than 0 then change
                        the textLocation
                    */
                    if(addresses.size() > 0) {
                        address = addresses.get(0).getAddressLine(0);
                        countryName = addresses.get(0).getCountryName();
                        cityName = addresses.get(0).getLocality();
                        postalCode = addresses.get(0).getPostalCode();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                textLocation =  address
                                + ", "
                                + countryName
                                + ", "
                                + cityName + ", "
                                + postalCode;
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*
            Check if the GPS code is the same to distinguish between codes
            asking for a camera or other permissions.
        */
        if(requestCode == REQUEST_CODE_PERMISSIONS_GPS) {
            /*
                If permission is granted by the user, the user does not need to turn off the device,
                ust execute the GPS function again
            */

            if(allPermissionGranted_GPS()){
                startGPS();
                return;
            }
        }
    }
}