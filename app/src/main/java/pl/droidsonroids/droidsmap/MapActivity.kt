package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MapActivity : AppCompatActivity() {

    private lateinit var flowManager: FlowManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }

    override fun onBackPressed() {
        flowManager.onBackNavigationButtonPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        flowManager.onRequestPermissionsResult(requestCode, grantResults)
    }
}