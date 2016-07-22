package com.example.user.simpleui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import java.lang.ref.WeakReference;
import java.util.List;

import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    final static int  ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");  //

        TextView noteTextView = (TextView)findViewById(R.id.noteTextView);
        TextView menuResultsTextView = (TextView)findViewById(R.id.menuResultsTextView);
        TextView storeInfoTextView = (TextView)findViewById(R.id.storeInfoTextView);
        ImageView staticMaoImageView = (ImageView)findViewById(R.id.googleMapImageView);
        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);
        String text = "";
        if (menuResultList != null)
        {
            for (String menuResult:menuResultList )
            {
                text += menuResult+"\n";
            }
        }
        menuResultsTextView.setText(text);

        MapFragment mapFragment=(MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                (new GeoCodingTask(OrderDetailActivity.this)).execute("台北市大安區羅斯福路四段一號");
            }
        });




    }

    @Override
    public void responseWithGeoCodingResults(LatLng latlng) {
        if (googleMap != null)
        {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17);
            googleMap.animateCamera(cameraUpdate);
            //googleMap.moveCamera(cameraUpdate);

            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("台灣大學").snippet("Hello Google Map");
            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    CameraUpdate cp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),21);
                    googleMap.moveCamera(cp);
                    return false;
                }
            });
            //googleMap.moveCamera(cameraUpdate);

            createGoogleAPIClient();
        }

    }
    private  void  createGoogleAPIClient()
    {
        if (googleApiClient == null)
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
            return;
        }

        createLocationRequest();

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        LatLng start = new LatLng(25.0186348, 121.5398379);
        if (location != null)
        {
            start = new LatLng(location.getLatitude(),location.getLongitude());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,17));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ACCESS_FINE_LOCATION_REQUEST_CODE == requestCode)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                onConnected(null);
            }
        }
    }



    @Override
    public void onConnectionSuspended(int i)
    {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }


    private void createLocationRequest()
    {
        if (locationRequest == null)
        {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,17));

        if (marker == null)
        {
            MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title("台灣大學").snippet("Hello Google Map");
            marker = googleMap.addMarker(markerOptions);
        }
        else
        {
            marker.setPosition(currentLatLng);
        }

    }




    /*public static class GeoCodingTask extends AsyncTask<String,Void,Bitmap>
    {
        WeakReference<ImageView> imageViewWeakReference;
        @Override
        protected Bitmap doInBackground(String... params) {
            String address = params[0];
            double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
            return Utils.getStaticMap(latlng);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            if (imageViewWeakReference.get() != null && bitmap != null)
            {
                ImageView imageView = imageViewWeakReference.get();
                imageView.setImageBitmap(bitmap);
            }
        }
        public GeoCodingTask(ImageView imageView)
        {
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

    }*/

}
