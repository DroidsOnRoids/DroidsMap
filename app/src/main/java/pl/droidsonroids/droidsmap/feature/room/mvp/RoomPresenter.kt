package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary

class RoomPresenter(private val roomFeatureBoundary: RoomFeatureBoundary) : RoomPresenterContract() {

    override fun onPerspectiveLost() = view.performOfficeTransition()

    companion object {
        fun create(roomFeatureBoundary: RoomFeatureBoundary)
                = RoomPresenter(roomFeatureBoundary)
    }
}

abstract class RoomPresenterContract : Presenter<RoomMvpView>() {
    abstract fun onPerspectiveLost()
}