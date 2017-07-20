package pl.droidsonroids.droidsmap.base

import pl.droidsonroids.droidsmap.ForwardFlowChangeListener

abstract class Presenter<V : MvpView> {
    protected lateinit var view: V

    fun attachView(view: V) {
        this.view = view
    }

    abstract fun onFlowCallbackRegistered(flowCallback: ForwardFlowChangeListener)
}