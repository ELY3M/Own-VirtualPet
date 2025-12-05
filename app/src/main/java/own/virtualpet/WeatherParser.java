package own.virtualpet;

import android.util.Log;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import own.virtualpet.CurrentConditions;

/**
 * This class parses the XML that is retrieved from the Google Weather API into a CurrentConditions object
 * 
 * @author Jonathan
 *
 * Google Weather API is gone now :(
 *
 */
public class WeatherParser
{
	static String TAG = "tama WeatherParser";

	static String condition = "none";
	//static String main = "none";
	static String temperature = "0";

	public static CurrentConditions getCurrentConditions(String url)
    {
		CurrentConditions cc = new CurrentConditions();
		handlejson obj;
		Log.i(TAG, "url: " + url);
		obj = new handlejson(url);
		obj.fetchJSON();
		while (obj.parsingComplete) ;
		condition = obj.getWeather();
		//main = obj.getmain();
		temperature = obj.getTemp();
		Log.i(TAG, "condition: " + condition + " temp: " + temperature);
		double finaltemp = Math.ceil(Double.valueOf(temperature));
		cc.setCondition(condition);
		//cc.setMain(main);
		cc.setTempF((int)finaltemp);

		Log.i(TAG,  "cc: " + cc);
		return cc;
    }






}
