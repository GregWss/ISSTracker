package uqac.ca.isstracker.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import uqac.ca.isstracker.R;
import uqac.ca.isstracker.model.N2yoPass;
import uqac.ca.isstracker.model.N2yoVisualPasses;
import uqac.ca.isstracker.other.Utils;

import static com.android.volley.Request.Method.GET;

public class PassesActivity extends AppCompatActivity
{
    public static final String TAG = "ACTIVITY - PASSES";

    private RequestQueue mRequestQueue;
    private static String n2yo_visual_passes_url;
    private static int n2yo_visual_passes_days = 2;
    private static int n2yo_visual_passes_min_visibility = 200;
    private static String n2yo_apikey;

    //Values from api's example if no pass is found at user location
    private static final double fakeLatitude = 48.418844;
    private static final double fakeLongitude = 71.056855;
    private static final double fakeAltitude = 77;

    private static N2yoVisualPasses n2yoVisualPasses;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passes);

        Toolbar toolbar = findViewById(R.id.passesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //API string values
        n2yo_visual_passes_url = getResources().getString(R.string.N2YO_API_HEAD);
        n2yo_apikey = getResources().getString(R.string.N2YO_API_KEY);

        //Get preferences values
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        n2yo_visual_passes_days = sharedPreferences.getInt("days_scope", 2);
        n2yo_visual_passes_min_visibility = sharedPreferences.getInt("min_visibility", 200);

        //N2yo visual passes API request
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        boolean perm1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean perm2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(perm1 && perm2)
        {
            //Check if location service is enabled on user's device
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Utils.Companion.promptEnableLocation(this);
            }
            else
            {
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null)
                        {
                            final String builder_n2yo_visual_passes_url;

                            if(sharedPreferences.getBoolean("use_fake_location", false))
                            {
                                builder_n2yo_visual_passes_url = n2yo_visual_passes_url +
                                        fakeLatitude + "/" +
                                        fakeLongitude + "/" +
                                        fakeAltitude + "/" +
                                        n2yo_visual_passes_days + "/" +
                                        n2yo_visual_passes_min_visibility + "/" +
                                        "&apiKey=" + n2yo_apikey;
                            }
                            else
                            {
                                builder_n2yo_visual_passes_url = n2yo_visual_passes_url +
                                        location.getLatitude() + "/" +
                                        location.getLongitude() + "/" +
                                        location.getAltitude() + "/" +
                                        n2yo_visual_passes_days + "/" +
                                        n2yo_visual_passes_min_visibility + "/" +
                                        "&apiKey=" + n2yo_apikey;
                            }

                            Log.e(TAG, builder_n2yo_visual_passes_url);

                            addToRequestQueue(new JsonObjectRequest(GET, builder_n2yo_visual_passes_url, null,
                                new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {
                                        n2yoVisualPasses = new N2yoVisualPasses(response);

                                        // Create the adapter that will return a fragment for each of the three
                                        // primary sections of the activity.
                                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                                        // Set up the ViewPager with the sections adapter.
                                        mViewPager = findViewById(R.id.container);
                                        mViewPager.setAdapter(mSectionsPagerAdapter);

                                        //Set up tabs
                                        TabLayout tabLayout = findViewById(R.id.tabs);
                                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
                                    }
                                }, new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        Log.e(TAG, error.getMessage());
                                    }
                                }
                            ));
                        }
                        else
                        {
                            Toast.makeText(PassesActivity.this, "Unable to retrieve your location", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(PassesActivity.this, "Failed to retrieve your location", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passes, menu);
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
        if (id == R.id.action_observation_info)
        {
            Intent infoFragment = new Intent(getApplicationContext(), ObserveIssInfoActivity.class);
            startActivity(infoFragment);
            return true;
        }

        if(id == R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

        public PlaceholderFragment() { }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            this.rootView = inflater.inflate(R.layout.fragment_passes, container, false);

            final int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            final TimeZone timeZone = TimeZone.getDefault();
            final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            //final DateFormat dateFormat = new SimpleDateFormat("yyyy MMMM dd", getResources().getConfiguration().getLocales().get(0));
            final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss z", getResources().getConfiguration().getLocales().get(0));
            dateFormat.setTimeZone(timeZone);

            //Check if there's not a 'sectionNumber-ish' pass
            if(sectionNumber > n2yoVisualPasses.getPasses().size())
            {
                this.rootView.findViewById(R.id.startPassLayout).setVisibility(View.GONE);
                this.rootView.findViewById(R.id.maxHeightPassLayout).setVisibility(View.GONE);
                this.rootView.findViewById(R.id.endPassLayout).setVisibility(View.GONE);

                this.rootView.findViewById(R.id.startLabel).setVisibility(View.GONE);
                this.rootView.findViewById(R.id.maxHeightLabel).setVisibility(View.GONE);
                this.rootView.findViewById(R.id.endLabel).setVisibility(View.GONE);

                this.rootView.findViewById(R.id.visibilitySecondsLabel).setVisibility(View.GONE);
                ((TextView) this.rootView.findViewById(R.id.visibleLabel)).setText(R.string.pass_no_pass_label);
                String count = "";
                switch(sectionNumber)
                {
                    case 1:
                        count = getResources().getString(R.string.pass_count_first);
                        break;

                    case 2:
                        count = getResources().getString(R.string.pass_count_second);
                        break;

                    case 3:
                        count = getResources().getString(R.string.pass_count_third);
                        break;
                }

                ((TextView) this.rootView.findViewById(R.id.visibilityView))
                        .setText(getString(R.string.pass_no_pass_message,
                                count,
                                n2yo_visual_passes_min_visibility,
                                n2yo_visual_passes_days));

                return this.rootView;
            }

            N2yoPass pass = n2yoVisualPasses.getPasses().get(sectionNumber - 1);

            //TextView textView = rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.passes_section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            Date startDate = Date.from(Instant.ofEpochSecond(pass.getStartUTC()));
            Date maxDate = Date.from(Instant.ofEpochSecond(pass.getMaxUTC()));
            Date endDate = Date.from(Instant.ofEpochSecond(pass.getEndUTC()));

            TextView visibilityView = this.rootView.findViewById(R.id.visibilityView);
            visibilityView.setText(String.valueOf(pass.getDuration()));

            //Start values
            View startPass = this.rootView.findViewById(R.id.startPassLayout);

            TextView startDateView = startPass.findViewById(R.id.dateView);
            startDateView.setText(dateFormat.format(startDate).toLowerCase());

            TextView startTimeView = startPass.findViewById(R.id.timeView);
            startTimeView.setText(timeFormat.format(startDate).toLowerCase());

            TextView startAzView = startPass.findViewById(R.id.azView);
            String startAz = pass.getStartAZ() + "°".toLowerCase();
            startAzView.setText(startAz);

            TextView startAzCompassView = startPass.findViewById(R.id.azCompassView);
            startAzCompassView.setText(pass.getStartAZCompass().toLowerCase());

            TextView startElView = startPass.findViewById(R.id.elView);
            String startEl = pass.getStartEl() + "°".toLowerCase();
            startElView.setText(startEl);

            //Max Height values
            View maxPassView = this.rootView.findViewById(R.id.maxHeightPassLayout);

            TextView maxDateView = maxPassView.findViewById(R.id.dateView);
            maxDateView.setText(dateFormat.format(maxDate).toLowerCase());

            TextView maxTimeView = maxPassView.findViewById(R.id.timeView);
            maxTimeView.setText(timeFormat.format(maxDate).toLowerCase());

            TextView maxAzView = maxPassView.findViewById(R.id.azView);
            String maxAz = pass.getMaxAz() + "°".toLowerCase();
            maxAzView.setText(maxAz);

            TextView maxAzCompassView = maxPassView.findViewById(R.id.azCompassView);
            maxAzCompassView.setText(pass.getMaxAzCompass().toLowerCase());

            TextView maxElView = maxPassView.findViewById(R.id.elView);
            String maxEl = pass.getMaxEl() + "°".toLowerCase();
            maxElView.setText(maxEl);

            //End values
            View endPassView = this.rootView.findViewById(R.id.endPassLayout);

            TextView endDateView = endPassView.findViewById(R.id.dateView);
            endDateView.setText(dateFormat.format(endDate).toLowerCase());

            TextView endTimeView = endPassView.findViewById(R.id.timeView);
            endTimeView.setText(timeFormat.format(endDate).toLowerCase());

            TextView endAzView = endPassView.findViewById(R.id.azView);
            String endAz = pass.getEndAz() + "°".toLowerCase();
            endAzView.setText(endAz);

            TextView endAzCompassView = endPassView.findViewById(R.id.azCompassView);
            endAzCompassView.setText(pass.getEndAzCompass().toLowerCase());

            TextView endElView = endPassView.findViewById(R.id.elView);
            String endEl = pass.getEndEl() + "°".toLowerCase();
            endElView.setText(endEl);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
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
}
