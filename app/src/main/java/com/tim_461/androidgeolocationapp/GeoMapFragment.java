package com.tim_461.androidgeolocationapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeoMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeoMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeoMapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MainActivity activity;
    private Context context;
    private static final String TAG = GeoMapFragment.class.getSimpleName();
    private static String geoLocAPIKey = "AIzaSyAHO_QZPWnb7OiZ5Vo_QOGw_qRlYCWR368";

    private MapFragment mapFrag;
    private boolean isMapInit;
    private FrameLayout mapContain;
    private LatLng mylatLng;
    private LatLng currentlatLng;

    private Button mapNormalButton;
    private Button mapHybridButton;
    private Button mapTerrainButton;
    private Button toggleStreetButton;

    private EditText locationText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeoMapFragment newInstance(String param1, String param2) {
        GeoMapFragment fragment = new GeoMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GeoMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.context = this.getActivity();
        this.activity = ((MainActivity) activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map, container, false);

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void initFragment() {
        Log.d(TAG, "Init Fragment");

        View view = getView();
        if(view == null){
            // Shouldn't happen
            return;
        }

        // Initialize Layout params
        mapFrag = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.fl_mapView));
        mapNormalButton = (Button) view.findViewById(R.id.b_mapNormal);
        mapHybridButton = (Button) view.findViewById(R.id.b_mapHybrid);
        mapTerrainButton = (Button) view.findViewById(R.id.b_mapTerrain);
        locationText = (EditText) view.findViewById(R.id.et_locationText);

        toggleStreetButton = (Button) view.findViewById(R.id.b_streetView);


        // Init params
        currentlatLng = new LatLng(30.2794888408268,-97.7425112453448);
        GoogleMap googleMap = mapFrag.getMap();


        // Init google map
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            setUpMap(mapFrag.getMap());
            Log.d(TAG, "MAP SETUP FINISHED");
        }else{
            Log.d(TAG, "MAP Null");
        }

        // Listeners
        mapNormalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMap googleMap = mapFrag.getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(40.74844, -73.985664))
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setBuildingsEnabled(true);
                currentlatLng = new LatLng(40.74844, -73.985664);
            }
        });
        mapHybridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMap googleMap = mapFrag.getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        mapTerrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMap googleMap = mapFrag.getMap();
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
        locationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i(TAG,"Enter pressed");
                    submitLocation(v);
                }
                return false;
            }
        });
        toggleStreetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StreetViewActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", currentlatLng.latitude);
                b.putDouble("lon", currentlatLng.longitude);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void submitLocation(View v){
        String locTxt = locationText.getText().toString();
        Log.d(TAG, "Submitting Location:"+locTxt);
        String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String postURL = baseURL;


        // If size if 0, null, or default then error
        if(locTxt == null || locTxt.length() == 0 || locTxt.equals(getString(R.string.enter_location))){
            Toast.makeText(activity, "Please Enter a Location!", Toast.LENGTH_LONG).show();
            return;
        }

        // Parse string into tokens
        StringTokenizer st = new StringTokenizer(locTxt, " ");
        // Parse 1st token
        if(st.hasMoreTokens()) {
            String token = st.nextToken();
            postURL = postURL.concat(token);
        }
        // Parse remaining tokens
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            postURL = postURL.concat("+"+token);
        }

        // Add API Key
        postURL = postURL.concat("&key="+geoLocAPIKey);
        Log.d(TAG, postURL);

        // Post to server
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GeoLocationPost().execute(postURL);
        } else {
            Toast.makeText(activity, "No network connection available.", Toast.LENGTH_LONG).show();
        }

    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class GeoLocationPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return Internet.getFromURL(urls[0]);
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            parseGeoCodeResults(result);
        }
    }

    private void parseGeoCodeResults(String result){
        Log.d(TAG, "Results:"+result);
        try {
            JSONObject resultObj = new JSONObject(result);
            if (resultObj.has("error")) {

            } else {
                JSONArray results = resultObj.getJSONArray("results");
                JSONObject firstObj = results.getJSONObject(0);
                JSONObject geoMet = firstObj.getJSONObject("geometry").getJSONObject("location");
                String formattedAddr = firstObj.getString("formatted_address");
                Double lat = geoMet.getDouble("lat");
                Double lon = geoMet.getDouble("lng");
                Log.d(TAG, "Lat:"+lat+", lon:"+lon);
                addPinToMap(lat, lon, formattedAddr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }

    private void addPinToMap(Double lat, Double lon, String formattedAddr){
        GoogleMap googleMap = mapFrag.getMap();
        LatLng nMarker = new LatLng(lat, lon);
        // Set street view current lat long
        this.currentlatLng = nMarker;
        MarkerOptions options = new MarkerOptions();
        Marker marker = googleMap.addMarker(options.position(nMarker).title(formattedAddr));

        // Show the current location in Google Map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(nMarker)
                .zoom(14)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setBuildingsEnabled(false);
        calculateDistanceFromCurrentPosition(nMarker);
    }

    private void calculateDistanceFromCurrentPosition(LatLng pos) {
        double currentLat = this.mylatLng.latitude;
        double currentLon = this.mylatLng.longitude;

        double positionLat = pos.latitude;
        double positionLon = pos.longitude;

        double R = 6372.8;
        double dLat = Math.toRadians(positionLat - currentLat);
        double dLon = Math.toRadians(positionLon - currentLon);
        currentLat = Math.toRadians(currentLat);
        positionLat = Math.toRadians(positionLat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(currentLat) * Math.cos(positionLat);
        double c = 2 * Math.asin(Math.sqrt(a));

        Toast.makeText(activity, "Distance From You = "+round(R * c, 3)+" km", Toast.LENGTH_LONG).show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Sets up google maps with deals and user location
     *
     * @param googleMap - map_active to be initialized
     */

    private void setUpMap(GoogleMap googleMap) {
        Double DEFAULT_LATITUDE = 30.2794888408268;
        Double DEFAULT_LONGITUDE = -97.7425112453448;
        // Enable MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = provider != null && !provider.isEmpty() ? locationManager.getLastKnownLocation(provider) : null;
        if (myLocation == null) {
            myLocation = new Location("DefaultLocation");
            myLocation.setLatitude(DEFAULT_LATITUDE);
            myLocation.setLongitude(DEFAULT_LONGITUDE);
        }

        //set map_active type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        mylatLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
        currentlatLng = mylatLng;

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        MarkerOptions options = new MarkerOptions();


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
