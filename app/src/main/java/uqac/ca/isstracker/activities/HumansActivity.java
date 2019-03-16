package uqac.ca.isstracker.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//Volley imports
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

import uqac.ca.isstracker.R;
import uqac.ca.isstracker.handlers.AstronautAdapter;
import uqac.ca.isstracker.model.Astronaut;


public class HumansActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private ListView layoutToAdapt;
    private Activity mActivity;
    private AstronautAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astronaut);

        layoutToAdapt = findViewById(R.id.linearAstronaut);

        mActivity = this;
        mRequestQueue = Volley.newRequestQueue(this);

        display_people(this);

    }


    /**
     * Displays a list of astronauts in a RecyclerView
     */
    private void display_people(final Context mContext) {
        final String url = "http://www.howmanypeopleareinspacerightnow.com/peopleinspace.json";
        final ArrayList<Astronaut> peopleInSpace = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mAdapter = new AstronautAdapter(mContext, peopleInSpace);
                    layoutToAdapt.setAdapter(mAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject jsonResponse = new JSONObject(jsonString);
                    try {
                        JSONArray astronauts = jsonResponse.getJSONArray("people");

                        for (int i = 0; i < astronauts.length(); ++i) {
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
                            Astronaut storeAnAstronaut = new Astronaut(name, image, countryLink, launchDate, role, location, bio, wiki, twitter);
                            peopleInSpace.add(storeAnAstronaut);
                        }

                        Collections.sort(peopleInSpace);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        };


        mRequestQueue.add(jsonObjectRequest);
    }
}