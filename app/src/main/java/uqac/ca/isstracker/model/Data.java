package uqac.ca.isstracker.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uqac.ca.isstracker.R;

public class Data
{
    private ArrayList<String> sentences;
    private ArrayList<String> values;
    public Data(Context context, ISSNow issNowData, ISSPosition issPosData, N2yo n2yoData)
    {
        //Add static data
        this.sentences = new ArrayList<>();
        this.values = new ArrayList<>();
        this.sentences.addAll(Arrays.asList(context.getResources().getStringArray(R.array.iss_static_sentences)));
        this.values.addAll(Arrays.asList(context.getResources().getStringArray(R.array.iss_static_values_metric)));
    }

    public String[] getData(int index)
    {
        String[] out = new String[2];

        out[0] = this.sentences.get(index);
        out[1] = this.values.get(index);

        return out;
    }

    public int size()
    {
        return this.sentences.size();
    }
}
