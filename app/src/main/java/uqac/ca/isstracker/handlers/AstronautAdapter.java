package uqac.ca.isstracker.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.squareup.picasso.Picasso;

import java.util.*;

import uqac.ca.isstracker.model.Astronaut;
import uqac.ca.isstracker.R;

public class AstronautAdapter extends ArrayAdapter<Astronaut> {

    private Context mContext;
    private List<Astronaut> astronautsList = new ArrayList<>();

    private ImageView mProfileImage;
    private ImageView mCountryFlag;
    private ImageView mAstronautTwitter;
    private ImageView mAstronautWiki;
    private TextView mName;
    private TextView mRole;
    private TextView mDate;
    private TextView mBio;

    private int currentPosition = -1;

    public AstronautAdapter(@NonNull Context context, ArrayList<Astronaut> list) {
        super(context, 0 , list);
        mContext = context;
        astronautsList = list;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        currentPosition = position;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.astronaut_row, parent, false);

        Astronaut currentAstronaut = astronautsList.get(currentPosition);

          mProfileImage = (ImageView) listItem.findViewById(R.id.img);
          mCountryFlag = (ImageView) listItem.findViewById(R.id.countryFlag);
          mAstronautTwitter = (ImageView) listItem.findViewById(R.id.astronautTwitter);
          mAstronautWiki = (ImageView) listItem.findViewById(R.id.astronautWiki);
          mName = (TextView) listItem.findViewById(R.id.name);
          mRole = (TextView) listItem.findViewById(R.id.role);
          mDate = (TextView) listItem.findViewById(R.id.days_since_launch);
          mBio = (TextView) listItem.findViewById(R.id.bio);


        Picasso.get().load(currentAstronaut.getImage()).into(mProfileImage);
        Picasso.get().load(currentAstronaut.getCountryLink()).into(mCountryFlag);

        mAstronautTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Astronaut currentAstronaut = astronautsList.get(currentPosition);
                if (currentAstronaut.getTwitter().length() != 0) {
                    Toast.makeText(mContext, currentAstronaut.getTwitter(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAstronautWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Astronaut currentAstronaut = astronautsList.get(currentPosition);
                if (currentAstronaut.getTwitter().length() != 0) {
                    Toast.makeText(mContext, currentAstronaut.getWiki(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mName.setText(currentAstronaut.getName());
        mRole.setText(currentAstronaut.getRole());
        mDate.setText(currentAstronaut.getLaunchDate());
        mBio.setText(currentAstronaut.getBio());

        return listItem;
    }
}
