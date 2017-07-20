package pl.droidsonroids.droidsmap.base

import pl.droidsonroids.droidsmap.FlowNavigator

interface MvpView {
    fun onPerspectiveChanged(active: Boolean)
    fun registerFlowNavigator(flowNavigator: FlowNavigator)
}