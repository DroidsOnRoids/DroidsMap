package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary

class RoomPresenter(private val roomFeatureBoundary: RoomFeatureBoundary) : Presenter<RoomMvpView>() {

    companion object {
        fun create(roomFeatureBoundary: RoomFeatureBoundary)
                = RoomPresenter(roomFeatureBoundary)
    }
}