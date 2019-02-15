package uqac.ca.isstracker.handlers;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import uqac.ca.isstracker.activities.HomeActivity;
import uqac.ca.isstracker.threads.HomeSideThread;

public class HomeSideHandler extends Handler
{
    private static final String TAG = "HOME HANDLER";

    private final WeakReference<AppCompatActivity> mActivity;
    private final TextView textSentence;
    private final TextView textValue;

    public HomeSideHandler(AppCompatActivity activity, TextView textSentence, TextView textValue)
    {
        this.mActivity = new WeakReference<>(activity);
        this.textSentence = textSentence;
        this.textValue = textValue;
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);

        switch (msg.what)
        {
            case HomeSideThread.UPDATE_UI:
                ((HomeActivity) mActivity.get()).setValuesAndAnimate();
                break;
        }
    }
}
