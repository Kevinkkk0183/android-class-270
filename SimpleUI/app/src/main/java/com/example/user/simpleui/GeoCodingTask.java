package com.example.user.simpleui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

/**
 * Created by user on 2016/7/21.
 */
public class GeoCodingTask extends AsyncTask<String,Void,LatLng> {
    WeakReference<GeoCodingResponse> geoCodingResponseWeakReference;

    @Override
    protected LatLng doInBackground(String... params) {
        String address = params[0];
        double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
        return new LatLng(latlng[0], latlng[1]);
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        if (geoCodingResponseWeakReference.get() != null)
        {
            GeoCodingResponse geoCodingResponse = geoCodingResponseWeakReference.get();
            geoCodingResponse.responseWithGeoCodingResults(latLng);
        }
    }

    public GeoCodingTask(GeoCodingResponse geoCodingResponse) {
        this.geoCodingResponseWeakReference = new WeakReference<GeoCodingResponse>(geoCodingResponse);
    }
    interface GeoCodingResponse{
        void responseWithGeoCodingResults(LatLng latlng);
    }

}

