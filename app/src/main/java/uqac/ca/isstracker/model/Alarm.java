package uqac.ca.isstracker.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm extends BroadcastReceiver {
    public static String EXTRA_ALARME = "EXTRA_ALARME";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("DIM", "Alarm.onReceive");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DIM:ALARM");
        wl.acquire();

        //MediaPlayer.create(context, R.raw.angry).start();
        Toast.makeText(context, "Alarm ! " + intent.getStringExtra(EXTRA_ALARME), Toast.LENGTH_LONG).show();

        wl.release();
    }

    public void setAlarm(Context context, int day, int hours, int minutes) {
        //TODO : a notification instead of a toast

        Log.i("DIM", "Alarm.setAlarm");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("EXTRA_ALARME", "IL EST l'HEURE");
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar datePass = getTargetTime(day, hours, minutes);
        am.setAlarmClock(new AlarmManager.AlarmClockInfo(datePass.getTimeInMillis(), pi), pi);
    }

    public void cancelAlarm(Context context)
    {
        Log.i("DIM", "Alarm.cancelAlarm");

        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private Calendar getTargetTime(int jour, int heure, int minute) {
        Calendar alarme = Calendar.getInstance();
        alarme.set(Calendar.DAY_OF_MONTH, jour);
        alarme.set(Calendar.HOUR_OF_DAY, heure);
        alarme.set(Calendar.MINUTE, minute);

        return alarme;
    }
}
