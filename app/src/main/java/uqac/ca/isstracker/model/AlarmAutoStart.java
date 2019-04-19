package uqac.ca.isstracker.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmAutoStart extends BroadcastReceiver {

    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Log.i("DIM", "ACTION_BOOT_COMPLETED");
            alarm.setAlarm(context);
        }
    }
}
