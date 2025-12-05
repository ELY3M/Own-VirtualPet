package own.virtualpet;

import android.util.Log;

import own.virtualpet.WeatherParser;

/**
 * Gets the current conditions from Google given a latitude and longitude
 * 
 * @author Jonathan
 * 
 */
public class WeatherRetriever
{
	static String TAG = "tama WeatherRetriever";
    //private static final String XML_SOURCE = "http://www.google.com/ig/api?weather=,,,";
	//https://forecast.weather.gov/MapClick.php?lat=44.9055&lon=-122.8107&lg=english&FcstType=json
	private static final String URL = "https://forecast.weather.gov/MapClick.php?";

    public static CurrentConditions getCurrentConditions(double latitude, double longitude)
    {
	String s = URL + "lat=" + latitude + "&lon=" + longitude + "&lg=english&FcstType=json";
	Log.i(TAG, "final weather url: " +s);

	CurrentConditions cc = WeatherParser.getCurrentConditions(s);
		Log.i(TAG, "weather: " + cc);
	return cc;
    }
}
