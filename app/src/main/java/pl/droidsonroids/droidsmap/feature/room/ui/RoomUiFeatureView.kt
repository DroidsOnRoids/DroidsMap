package pl.droidsonroids.droidsmap.feature.room.ui

import android.app.Activity
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenter

class RoomUiFeatureView(activity: Activity) : BaseFeatureView<RoomPresenter>() {
    fun onBackButtonPressed() {
        presenter.onBackButtonPressed()
    }
}