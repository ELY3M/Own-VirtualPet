package own.virtualpet

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.e175.klaus.solarpositioning.Grena3
import net.e175.klaus.solarpositioning.SPA
import own.virtualpet.databinding.ActivityMainBinding
import java.time.ZonedDateTime


class MainActivity : AppCompatActivity(), LocationListener {

    val TAG = "elys-pet"

/*
    lateinit var GoogleApiClient: GoogleApiClient
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 150000
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 300000
    var mCurrentLocation: Location? = null
    lateinit var mLocationRequest: LocationRequest
*/

    private lateinit var locationManager: LocationManager
    private var mylocation: Location? = null
    private var lat = 0.0
    private var lon = 0.0
    private var temp = "NA°F"
    private var icon = "unknown"
    private var finalicon = "unknown"
    //private var weather = "unknown"


    var ZENITH = 0.0
    private var weather: Int = Weather.NONE
    private var cc: CurrentConditions? = null
    private var weatherText: String? = null


    private lateinit var binding: ActivityMainBinding


    fun isOnline(): Boolean {
        val cm = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected) {
            Log.i(TAG, "network state = true")
            return true
        }
        return false
    }

    fun setBackground(drawable: Int) {

        var background = findViewById(R.id.container) as ConstraintLayout
        val tile = BitmapFactory.decodeResource(resources, drawable)
        val tiledBitmapDrawable = BitmapDrawable(resources, tile)
        tiledBitmapDrawable.tileModeX = Shader.TileMode.REPEAT
        tiledBitmapDrawable.tileModeY = Shader.TileMode.REPEAT
        background.setBackgroundDrawable(tiledBitmapDrawable)

    }


    fun dayornight(lat: Double, lon: Double) {
        var dateTime = ZonedDateTime.now()
        val solarposition = Grena3.calculateSolarPosition(dateTime, lat, lon, 67.0)
        Log.i(TAG, "Grena3: " + solarposition.zenithAngle)
        Log.i(TAG, "Grena3 lat: $lat lon: $lon")
        val solarposition2 = SPA.calculateSolarPosition(
            dateTime, lat, lon,
            190.0,  // elevation
            67.0,  // delta T
            1010.0,  // avg. air pressure
            11.0
        ) // avg. air temperature
        Log.i(TAG, "SPA (more acc but slow): " + solarposition2.zenithAngle)
        Log.i(TAG, "SPA lat: $lat lon: $lon")
        ZENITH = solarposition.zenithAngle

        if (ZENITH < 90.8) {
            Log.i(TAG, "day")
            setBackground(R.drawable.background_day)

        }
        if (ZENITH >= 90.8 && ZENITH < 96) {
            Log.i(TAG, "civil")
            setBackground(R.drawable.background_civil)
        }

        if (ZENITH >= 96 && ZENITH < 102) {
            Log.i(TAG, "nautical")
            setBackground(R.drawable.background_nautical)
        }

        if (ZENITH >= 102 && ZENITH < 108) {
            Log.i(TAG, "astronomical")
            setBackground(R.drawable.background_ast)
        }
        if (ZENITH >= 108) {
            Log.i(TAG, "night")
            setBackground(R.drawable.background_night)
        }
    }


    /////GPS Stuff//////
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 5000, 5f, this)

        mylocation = locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)

        if (mylocation != null) {
            lat = mylocation!!.latitude
            lon = mylocation!!.longitude
            Log.d(TAG, "Lat: " + lat)
            Log.d(TAG, "Lon: " + lon)
            getweather()

        } else {
            Log.d(TAG, "mylocation is null :(")
        }


    }

    override fun onLocationChanged(location: Location) {
        mylocation = location
        lat = mylocation!!.latitude
        lon = mylocation!!.longitude
        Log.d(TAG, "Lat: "+ lat)
        Log.d(TAG, "Lon: "+ lon)
        getweather()
    }
/////end of gps stuff/////




    /*
	*

	public static final int DRIZZLE = 0;
    public static final int HAIL = 6;
    public static final int HYRAIN = 3;
    public static final int HYSNOW = 9;
    public static final int ICEPELLETS = 5;
    public static final int LTRAIN = 1;
    public static final int LTSNOW = 7;
    public static final int NONE = -1;
    public static final int RAIN = 2;
    public static final int SNOW = 8;
    public static final int THUNDERSTORM = 4;
    public static final int UNKNOWN = 10;

	* */

    /*
	*

	public static final int DRIZZLE = 0;
    public static final int HAIL = 6;
    public static final int HYRAIN = 3;
    public static final int HYSNOW = 9;
    public static final int ICEPELLETS = 5;
    public static final int LTRAIN = 1;
    public static final int LTSNOW = 7;
    public static final int NONE = -1;
    public static final int RAIN = 2;
    public static final int SNOW = 8;
    public static final int THUNDERSTORM = 4;
    public static final int UNKNOWN = 10;

	* */

//weather gfx
    private fun loadWeather(type: Int) {



        if (type == Weather.SNOW) {
            weather = type
            //add snow
        } else if (type == Weather.RAIN) {
            weather = type
            //add rain
        } else {
            weather = Weather.NONE
        }


    }






    fun getweather() {
        Log.i(TAG, "My current location is: Latitude = $lat, Longitude = $lon")
        dayornight(lat, lon)

        val weathertext = findViewById(R.id.weathertext) as TextView

        if (weatherText != null) {
            Log.i(TAG, "weathertext is not null... should remove it.")
            weatherText = ""
        }
        if (isOnline()) {
            cc = WeatherRetriever.getCurrentConditions(lat, lon)
            Log.i(TAG, "cc: $cc")
        } else {
            cc = null
        }
        if (cc != null) {
            Log.i(TAG, cc.toString())
            weatherText = (cc!!.getCondition() + ", " + cc!!.getTempF()).toString() + "°F"
            //mainLayer.attachChild(weatherText)
            weathertext.setText(weatherText)

            var weatherType = -1
            val condition: String = cc!!.getCondition().toLowerCase()
            if (condition.contains("rain")) {
                weatherType = 2
            } else if (condition.contains("drizzle")) {
                weatherType = 0
            } else if (condition.contains("snow")) {
                weatherType = 8
            }
            loadWeather(weatherType)
        } else {
            weatherText = "Weather is not available"
            weathertext.setText(weatherText)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setBackground(R.drawable.background_day)


        getLocation()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}