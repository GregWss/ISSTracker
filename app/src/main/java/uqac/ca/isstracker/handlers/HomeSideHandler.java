package uqac.ca.isstracker.handlers;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import uqac.ca.isstracker.activities.HomeActivity;
import uqac.ca.isstracker.threads.HomeSideThread;

public class HomeSideHandler extends Handler
{
    private static final String TAG = "HOME HANDLER";

    private final WeakReference<AppCompatActivity> mActivity;

    public HomeSideHandler(AppCompatActivity activity)
    {
        this.mActivity = new WeakReference<>(activity);
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
