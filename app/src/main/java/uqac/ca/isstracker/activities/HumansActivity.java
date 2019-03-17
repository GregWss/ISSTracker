package uqac.ca.isstracker.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

//Volley imports
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import uqac.ca.isstracker.R;
import uqac.ca.isstracker.adapters.AstronautAdapter;
import uqac.ca.isstracker.model.Astronaut;

public class HumansActivity extends AppCompatActivity
{
    private RequestQueue mRequestQueue;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String URL = "http://www.howmanypeopleareinspacerightnow.com/peopleinspace.json";

    private ArrayList<Astronaut> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humans);
        Toolbar toolbar = findViewById(R.id.humansToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.astroCardsView);


        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        dataset = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    recyclerView.setHasFixedSize(true);

                    // use a linear layout manager
                    layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    recyclerView.setLayoutManager(layoutManager);

                    // specify an adapter (see also next example)
                    adapter = new AstronautAdapter(getApplicationContext(), dataset);
                    recyclerView.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e) { }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
            {
                try
                {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject(jsonString);

                    try
                    {
                        JSONArray astronauts = jsonResponse.getJSONArray("people");

                        for (int i = 0; i < astronauts.length(); ++i)
                        {
                            JSONObject anAstronaut = astronauts.getJSONObject(i);

                            final String name = anAstronaut.getString("name");
                            final String image = anAstronaut.getString("biophoto");
                            final String countryLink = anAstronaut.getString("countryflag");
                            final String launchDate = anAstronaut.getString("launchdate");
                            String role = anAstronaut.getString("title");
                            final String location = anAstronaut.getString("location");
                            final String bio = anAstronaut.getString("bio");
                            final String wiki = anAstronaut.getString("biolink");
                            final String twitter = anAstronaut.getString("twitter");

                            if (role != null && !role.isEmpty())
                                role = role.substring(0, 1).toUpperCase() + role.substring(1);

                            Astronaut astronaut = new Astronaut(name, image, countryLink, launchDate, role, location, bio, wiki, twitter);
                            dataset.add(astronaut);
                        }

                        Collections.sort(dataset);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                }
                catch (UnsupportedEncodingException e)
                {
                    return Response.error(new ParseError(e));
                }
                catch (JSONException je)
                {
                    return Response.error(new ParseError(je));
                }
            }
        };

        mRequestQueue.add(request);
    }
}