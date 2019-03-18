package uqac.ca.isstracker.activities;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import uqac.ca.isstracker.R;
import uqac.ca.isstracker.adapters.InfoWindowMapAdapter;
import uqac.ca.isstracker.model.ISSNow;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    // Activity TAG, in order to find what activity said what in Debug mode
    public static final String TAG = "ACTIVITY - MAP";

    private GoogleMap mMap;

    // Data
    private ISSNow issData;
    private float ISSLat;
    private float ISSLng;

    // Refresh
    private Timer timer;
    private TimerTask refreshData;
    private Handler mTimerHandler = new Handler();

    // Initialising markers to be able to remove them every refresh
    public Marker issMarker = null;

    // Request parameters
    private RequestQueue mRequestQueue;
    private static final String open_notify_url_iss_now = "http://api.open-notify.org/iss-now";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop(){
        super.onStop();

        timer.cancel();
        timer.purge();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        requestAndDataTreatment();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(issMarker))
        {
            // If the marker is clicked, show the info window
            issMarker.showInfoWindow();
        }

        return true;
    }

    public void requestAndDataTreatment(){
        timer = new Timer();

        refreshData = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        addMapInfos();
                    }
                });
            }
        };

        timer.schedule(refreshData,1,5000);
    }

    private void addMapInfos() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, open_notify_url_iss_now,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Getting data
                    issData = new ISSNow(response);

                    // Getting ISSPosition from the JSON Object
                    ISSLng = issData.getIssLongitude();
                    ISSLat = issData.getIssLatitude();

                    // Print the ISS latitude directly from the JSON Object received
                    // (Only for testing purpose) DO NOT DELETE
                    // Toast.makeText(getApplicationContext(), "" + issData.getIssLatitude(), Toast.LENGTH_LONG).show();

                    // Store the ISS position in a variable
                    LatLng ISS = new LatLng(ISSLat, ISSLng);

                    // If no marker exists, create one
                    // Else move the marker to the new position
                    if (issMarker == null) {
                        issMarker = mMap.addMarker(new MarkerOptions()
                                                        .position(ISS)
                                                        .title("ISS position")
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                    } else {
                        issMarker.setPosition(ISS);
                    }

                    InfoWindowMapAdapter customInfoWindow = new InfoWindowMapAdapter(MapActivity.this);
                    mMap.setInfoWindowAdapter(customInfoWindow);

                    String infos = "Lat : " + issData.getIssLatitude()
                            + " Lng : " + issData.getIssLongitude();

                    issMarker.setSnippet(infos);

                    // Center the view on the last position received
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ISS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                //TODO
                // What to do when there is no response ?
            }
        });

        // Access to the RequestQueue
        addToRequestQueue(jsonObjectRequest);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue()
    {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (this.mRequestQueue == null)
        {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
