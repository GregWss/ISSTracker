package uqac.ca.isstracker.model;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
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
        // When service started, get the pass infos from extras
        Bundle dateInfos = intent.getExtras();
        int day = dateInfos.getInt("Day");
        int hours = dateInfos.getInt("Hours");
        int minutes = dateInfos.getInt("Minutes");

        // Then set an alarm
        alarm.setAlarm(this, day, hours, minutes);
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
