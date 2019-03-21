package uqac.ca.isstracker.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import uqac.ca.isstracker.R;

/**
 * This class holds every relevant information to show in the Home activity, acquired from APIs.
 */
public class Data
{
    private ArrayList<String> sentences;
    private ArrayList<String> values;
    public Data(Context context, ISSNow issNowData, ISSAstros issAstrosData, N2yoSatPos n2YoSatPosData)
    {
        //Static data
        this.sentences = new ArrayList<>();
        this.values = new ArrayList<>();
        this.sentences.addAll(Arrays.asList(context.getResources().getStringArray(
                R.array.iss_static_sentences)));
        this.values.addAll(Arrays.asList(context.getResources().getStringArray(
                R.array.iss_static_values_metric)));

        //API values
        //Astronauts
        this.sentences.add(context.getResources().getString(R.string.iss_people_sentence));
        this.values.add(String.valueOf(issAstrosData.getNumber()));
        //N2yoSatPos
        this.sentences.add(context.getResources().getString(R.string.iss_altitude));
        this.values.add(String.valueOf(n2YoSatPosData.getPositions().get(0).getSataltitude()) + " km");
    }

    /**
     * Returns the data couple at the given index, containing a sentence and his related value.
     * @param index the index of the data set where to get info from.
     * @return a data {@code String} array. String at index 0 is the sentence and string at index 1 is the value.
     */
    public String[] getSentenceValueCouple(int index)
    {
        if(index < 0 || index > this.size())
            return new String[]{"Error: Index param out of range", "---"};
        else
            return new String[]{this.sentences.get(index), this.values.get(index)};
    }

    /**
     * Returns the number of data couples registered.
     * @return number of data couples.
     */
    public int size()
    {
        return this.sentences.size();
    }
}
