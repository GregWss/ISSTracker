package uqac.ca.isstracker.other

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
    }
}