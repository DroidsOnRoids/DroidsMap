package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MapActivity : AppCompatActivity() {

    private lateinit var activityWrapper: ActivityWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        activityWrapper = ActivityWrapper(this)
    }

    override fun onBackPressed() {
        activityWrapper.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activityWrapper.onRequestPermissionsResult(requestCode, grantResults)
    }
}