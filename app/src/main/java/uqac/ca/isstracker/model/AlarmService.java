package uqac.ca.isstracker.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {
    private IBinder monBinder;

    Alarm alarm = new Alarm();

    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.setAlarm(this);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public boolean onUnbind(Intent intent)
    {
        return false;
    }
}
