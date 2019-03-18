package uqac.ca.isstracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import uqac.ca.isstracker.R;

public class InfoWindowMapAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;

    public InfoWindowMapAdapter(Context context) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_onmap, null);
    }

    private void renderWindowText (Marker marker, View view){
        // Setting the title
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.iwTitle);

        if (!tvTitle.equals("")){
            tvTitle.setText(title);
        }

        // Setting the content
        String content = marker.getSnippet();
        TextView tvContent = (TextView) view.findViewById(R.id.iwContent);

        if (!tvContent.equals("")){
            tvContent.setText(content);
        }
    }

    @Override
    public View getInfoWindow(Marker marker){
        renderWindowText(marker, mWindow);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return null;
    }

}
