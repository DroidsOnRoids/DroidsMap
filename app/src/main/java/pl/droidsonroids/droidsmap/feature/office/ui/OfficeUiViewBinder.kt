package pl.droidsonroids.droidsmap.feature.office.ui

import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.scene_office_map.*
import pl.droidsonroids.droidsmap.MapActivity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeViewModel

class OfficeUiViewBinder(private val activity: MapActivity) {

    private var googleMap: GoogleMap? = null

    init {
        activity.map.getMapAsync { googleMap = it }
    }

    fun bind(model: OfficeViewModel) {
        if (googleMap == null) activity
    }
}