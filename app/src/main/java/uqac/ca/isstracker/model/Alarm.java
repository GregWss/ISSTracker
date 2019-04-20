package uqac.ca.isstracker.model;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import uqac.ca.isstracker.R;
import uqac.ca.isstracker.activities.PassesActivity;

public class Alarm extends BroadcastReceiver {
    public static String EXTRA_ALARME = "EXTRA_ALARME";

    private static final String CHANNEL_ID = "PASS_NOTIF_CHAN";
    private int notificationId;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //TODO : a notification instead of a toast
        Log.i("DIM", "Alarm.onReceive");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DIM:ALARM");
        wl.acquire();

        // Sending the notification
        createNotificationAndNotify(context);

        wl.release();
    }

    public void setAlarm(Context context, int day, int hours, int minutes) {
        Log.i("DIM", "Alarm.setAlarm");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

    public void createNotificationAndNotify(Context context){
        Intent intentToLaunch = new Intent(context, PassesActivity.class);
        intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToLaunch, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.iss_logo)
                .setContentTitle("The ISS is coming !")
                .setContentText("Look up, the ISS is passing near you !")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Look up, the ISS is passing near you !"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setTicker("Look up, the ISS is passing near you !")
                .setVibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500})
                .setLights(0xff00ffff, 500, 1000)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
}
