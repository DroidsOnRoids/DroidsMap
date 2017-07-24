package pl.droidsonroids.droidsmap.feature.room.ui

import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.scene_office_map.*
import kotlinx.android.synthetic.main.scene_room.*
import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.base.UiGateway
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenterContract

class RoomUiFeatureView(private val activityWrapper: MapActivityWrapper, presenter: RoomPresenterContract) : BaseFeatureView<RoomMvpView, RoomPresenterContract>(), RoomMvpView, RoomUiGateway {

    init {
        this.presenter = presenter
    }

    override fun performOfficeTransition() {
        with(activityWrapper.activity) {
            val roomScene = Scene.getSceneForLayout(roomRootLayout, R.layout.scene_room, this)
            roomScene.setEnterAction {
                val roomSceneImage = (roomScene.sceneRoot.findViewById(R.id.zoomedRoomImage) as ImageView)
                roomSceneImage.setImageDrawable(roomImage.drawable)
            }
            val sceneTransition = TransitionInflater.from(this).inflateTransition(pl.droidsonroids.droidsmap.R.transition.room_scene_enter_transition)
            TransitionManager.go(roomScene, sceneTransition)
        }
    }
}

interface RoomUiGateway : UiGateway

