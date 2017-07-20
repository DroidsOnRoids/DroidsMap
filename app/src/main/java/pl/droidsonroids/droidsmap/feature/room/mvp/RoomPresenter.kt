package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.ForwardFlowChangeListener
import pl.droidsonroids.droidsmap.base.Presenter
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary

class RoomPresenter(private val roomFeatureBoundary: RoomFeatureBoundary) : Presenter<RoomMvpView>() {
    override fun onFlowCallbackRegistered(flowCallback: ForwardFlowChangeListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun create(roomFeatureBoundary: RoomFeatureBoundary)
                = RoomPresenter(roomFeatureBoundary)
    }
}