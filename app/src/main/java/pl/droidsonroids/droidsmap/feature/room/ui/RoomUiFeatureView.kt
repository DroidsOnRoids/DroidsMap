package pl.droidsonroids.droidsmap.feature.room.ui

import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office_map.*
import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.base.BaseFeatureView
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.base.UiGateway
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenterContract

class RoomUiFeatureView(private val activityWrapper: MapActivityWrapper, presenter: RoomPresenterContract) : BaseFeatureView<RoomMvpView, RoomPresenterContract>(), RoomMvpView, RoomUiGateway {

    private val officeScene: Scene

    init {
        this.presenter = presenter
        officeScene = Scene(activityWrapper.activity.rootLayout, activityWrapper.activity.officeSceneLayout)
    }

    override fun onPerspectiveLost() {
        super.onPerspectiveLost()
        presenter.onPerspectiveLost()
    }

    override fun performOfficeTransition() {
        with(activityWrapper.activity) {
            val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_exit_transition)
            sceneTransition.addListener(TransitionListenerAdapter({
                roomImage.setImageDrawable(null)
            }))
            TransitionManager.go(officeScene, sceneTransition)
        }
    }
}

interface RoomUiGateway : UiGateway

