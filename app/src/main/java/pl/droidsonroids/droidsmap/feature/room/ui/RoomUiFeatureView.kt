package pl.droidsonroids.droidsmap.feature.room.ui

import pl.droidsonroids.droidsmap.ActivityWrapper
import pl.droidsonroids.droidsmap.Perspective
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenter

open class RoomUiFeatureView(private val activityWrapper: ActivityWrapper) : BaseFeatureView<RoomPresenter>(), RoomMvpView {

    override fun performRoomTransition() {
        with(activityWrapper) {
//            val roomScene = android.transition.Scene.getSceneForLayout(roomRoot, pl.droidsonroids.droidsmap.R.layout.scene_room, this)
//            roomScene.setEnterAction {
//                val roomSceneImage = (roomScene.sceneRoot.findViewById(pl.droidsonroids.droidsmap.R.id.zoomedRoomImage) as ImageView)
//                roomSceneImage.setImageDrawable(roomImage.drawable)
//            }
//            val sceneTransition = android.transition.TransitionInflater.from(this).inflateTransition(pl.droidsonroids.droidsmap.R.transition.room_scene_enter_transition)
//            android.transition.TransitionManager.go(roomScene, sceneTransition)

            TODO()
        }
    }

    fun onBackButtonPressed() {
        presenter.onBackButtonPressed()
    }

    open fun onPerspectiveChanged(perspective: Perspective) {
        TODO()
    }
}