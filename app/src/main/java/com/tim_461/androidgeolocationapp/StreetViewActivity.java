package com.tim_461.androidgeolocationapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;


public class StreetViewActivity extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {
    private static final String TAG = StreetViewActivity.class.getSimpleName();
    public StreetViewPanoramaFragment streetViewPanoramaFragment;
    public LatLng pos;
    public double lat;
    public double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);

        Bundle b = getIntent().getExtras();
        lat = b.getDouble("lat");
        lon = b.getDouble("lon");

        Log.d(TAG, "Lat="+lat+", lon="+lon);

        this.streetViewPanoramaFragment =
                (StreetViewPanoramaFragment)
                        getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        this.streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_street_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(lat, lon));
    }
}
