package pl.droidsonroids.droidsmap.base

import pl.droidsonroids.droidsmap.FlowNavigator

abstract class Presenter<V : MvpView> {
    protected lateinit var view: V
    protected var flowChangeNavigator: FlowNavigator? = null
        set(value) = Unit

    open fun attachView(view: V) {
        this.view = view
        onViewAttached()
    }

    open fun registerFlowNavigator(flowNavigator: FlowNavigator) {
        this.flowChangeNavigator = flowNavigator
        onFlowNavigatorRegistered()
    }

    open fun onViewAttached() = Unit

    open fun onFlowNavigatorRegistered() = Unit
}