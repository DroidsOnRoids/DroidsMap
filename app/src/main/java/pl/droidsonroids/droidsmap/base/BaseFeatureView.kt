package pl.droidsonroids.droidsmap.base

import android.transition.Transition
import pl.droidsonroids.droidsmap.MapActivity

abstract class BaseFeatureView<V : MvpView, P : Presenter<V>> : UiGateway, MvpView {
    lateinit var presenter: P

    override fun onPerspectiveGained() {
        presenter.attachView(this as V)
    }

    override fun onPerspectiveLost() {
    }

    object UiCommandInvoker {
        private val uiCommands = mutableListOf<() -> Any>()

        fun queueInvokement(function: () -> Any) {
            uiCommands += function
        }

        fun invokeQueuedChain() = uiCommands.forEach {
            it()
            uiCommands -= it
        }
    }

    inner class TransitionListenerAdapter(private val endAction: () -> Unit) : Transition.TransitionListener {

        override fun onTransitionResume(transition: Transition?) = Unit

        override fun onTransitionPause(transition: Transition?) = Unit

        override fun onTransitionCancel(transition: Transition?) = Unit

        override fun onTransitionStart(transition: Transition?) = Unit

        override fun onTransitionEnd(transition: Transition?) = endAction()
    }
}

open class MapActivityWrapper(val activity: MapActivity)
