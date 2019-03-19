package uqac.ca.isstracker.activities;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uqac.ca.isstracker.model.Data;
import uqac.ca.isstracker.model.ISSAstros;
import uqac.ca.isstracker.model.ISSNow;
import uqac.ca.isstracker.model.N2yo;
import uqac.ca.isstracker.R;

import static com.android.volley.Request.Method.GET;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{
    public static final String TAG = "ACTIVITY - HOME";

    public ISSNow issNowData;
    public ISSAstros issAstrosData;
    public N2yo n2yoData;
    public Data data;
    private int dataIndex;
    private int backgroundIndex;
    private ArrayList<Integer> backgroundImages;

    private Timer timer;

    private boolean initialized;

    private LocationManager locationManager;

    private FusedLocationProviderClient fusedLocationClient;

    private static final String open_notify_url_iss_now = "http://api.open-notify.org/iss-now";
    private static final String open_notify_url_astros = "http://api.open-notify.org/astros";
    //https://www.n2yo.com/rest/v1/satellite/positions/25544/48.418844/71.056855/77/1/&apiKey=XSLP3D-VBZHHR-4MHVUR-3YE5
    private static final String n2yo_url = "https://www.n2yo.com/rest/v1/satellite/positions/25544/";
    private String n2yo_seconds = "1";
    private static final String n2yo_apikey = "XSLP3D-VBZHHR-4MHVUR-3YE5";

    private boolean open_notify_iss_now_received;
    private boolean open_notify_astros_received;
    private boolean n2yo_received;

    private RequestQueue mRequestQueue;

    private ImageButton btnAbout;
    private TextView textValue;
    private TextView textSentence;
    private ImageView backgroundView;

    private AnimatorSet animatorSetIn;
    private AnimatorSet animatorSetOut;
    private ObjectAnimator animationTextValueDown;
    private ObjectAnimator animationTextValueUp;
    private ValueAnimator fadeInTextValueAnim;
    private ValueAnimator fadeOutTextValueAnim;
    private ObjectAnimator animationTextSentenceDown;
    private ObjectAnimator animationTextSentenceUp;
    private ValueAnimator fadeInTextSentenceAnim;
    private ValueAnimator fadeOutTextSentenceAnim;

    final private int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Construct view
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.homeToolbar);
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
        this.initialized            = false;
        this.fusedLocationClient    = LocationServices.getFusedLocationProviderClient(this);
        this.dataIndex              = 0;
        this.backgroundIndex        = 0;
        this.backgroundImages       = new ArrayList<>();
        this.btnAbout               = findViewById(R.id.aboutButton);
        this.textValue              = findViewById(R.id.textValue);
        this.textSentence           = findViewById(R.id.textSentence);
        this.backgroundView         = findViewById(R.id.homeBackgroundView);
        this.locationManager        = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.animatorSetIn          = new AnimatorSet();
        this.animatorSetOut         = new AnimatorSet();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                //Do nothing
            }
        });
        builder.setTitle(R.string.btn_about_title);
        builder.setMessage(R.string.btn_about_content);

        final AlertDialog dialog = builder.create();

        this.btnAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.show();
            }
        });
        this.textValue.setText("");
        this.textSentence.setText("");



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

        this.backgroundImages.add(R.drawable.unsplash_nasa_43567);
        this.backgroundImages.add(R.drawable.unsplash_nasa_43568);
        this.backgroundImages.add(R.drawable.unsplash_nasa_63029);
        this.backgroundImages.add(R.drawable.unsplash_niketh_vellanki_252581);
        this.backgroundImages.add(R.drawable.unsplash_richard_gatley_533872);


    }

    @Override
    public void onStart()
    {
        super.onStart();

        init();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
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
        getMenuInflater().inflate(R.menu.activity_home_action_bar_menu, menu);
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

        if (id == R.id.action_refresh)
        {
            this.initialized = false;
            init();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch(item.getItemId())
        {
            case R.id.nav_map:
                Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapIntent);
                break;

            case R.id.nav_dashboard:
                Intent dashboardIntent = new Intent(getApplicationContext(), HumansActivity.class);
                startActivity(dashboardIntent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Initiates views, checks permissions and execute data gathering
     */
    private void init()
    {
        if(initialized) //Used to init only once
            return;

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }

        this.timer = new Timer();

        //Init views

        if(this.animatorSetIn.isRunning() && this.animatorSetOut.isStarted())  /// MANDATORY TEST
        {
            this.animatorSetIn.end();
            this.animatorSetOut.cancel();
        }
        else if(this.animatorSetOut.isRunning())
        {
            this.animatorSetOut.end();
            this.textSentence.setTranslationY(-100f);
            this.textSentence.setAlpha(1f);
            this.textValue.setTranslationY(+100f);
            this.textValue.setAlpha(1f);
        }

        Glide.with(getApplicationContext())
                .clear(backgroundView);
        Glide.with(getApplicationContext())
                .load(R.color.colorPrimary)
                .transition(withCrossFade())
                .into(backgroundView);

        //Check permissions

        boolean perm1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean perm2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(perm1 && perm2)
        {
            //Check if location service is enabled on user's device
            if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                promptEnableLocation();
            }
            else
            {
                this.textValue.setText("---");
                this.textSentence.setText(R.string.location_pending_sentence);

                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                        {
                            textSentence.setText(R.string.api_pending_sentence);
                            executeAPIRequests(location);
                            initialized = true;
                        }
                        else
                        {
                            textSentence.setText(R.string.location_error);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        textValue.setText("---");
                        textSentence.setText(R.string.location_error);
                    }
                });
            }
        }
        else
        {
            promptPermissions();
        }
    }

    /**
     * Lazy initialize the request queue, the queue instance will be created when it is accessed
     * for the first time
     * @return Request Queue
     */
    public RequestQueue getRequestQueue()
    {
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

    /**
     * Executes API requests and begins UI updates when all APIs have responded
     */
    public void executeAPIRequests(Location location)
    {
        this.open_notify_astros_received = false;
        this.open_notify_iss_now_received = false;
        this.n2yo_received = false;

        final String builder_n2yo_url = n2yo_url +
                location.getLatitude() + "/" +
                location.getLongitude() + "/" +
                location.getAltitude() + "/" +
                n2yo_seconds + "/" +
                "&apiKey=" + n2yo_apikey;

        // pass "null" for GET requests
        JsonObjectRequest req_iss_now = new JsonObjectRequest(GET, open_notify_url_iss_now, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        open_notify_iss_now_received = true;
                        issNowData = new ISSNow(response);
                        startUIUpdates();
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
                        startUIUpdates();
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

        JsonObjectRequest req_n2yo = new JsonObjectRequest(GET, builder_n2yo_url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        n2yo_received = true;
                        n2yoData = new N2yo(response);
                        startUIUpdates();
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

    /**
     * Begins UI updates if every API have responded
     */
    private void startUIUpdates()
    {
        //Set values only if all APIs have responded
        if(this.open_notify_iss_now_received && this.open_notify_astros_received && this.n2yo_received)
        {
            this.data = new Data(getApplicationContext(), null, issAstrosData, n2yoData);

            this.timer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    runOnUiThread(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            setValuesAndAnimate();
                        }
                    });
                }
            }, 0, 7000);
        }
    }

    /**
     * Executes the UI animation process (showing values and animations)
     */
    private void setValuesAndAnimate()
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
            if(backgroundIndex >= backgroundImages.size())
                backgroundIndex = 0;

            this.animatorSetIn.play(this.animationTextValueDown)
                    .with(this.animationTextSentenceDown)
                    .with(this.fadeInTextValueAnim)
                    .with(this.fadeInTextSentenceAnim);
            this.animatorSetIn.start();

            Glide.with(getApplicationContext())
                    .load(backgroundImages.get(backgroundIndex++))
                    .transition(withCrossFade())
                    .into(backgroundView);

            this.animatorSetOut.setStartDelay(4900);
            this.animatorSetOut.play(this.animationTextValueUp)
                    .with(this.animationTextSentenceUp)
                    .with(this.fadeOutTextValueAnim)
                    .with(this.fadeOutTextSentenceAnim);
            this.animatorSetOut.start();
        }
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
                    init();
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
