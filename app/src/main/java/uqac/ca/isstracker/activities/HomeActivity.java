package uqac.ca.isstracker.activities;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import uqac.ca.isstracker.handlers.HomeSideHandler;
import uqac.ca.isstracker.model.Data;
import uqac.ca.isstracker.model.ISSAstros;
import uqac.ca.isstracker.model.ISSNow;
import uqac.ca.isstracker.model.N2yo;
import uqac.ca.isstracker.R;
import uqac.ca.isstracker.threads.HomeSideThread;

import static com.android.volley.Request.Method.GET;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{
    public static final String TAG = "ACTIVITY - HOME";

    public ISSNow issNowData;
    public ISSAstros issAstrosData;
    public N2yo n2yoData;
    public Data data;
    private int dataIndex;

    private HomeSideThread sideThread;
    private HomeSideHandler sideHandler;

    private LocationManager locationManager;
    private Location currLocation;
    private boolean gps_state;

    private static final String open_notify_url_iss_now = "http://api.open-notify.org/iss-now";
    private static final String open_notify_url_astros = "http://api.open-notify.org/astros";
    private static final String n2yo_url = "https://www.n2yo.com/rest/v1/satellite/positions/25544/48.418844/71.056855/77/1/&apiKey=XSLP3D-VBZHHR-4MHVUR-3YE5";

    private boolean open_notify_iss_now_received;
    private boolean open_notify_astros_received;
    private boolean n2yo_received;

    private RequestQueue mRequestQueue;

    private TextView textValue;
    private TextView textSentence;

    private ObjectAnimator animationTextValueDown;
    private ObjectAnimator animationTextValueUp;
    private ValueAnimator fadeInTextValueAnim;
    private ValueAnimator fadeOutTextValueAnim;
    private ObjectAnimator animationTextSentenceDown;
    private ObjectAnimator animationTextSentenceUp;
    private ValueAnimator fadeInTextSentenceAnim;
    private ValueAnimator fadeOutTextSentenceAnim;

    final private int PERMISSIONS_REQUEST = 1;

    //Location listener, searching for a GPS signal and enabling acquisition when it's found.
    private final LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            //Set gps state
            setGPSState(location);
            currLocation = location;
            Log.d(TAG, location.toString());
        }

        public void onProviderDisabled(String Provider)
        {
            setGPSState(false);
            currLocation = null;
        }

        public void onProviderEnabled(String provider)
        {
            Toast.makeText(HomeActivity.this, "PROVIDER : " + provider, Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Construct view
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(R.string.title_activity_home);

        //Init values
        this.dataIndex          = 0;
        this.textValue          = findViewById(R.id.textValue);
        this.textSentence       = findViewById(R.id.textSentence);
        this.sideHandler        = new HomeSideHandler(this);
        this.sideThread         = new HomeSideThread(getApplicationContext(), sideHandler);
        this.locationManager    = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.textValue.setText("---");
        this.textSentence.setText(R.string.api_pending_sentence);

        this.animationTextValueDown     = ObjectAnimator.ofFloat(textValue, "translationY", -100f)
                .setDuration(2000);

        this.animationTextValueUp       = ObjectAnimator.ofFloat(textValue, "translationY", 100f)
                .setDuration(2000);

        this.fadeInTextValueAnim        = ObjectAnimator.ofFloat(textValue, "alpha", 0f, 1f)
                .setDuration(2000);

        this.fadeOutTextValueAnim       = ObjectAnimator.ofFloat(textValue, "alpha", 1f, 0f)
                .setDuration(2000);

        this.animationTextSentenceDown  = ObjectAnimator.ofFloat(textSentence, "translationY", +100f)
                .setDuration(2000);

        this.animationTextSentenceUp    = ObjectAnimator.ofFloat(textSentence, "translationY", -100f)
                .setDuration(2000);

        this.fadeInTextSentenceAnim     = ObjectAnimator.ofFloat(textSentence, "alpha", 0f, 1f)
                .setDuration(2000);

        this.fadeOutTextSentenceAnim    = ObjectAnimator.ofFloat(textSentence, "alpha", 1f, 0f)
                .setDuration(2000);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        beginDataAcquisition();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch(item.getItemId())
        {
            case R.id.nav_humans:
                break;

            case R.id.nav_map:
                break;

            case R.id.nav_dashboard:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }

    public void executeAPIRequests()
    {
        this.open_notify_astros_received = false;
        this.open_notify_iss_now_received = false;
        this.n2yo_received = false;

        // pass "null" for GET requests
        JsonObjectRequest req_iss_now = new JsonObjectRequest(GET, open_notify_url_iss_now, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        open_notify_iss_now_received = true;
                        issNowData = new ISSNow(response);
                        startSideThread();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, error.getMessage());
                    }
                }
        );
        addToRequestQueue(req_iss_now);

        JsonObjectRequest req_iss_astros = new JsonObjectRequest(GET, open_notify_url_astros, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        open_notify_astros_received = true;
                        issAstrosData = new ISSAstros(response);
                        startSideThread();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, error.getMessage());
                    }
                }
        );
        addToRequestQueue(req_iss_astros);

        JsonObjectRequest req_n2yo = new JsonObjectRequest(GET, n2yo_url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        n2yo_received = true;
                        n2yoData = new N2yo(response);
                        startSideThread();
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, error.getMessage());
                    }
                }
        );
        addToRequestQueue(req_n2yo);
    }

    public void startSideThread()
    {
        //Set values only if all APIs have responded
        if(this.open_notify_iss_now_received && this.open_notify_astros_received && this.n2yo_received)
        {
            data = new Data(getApplicationContext(), null, issAstrosData, null);
            if(!sideThread.isAlive()) sideThread.start();
        }
    }

    public void setValuesAndAnimate()
    {
        if(this.open_notify_iss_now_received && this.open_notify_astros_received && this.n2yo_received)
        {
            this.textSentence.setAlpha(0f);
            this.textValue.setAlpha(0f);

            this.textSentence.setText(data.getData(dataIndex)[0]);
            this.textValue.setText(data.getData(dataIndex)[1]);
            dataIndex++;
            if(dataIndex >= data.size())
                dataIndex = 0;

            AnimatorSet animatorSetIn = new AnimatorSet();
            animatorSetIn.play(this.animationTextValueDown).with(this.animationTextSentenceDown);
            animatorSetIn.play(this.animationTextValueDown).with(this.fadeInTextValueAnim);
            animatorSetIn.play(this.animationTextValueDown).with(this.fadeInTextSentenceAnim);
            animatorSetIn.start();

            AnimatorSet animatorSetOut = new AnimatorSet();
            animatorSetOut.setStartDelay(5000);
            animatorSetOut.play(this.animationTextValueUp).with(this.animationTextSentenceUp);
            animatorSetOut.play(this.animationTextValueUp).with(this.fadeOutTextValueAnim);
            animatorSetOut.play(this.animationTextValueUp).with(this.fadeOutTextSentenceAnim);
            animatorSetOut.start();
        }
    }

    private void beginDataAcquisition()
    {
        boolean perm1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean perm2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(perm1 && perm2)
        {
            //Check if location service is enabled on user's device
            if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) //That seems to don't work anymore
            {
                promptEnableLocation();
            }
            else
            {
                startLocationUpdates();
                executeAPIRequests();
            }
        }
        else
        {
            promptPermissions();
        }
    }

    /**
     * Begins GPS location reading
     * The result is sent to the location listener
     */
    private void startLocationUpdates()
    {
        try
        {
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this.locationListener);
        }
        catch (SecurityException se)
        {
            //Permission not granted
            Log.e("LOCATION REQUEST", se.getMessage());
        }
    }

    /**
     * Sets GPS connexion state depending on the location he's receiving.
     * @param location The location received by the GPS.
     */
    public void setGPSState(Location location)
    {
        this.gps_state = location != null;
    }

    /**
     * Sets GPS connexion state.
     * @param newState The new state to apply.
     */
    public void setGPSState(Boolean newState)
    {
        this.gps_state = newState;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST:
                // If request is cancelled, the result array is empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Execute requests
                    Log.e(TAG, "PERMISSIONS GRANTED");
                    beginDataAcquisition();
                }
                else
                {

                }
                break;
        }
    }

    /**
     *  Prompts user to ask for required permissions.
     *  If the localisation perm is not granted, the GPS acquisition is disabled.
     */
    private void promptPermissions()
    {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                PERMISSIONS_REQUEST
        );
    }

    private void promptEnableLocation()
    {
        AlertDialog.Builder alertPromptEnableGPS = new AlertDialog.Builder(this);

        alertPromptEnableGPS.setMessage("Help us determine your location. That means sending " +
                "anonymous location data to us only when he app is running.")
                .setTitle("Use location service?")
                .setCancelable(true)
                .setPositiveButton("Agree", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //Intent on location source system settings
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });

        alertPromptEnableGPS.setNegativeButton("Disagree", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });

        AlertDialog alertActivateLocation = alertPromptEnableGPS.create();
        alertActivateLocation.show();
    }
}