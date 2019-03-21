package uqac.ca.isstracker.other

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.support.v7.app.AlertDialog

abstract class Utils
{
    companion object
    {
        fun test(): String
        {
            return "L'orage coule sur ton visage."
        }

        fun milesToKilometers(value: Float): Double
        {
            return value / 0.62137
        }

        fun kilometersToMiles(value: Float): Double
        {
            return value * 0.62137
        }

        fun promptEnableLocation(context: Context)
        {
            val alertPromptEnableGPS = AlertDialog.Builder(context)

            alertPromptEnableGPS.setMessage("Help us determine your location. That means sending " + "anonymous location data to us only when he app is running.")
                    .setTitle("Use location service?")
                    .setCancelable(true)
                    .setPositiveButton("Agree")
                    { _, _ ->
                        //Intent on location source system settings
                        val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(callGPSSettingIntent)
                    }

            alertPromptEnableGPS.setNegativeButton("Disagree") { dialog, _ -> dialog.cancel() }

            val alertActivateLocation = alertPromptEnableGPS.create()
            alertActivateLocation.show()
        }
    }
}