package uqac.ca.isstracker.threads;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class HomeSideThread extends Thread implements Runnable
{
    private static final String TAG = "HOME TREAD";

    private Context context;
    private Handler handler;

    private boolean loop;
    private float startTime;
    private float elapsedTime;

    //Message identifiers
    public static final int UPDATE_UI = 0;

    public HomeSideThread(Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
        this.loop = false;
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        this.loop = true;
        this.startTime = System.currentTimeMillis();
        while(this.loop)
        {
            try
            {
                handler.sendMessage(handler.obtainMessage(UPDATE_UI));
                Thread.sleep(7000);
            }
            catch (InterruptedException ie)
            {
                Log.e(TAG, ie.getMessage());
            }
        }
    }
}
