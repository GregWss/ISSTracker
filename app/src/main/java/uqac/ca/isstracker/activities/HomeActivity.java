package uqac.ca.isstracker.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import uqac.ca.isstracker.model.ISSAstros;
import uqac.ca.isstracker.model.ISSNow;
import uqac.ca.isstracker.model.N2yo;
import uqac.ca.isstracker.R;

import static com.android.volley.Request.Method.GET;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static final String TAG = "ACTIVITY - HOME";

    private ISSNow issNowData;
    private ISSAstros issAstrosData;
    private N2yo n2yoData;

    private boolean open_notify_iss_now_received;
    private boolean open_notify_astros_received;
    private boolean n2yo_received;

    private RequestQueue mRequestQueue;

    private TextView textValue;
    private TextView textSentence;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(R.string.title_activity_home);

        textValue = findViewById(R.id.textValue);
        textSentence = findViewById(R.id.textSentence);
        textValue.setText("---");
        textSentence.setText(R.string.api_pending_sentence);

        executeAPIRequests();
    }

    @Override
    public void onStart()
    {
        super.onStart();
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
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
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
        open_notify_astros_received = false;
        open_notify_iss_now_received = false;
        n2yo_received = false;

        final String open_notify_url_iss_now ="http://api.open-notify.org/iss-now";
        final String open_notify_url_astros ="http://api.open-notify.org/astros";
        final String n2yo_url ="https://www.n2yo.com/rest/v1/satellite/positions/25544/48.418844/71.056855/77/1/&apiKey=XSLP3D-VBZHHR-4MHVUR-3YE5";

        // pass "null" for GET requests
        JsonObjectRequest req_iss_now = new JsonObjectRequest(GET, open_notify_url_iss_now, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        open_notify_iss_now_received = true;
                        issNowData = new ISSNow(response);
                        setValues();
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
                        setValues();
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
                        setValues();
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

    public void setValues()
    {
        //Set values only if all APIs have responded
        if(open_notify_iss_now_received && open_notify_astros_received && n2yo_received)
        {
            textValue.setAlpha(0f);
            textSentence.setAlpha(0f);

            Log.d("HOME", "Values ok");
            textSentence.setText(R.string.iss_people);
            textValue.setText("" + issAstrosData.getNumber());

            ObjectAnimator animationTextValueUp = ObjectAnimator.ofFloat(textValue, "translationY", -100f)
                    .setDuration(2000);

            ValueAnimator fadeInTextValueAnim = ObjectAnimator.ofFloat(textValue, "alpha", 0f, 1f)
                    .setDuration(2000);

            ObjectAnimator animationTextSentenceDown = ObjectAnimator.ofFloat(textSentence, "translationY", +100f)
                    .setDuration(2000);

            ValueAnimator fadeInTextSentenceAnim = ObjectAnimator.ofFloat(textSentence, "alpha", 0f, 1f)
                    .setDuration(2000);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animationTextValueUp).with(animationTextSentenceDown);
            animatorSet.play(animationTextValueUp).with(fadeInTextValueAnim);
            animatorSet.play(animationTextValueUp).with(fadeInTextSentenceAnim);
            animatorSet.start();
        }
    }
}
