package pl.droidsonroids.droidsmap.base

import android.transition.Transition

abstract class BaseFeatureView<P : Presenter> {
    lateinit var presenter: P

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
