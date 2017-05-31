package pl.droidsonroids.droidsmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.widget.GridLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.scene_office.*


class MapActivity : AppCompatActivity() {
    private var shouldMoveBack = true
    private var officeScene: Scene? = null
    private var currentRoom: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        officeScene = Scene(rootLayout, officeSceneLayout)

        roomImagesGridLayout.getRoomImages()
                .forEach {
                    it?.setOnClickListener {
                        currentRoom = it as ImageView
                        it.transitionName = "room_transition"
                        performRoomTransition()
                    }
                }
    }

    fun performRoomTransition() {
        shouldMoveBack = false
        val roomScene = Scene.getSceneForLayout(rootLayout, R.layout.scene_room, this)
        val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_transition)
        TransitionManager.go(roomScene, sceneTransition)
    }

    private fun performOfficeTransition() {
        val sceneTransition = TransitionInflater.from(this).inflateTransition(R.transition.room_scene_transition)
        sceneTransition.addListener(TransitionListenerAdapter({ currentRoom?.transitionName = "" }))
        TransitionManager.go(officeScene, sceneTransition)
    }

    override fun onBackPressed() {
        if (shouldMoveBack) {
            super.onBackPressed()
        } else {
            performOfficeTransition()
            shouldMoveBack = true
        }
    }
}

private fun GridLayout.getRoomImages() = (0..childCount)
        .map {
            getChildAt(it) as ImageView?
        }.toList()

private class TransitionListenerAdapter(runnableAction: () -> Unit) : Transition.TransitionListener {
    private val runnableAction = runnableAction

    override fun onTransitionResume(transition: Transition?) {
        //no-op
    }

    override fun onTransitionPause(transition: Transition?) {
        //no-op
    }

    override fun onTransitionCancel(transition: Transition?) {
        //no-op
    }

    override fun onTransitionStart(transition: Transition?) {
        //no-op
    }

    override fun onTransitionEnd(transition: Transition?) {
        kotlin.run { runnableAction() }
    }
}