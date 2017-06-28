package pl.droidsonroids.droidsmap.feature.office.ui

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.scene_office_map.*
import pl.droidsonroids.droidsmap.MapActivity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeViewModel

class OfficeUiFeatureView(activity: MapActivity) : OfficeMvpView {
    private val presenter = OfficePresenter.Factory().create(this)

    private val viewBinder = OfficeUiViewBinder(activity)
    private val commandInvoker: UiCommandInvoker = UiCommandInvoker()
    private var googleMap: GoogleMap? = null

    init {
        activity.map.getMapAsync {
            googleMap = it
            it.setup()
            commandInvoker.invokeQueuedChain()
        }
    }

    fun requestOffice() = presenter.onViewInitialized()

    override fun showMap(uiModel: OfficeViewModel) {
        if (googleMap == null) commandInvoker.queueInvokement { performShowMap(uiModel) } else performShowMap(uiModel)
    }

    private fun performShowMap(uiModel: OfficeViewModel) {
        val bounds = LatLngBounds.builder()
                .include(LatLng(uiModel.centerLatitude - 0.0005, uiModel.centerLongitude - 0.0005))
                .include(LatLng(uiModel.centerLatitude + 0.0005, uiModel.centerLongitude + 0.0005))
                .build()

        googleMap?.setLatLngBoundsForCameraTarget(bounds)
    }
}

private class UiCommandInvoker {
    private val uiCommands = mutableListOf<() -> Any>()

    fun queueInvokement(function: () -> Any) = uiCommands.add(function)

    fun invokeQueuedChain() = uiCommands.forEach {
        it()
        uiCommands -= it
    }
}

private fun GoogleMap.setup() {
    with(uiSettings) {
        isZoomControlsEnabled = false
        isCompassEnabled = false
        isMyLocationButtonEnabled = false
    }
}

