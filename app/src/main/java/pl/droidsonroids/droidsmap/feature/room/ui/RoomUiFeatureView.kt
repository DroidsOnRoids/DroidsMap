package pl.droidsonroids.droidsmap.feature.room.ui

import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.scene_office_map.*
import kotlinx.android.synthetic.main.scene_room.*
import pl.droidsonroids.droidsmap.FlowManager
import pl.droidsonroids.droidsmap.MapActivity
import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenter

class RoomUiFeatureView(private val activity: MapActivity) : BaseFeatureView<RoomPresenter>(), RoomMvpView, RoomUiGateway {

    override fun performOfficeTransition() {
        with(activity) {
            val roomScene = Scene.getSceneForLayout(roomRootLayout, R.layout.scene_room, this)
            roomScene.setEnterAction {
                val roomSceneImage = (roomScene.sceneRoot.findViewById(R.id.zoomedRoomImage) as ImageView)
                roomSceneImage.setImageDrawable(roomImage.drawable)
            }
            val sceneTransition = TransitionInflater.from(this).inflateTransition(pl.droidsonroids.droidsmap.R.transition.room_scene_enter_transition)
            TransitionManager.go(roomScene, sceneTransition)
        }
    }

    override fun registerFlowChangeCallback(flowManager: FlowManager) = Unit

    override fun onPerspectiveChanged(active: Boolean) = Unit
}

interface RoomUiGateway {
    fun onPerspectiveChanged(active: Boolean)
    fun registerFlowChangeCallback(flowManager: FlowManager)
}